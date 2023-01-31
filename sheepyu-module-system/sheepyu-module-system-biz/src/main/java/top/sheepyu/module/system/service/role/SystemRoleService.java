package top.sheepyu.module.system.service.role;

import top.sheepyu.framework.mybatisplus.core.query.IServiceX;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.system.controller.admin.role.vo.SystemRoleCreateVo;
import top.sheepyu.module.system.controller.admin.role.vo.SystemRoleQueryVo;
import top.sheepyu.module.system.controller.admin.role.vo.SystemRoleUpdateVo;
import top.sheepyu.module.system.dao.role.SystemRole;

import javax.validation.Valid;
import java.util.List;

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

    List<SystemRole> listRoleByUserId(Long userId);
}
