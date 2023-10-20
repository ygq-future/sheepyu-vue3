package top.sheepyu.module.system.service.dept;

import cn.hutool.core.collection.CollUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.module.system.controller.admin.dept.vo.SystemDeptCreateVo;
import top.sheepyu.module.system.controller.admin.dept.vo.SystemDeptQueryVo;
import top.sheepyu.module.system.controller.admin.dept.vo.SystemDeptUpdateVo;
import top.sheepyu.module.system.dao.dept.SystemDept;
import top.sheepyu.module.system.dao.dept.SystemDeptQueryDeptMapper;
import top.sheepyu.module.system.dao.dept.SystemDeptRoleMapper;
import top.sheepyu.module.system.dao.permission.role.SystemRole;
import top.sheepyu.module.system.dao.user.SystemUser;
import top.sheepyu.module.system.dao.user.SystemUserDeptMapper;
import top.sheepyu.module.system.service.permission.PermissionBiz;
import top.sheepyu.module.system.service.permission.SystemRoleService;
import top.sheepyu.module.system.service.user.SystemUserService;

import java.util.*;

import static top.sheepyu.framework.security.util.SecurityFrameworkUtil.getLoginUserId;
import static top.sheepyu.module.common.enums.CommonStatusEnum.DISABLE;
import static top.sheepyu.module.common.util.CollectionUtil.*;
import static top.sheepyu.module.system.enums.dept.DeptTypeEnum.*;

/**
 * @author ygq
 * @date 2023-09-26 11:51
 **/
@Service
@Validated
@AllArgsConstructor
public class SystemDeptBiz {
    private final SystemDeptService systemDeptService;
    private final SystemUserDeptMapper systemUserDeptMapper;
    private final SystemUserService systemUserService;
    private final PermissionBiz permissionBiz;
    private final SystemDeptRoleMapper systemDeptRoleMapper;
    private final SystemDeptQueryDeptMapper systemDeptQueryDeptMapper;
    private final SystemRoleService systemRoleService;

