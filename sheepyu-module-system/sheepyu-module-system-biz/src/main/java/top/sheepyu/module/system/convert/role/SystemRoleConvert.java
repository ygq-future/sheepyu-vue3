package top.sheepyu.module.system.convert.role;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.system.controller.admin.role.vo.SystemRoleCreateVo;
import top.sheepyu.module.system.controller.admin.role.vo.SystemRoleRespVo;
import top.sheepyu.module.system.controller.admin.role.vo.SystemRoleUpdateVo;
import top.sheepyu.module.system.dao.role.SystemRole;

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

    SystemRoleRespVo convert(SystemRole systemConfig);

    List<SystemRoleRespVo> convertList(List<SystemRole> list);

    PageResult<SystemRoleRespVo> convertPage(PageResult<SystemRole> pageResult);
}
