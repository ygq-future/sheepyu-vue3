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
import top.sheepyu.module.system.dao.user.SystemUser;
import top.sheepyu.module.system.dao.user.SystemUserDeptMapper;
import top.sheepyu.module.system.service.permission.PermissionBiz;
import top.sheepyu.module.system.service.user.SystemUserService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Transactional(rollbackFor = Exception.class)
    public void createDept(SystemDeptCreateVo createVo) {
        Long deptId = systemDeptService.createDept(createVo);
        Set<Long> targetDeptIds = createVo.getTargetDeptIds();
        if (permissionBiz.isSuperAdminRole() && CollUtil.isNotEmpty(targetDeptIds)) {
            systemDeptQueryDeptMapper.insertSourceIdAndTargetIds(deptId, targetDeptIds);
        }
        permissionBiz.assignUserToDept(deptId, createVo.getLeaderUserIds());
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
            if (dept.getType().equals(GROUP.getCode())) {
                this.fillLeaderUserInfo(dept);
            }
        });
        return systemDeptService.deptListToTree(deptList);
    }

    /**
     * 查询用户所能够查询的权限项下的用户, 非用户都是禁用
     *
     * @return List<SystemDept>
     */
    public List<SystemDept> listDeptUser() {
        List<SystemDept> deptList = systemDeptService.listDeptByPermission(new SystemDeptQueryVo());
        for (SystemDept dept : deptList) {
            if (!dept.getType().equals(ITEM.getCode())) {
                dept.setDisabled(true);
                continue;
            }
            Set<Long> userIds = systemUserDeptMapper.findUserIdByDeptIds(Collections.singleton(dept.getId()));
            if (CollUtil.isNotEmpty(userIds)) {
                List<SystemUser> userList = systemUserService.lambdaQuery()
                        .in(SystemUser::getId, userIds)
                        .select(SystemUser::getId, SystemUser::getUsername)
                        .list();
                //把User和Dept同化放在一颗树上
                List<SystemDept> userChildren = userList.stream().map(e -> {
                    SystemDept userDept = new SystemDept();
                    userDept.setId(e.getId())
                            .setParentId(dept.getId())
                            .setName(e.getUsername())
                            .setType(USER.getCode());
                    return userDept;
                }).collect(Collectors.toList());
                dept.setChildren(userChildren);
            }
        }
        return systemDeptService.deptListToTree(deptList);
    }

    public SystemDept findById(Long id) {
        SystemDept dept = systemDeptService.findById(id);
        if (dept.getType().equals(GROUP.getCode())) {
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
