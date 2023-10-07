package top.sheepyu.module.system.service.permission;

import top.sheepyu.framework.mybatisplus.core.query.IServiceX;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.system.controller.admin.permission.role.SystemRoleCreateVo;
import top.sheepyu.module.system.controller.admin.permission.role.SystemRoleUpdateVo;
import top.sheepyu.module.system.dao.permission.role.SystemRole;
import top.sheepyu.module.system.service.permission.bo.SystemRoleQueryBo;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author ygq
 * @date 2023-01-29 17:55
 **/
public interface SystemRoleService extends IServiceX<SystemRole> {
    void createRole(@Valid SystemRoleCreateVo createVo);

    void updateRole(@Valid SystemRoleUpdateVo updateVo);

    void deleteRole(Collection<Long> idList);

    void transfer(Long sourceDeptId, Long targetDeptId);

    PageResult<SystemRole> pageAllRole(SystemRoleQueryBo queryBo);

    PageResult<SystemRole> pageRoleByPermission(@Valid SystemRoleQueryBo queryBo);

    List<SystemRole> listAllRole();

    List<SystemRole> listRoleByCreators(List<String> creatorList);

    List<SystemRole> listRoleByDeptIds(Set<Long> deptIds);

    SystemRole findById(Long id);

    boolean isSuperAdmin(Long roleId);

    boolean hasAnySuperAdmin(Set<Long> roleIds);

    List<SystemRole> findRoleByIds(Set<Long> roleIds);
}
