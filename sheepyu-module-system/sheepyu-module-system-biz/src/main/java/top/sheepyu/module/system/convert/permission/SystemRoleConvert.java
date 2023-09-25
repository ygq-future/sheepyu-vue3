package top.sheepyu.module.system.convert.permission;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.system.controller.admin.permission.role.SystemRoleCreateVo;
import top.sheepyu.module.system.controller.admin.permission.role.SystemRoleRespVo;
import top.sheepyu.module.system.controller.admin.permission.role.SystemRoleUpdateVo;
import top.sheepyu.module.system.dao.permission.role.SystemRole;

import java.util.List;

/**
 * @author ygq
 * @date 2023-01-29 18:01
 **/
@Mapper
public interface SystemRoleConvert {
    SystemRoleConvert CONVERT = Mappers.getMapper(SystemRoleConvert.class);

    SystemRole convert(SystemRoleCreateVo createVo);

    SystemRole convert(SystemRoleUpdateVo updateVo);

    SystemRoleRespVo convert(SystemRole systemRole);

    List<SystemRoleRespVo> convertList(List<SystemRole> list);

    PageResult<SystemRoleRespVo> convertPage(PageResult<SystemRole> pageResult);
}
