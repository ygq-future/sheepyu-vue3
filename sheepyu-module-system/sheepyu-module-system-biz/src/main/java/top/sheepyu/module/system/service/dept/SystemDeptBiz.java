package top.sheepyu.module.system.service.dept;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.module.system.controller.admin.dept.vo.SystemDeptCreateVo;
import top.sheepyu.module.system.controller.admin.dept.vo.SystemDeptQueryVo;
import top.sheepyu.module.system.controller.admin.dept.vo.SystemDeptUpdateVo;
import top.sheepyu.module.system.dao.dept.SystemDept;
import top.sheepyu.module.system.dao.dept.SystemDeptRoleMapper;
import top.sheepyu.module.system.dao.user.SystemUser;
import top.sheepyu.module.system.dao.user.SystemUserDeptMapper;
import top.sheepyu.module.system.service.permission.PermissionBiz;
import top.sheepyu.module.system.service.user.SystemUserService;

import java.util.Collections;
import java.util.List;
import java.util.Set;

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

    @Transactional(rollbackFor = Exception.class)
    public void createDept(SystemDeptCreateVo createVo) {
        Long deptId = systemDeptService.createDept(createVo);
        permissionBiz.assignUserToDept(deptId, createVo.getLeaderUserIds());
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateDept(SystemDeptUpdateVo updateVo) {
        systemDeptService.updateDept(updateVo);
        permissionBiz.assignUserToDept(updateVo.getId(), updateVo.getLeaderUserIds());
    }

    public List<SystemDept> listDept(SystemDeptQueryVo queryVo) {
        List<SystemDept> deptList = systemDeptService.listDept(queryVo);
        deptList.forEach(this::fillLeaderUserInfo);
        return systemDeptService.deptListToTree(deptList);
    }

    public SystemDept findById(Long id) {
        SystemDept dept = systemDeptService.findById(id);
        this.fillLeaderUserInfo(dept);
        return dept;
    }

    private void fillLeaderUserInfo(SystemDept dept) {
        Set<Long> userIds = systemUserDeptMapper.findUserIdByDeptIds(Collections.singleton(dept.getId()));
        List<String> nicknameList = systemUserService.findFieldValueByIds(SystemUser::getNickname, userIds);
        dept.setLeaderUserIds(userIds);
        dept.setLeaderNicknames(String.join(",", nicknameList));
    }

    public List<SystemDept> tree() {
        return systemDeptService.tree();
    }

    @Transactional
    public void deleteDept(Long id) {
        systemDeptService.deleteDept(id);
        systemDeptRoleMapper.deleteByDeptId(id);
    }
}