    @Transactional(rollbackFor = Exception.class)
    public void createDept(SystemDeptCreateVo createVo) {
        Long deptId = systemDeptService.createDept(createVo);
        Set<Long> targetDeptIds = createVo.getTargetDeptIds();
        if (permissionBiz.isSuperAdminRole() && CollUtil.isNotEmpty(targetDeptIds)) {
            systemDeptQueryDeptMapper.insertSourceIdAndTargetIds(deptId, targetDeptIds);
        }
        permissionBiz.assignUserToDept(deptId, createVo.getLeaderUserIds());
        permissionBiz.assignRoleToDept(deptId, new HashSet<>());
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateDept(SystemDeptUpdateVo updateVo) {
        systemDeptService.updateDept(updateVo);
        if (permissionBiz.isSuperAdminRole()) {
            updateDeptQueryDept(updateVo.getId(), updateVo.getTargetDeptIds());
        }
        permissionBiz.assignUserToDept(updateVo.getId(), updateVo.getLeaderUserIds());
    }

    public List<SystemDept> listDeptByPermission(SystemDeptQueryVo queryVo) {
        List<SystemDept> deptList = systemDeptService.listDeptByPermission(queryVo);
        deptList.forEach(dept -> {
            if (dept.getType().equals(DEPT.getCode())) {
                this.fillLeaderUserInfo(dept);
            }
        });
        return systemDeptService.deptListToTree(deptList);
    }

    /**
     * 查询用户所能够查询的部门/职位下的用户, 非用户都是禁用
     * 如果某个部门/职位下面有用户, 那么此部门/职位为启用状态, 为了方便批量选择
     *
     * @return List<SystemDept>
     */
    public List<SystemDept> listDeptUser() {
        List<SystemDept> deptList = systemDeptService.listDeptByPermission(new SystemDeptQueryVo());
        for (SystemDept dept : deptList) {
            Set<Long> userIds = systemUserDeptMapper.findUserIdByDeptIds(Collections.singleton(dept.getId()));
            if (CollUtil.isEmpty(userIds)) {
                dept.setDisabled(true);
                continue;
            }
            List<SystemUser> userList = systemUserService.lambdaQuery()
                    .in(SystemUser::getId, userIds)
                    .select(SystemUser::getId, SystemUser::getUsername, SystemUser::getStatus)
                    .list();
            //把User和Dept同化放在一颗树上
            List<SystemDept> userDeptChildren = convertList(userList, e -> {
                SystemDept userDept = new SystemDept();
                userDept.setId(e.getId())
                        .setParentId(dept.getId())
                        .setName(e.getUsername().concat("(user)"))
                        .setType(USER.getCode())
                        .setDisabled(Objects.equals(DISABLE.getCode(), e.getStatus()));
                return userDept;
            });
            dept.setChildren(userDeptChildren);
        }
        return systemDeptService.deptListToTree(deptList);
    }

    /**
     * 查询用户所能够查询的部门/职位下的角色, 非角色都是禁用
     * 如果某个部门/职位下面有角色, 那么此部门/职位为启用状态, 为了方便批量选择
     *
     * @return List<SystemDept>
     */
    public List<SystemDept> listDeptRole() {
        List<SystemDept> deptList = systemDeptService.listDeptByPermission(new SystemDeptQueryVo());
        //查询用户所拥有的角色
        Set<Long> hasRoleIds = permissionBiz.findRoleIdsByUserId(getLoginUserId());
        List<SystemRole> hasRoles = new ArrayList<>();
        if (CollUtil.isNotEmpty(hasRoleIds)) {
            hasRoles = systemRoleService.listByIds(hasRoleIds);
        }
        for (SystemDept dept : deptList) {
            List<SystemRole> roleList = systemRoleService.listRoleByDeptIds(Collections.singleton(dept.getId()));
            if (CollUtil.isEmpty(roleList)) {
                dept.setDisabled(true);
                continue;
            }
            //判断所拥有的角色是否已经在自己部门的管理下了,如果不在就要添加到根节点数据
            List<Long> containRoleIds = convertListFilter(roleList, SystemRole::getId, e -> hasRoleIds.contains(e.getId()));
            hasRoles = filterList(hasRoles, e -> !containRoleIds.contains(e.getId()));
            //把Role和Dept同化放在一颗树上
            List<SystemDept> roleDeptChildren = convertList(roleList, e -> {
                SystemDept roleDept = new SystemDept();
                roleDept.setId(e.getId())
                        .setParentId(dept.getId())
                        .setName(e.getName().concat("(role)"))
                        .setType(ROLE.getCode());
                return roleDept;
            });
            dept.setChildren(roleDeptChildren);
        }
        //说明这些角色是自己已经拥有的,但是没有在自己部门的管理下面
        if (CollUtil.isNotEmpty(hasRoles)) {
            List<SystemDept> hasRoleDepts = convertList(hasRoles, e -> {
                SystemDept roleDept = new SystemDept();
                roleDept.setId(e.getId())
                        .setParentId(0L)
                        .setName(e.getName().concat("(role)"))
                        .setType(ROLE.getCode());
                return roleDept;
            });
            deptList.addAll(hasRoleDepts);
        }
        return systemDeptService.deptListToTree(deptList);
    }

    public SystemDept findById(Long id) {
        SystemDept dept = systemDeptService.findById(id);
        if (dept.getType().equals(DEPT.getCode())) {
            this.fillLeaderUserInfo(dept);
        }
        this.fillQueryDeptInfo(dept);
        return dept;
    }

    public List<SystemDept> treeByPermission() {
        return systemDeptService.treeByPermission();
    }

    @Transactional
    public void deleteDept(Long id) {
        //将此部门下的角色转移到此部门的父部门下
        SystemDept dept = systemDeptService.findById(id);
        systemRoleService.transfer(id, dept.getParentId());
        //删除部门及其关联
        systemDeptService.deleteDept(id);
        systemDeptRoleMapper.deleteByDeptId(id);
        systemDeptQueryDeptMapper.deleteBySourceId(id);
    }

    private void updateDeptQueryDept(Long sourceId, Set<Long> targetDeptIds) {
        Set<Long> oldTargetIds = systemDeptQueryDeptMapper.findBySourceId(sourceId);
        Collection<Long> addList = CollUtil.subtract(targetDeptIds, oldTargetIds);
        Collection<Long> removeList = CollUtil.subtract(oldTargetIds, targetDeptIds);
        if (CollUtil.isNotEmpty(addList)) {
            systemDeptQueryDeptMapper.insertSourceIdAndTargetIds(sourceId, addList);
        }
        if (CollUtil.isNotEmpty(removeList)) {
            systemDeptQueryDeptMapper.deleteBySourceIdAndTargetIds(sourceId, removeList);
        }
    }

    private void fillLeaderUserInfo(SystemDept dept) {
        Set<Long> userIds = systemUserDeptMapper.findUserIdByDeptIds(Collections.singleton(dept.getId()));
        List<String> nicknameList = systemUserService.findFieldValueByIds(SystemUser::getNickname, userIds);
        dept.setLeaderUserIds(userIds);
        dept.setLeaderNicknames(String.join(",", nicknameList));
    }

    private void fillQueryDeptInfo(SystemDept dept) {
        Set<Long> targetDeptIds = systemDeptQueryDeptMapper.findBySourceId(dept.getId());
        dept.setTargetDeptIds(targetDeptIds);
    }
}
