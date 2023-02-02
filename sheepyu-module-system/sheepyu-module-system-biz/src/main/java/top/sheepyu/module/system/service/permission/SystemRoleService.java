package top.sheepyu.module.system.service.permission;

import top.sheepyu.framework.mybatisplus.core.query.IServiceX;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.system.controller.admin.permission.role.SystemRoleCreateVo;
import top.sheepyu.module.system.controller.admin.permission.role.SystemRoleQueryVo;
import top.sheepyu.module.system.controller.admin.permission.role.SystemRoleUpdateVo;
import top.sheepyu.module.system.dao.permission.role.SystemRole;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * @author ygq
 * @date 2023-01-29 17:55
 **/
public interface SystemRoleService extends IServiceX<SystemRole> {
    void createRole(@Valid SystemRoleCreateVo createVo);

    void updateRole(@Valid SystemRoleUpdateVo updateVo);

    void deleteRole(Long id);

    PageResult<SystemRole> pageRole(@Valid SystemRoleQueryVo queryVo);

    List<SystemRole> listRole();

    SystemRole findById(Long id);

    boolean isSuperAdmin(Long roleId);

    boolean isAnySuperAdmin(Set<Long> roleIds);

    List<SystemRole> findRoleByIdsFromCache(Set<Long> roleIds);
}
