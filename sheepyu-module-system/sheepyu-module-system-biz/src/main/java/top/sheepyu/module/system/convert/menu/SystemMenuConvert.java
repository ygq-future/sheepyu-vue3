package top.sheepyu.module.system.convert.menu;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import top.sheepyu.module.system.controller.admin.menu.vo.SystemMenuCreateVo;
import top.sheepyu.module.system.controller.admin.menu.vo.SystemMenuRespVo;
import top.sheepyu.module.system.controller.admin.menu.vo.SystemMenuUpdateVo;
import top.sheepyu.module.system.dao.menu.SystemMenu;

import java.util.List;

/**
 * @author ygq
 * @date 2023-01-29 18:01
 **/
@Mapper
public interface SystemMenuConvert {
    SystemMenuConvert CONVERT = Mappers.getMapper(SystemMenuConvert.class);

    SystemMenu convert(SystemMenuCreateVo createVo);

    SystemMenu convert(SystemMenuUpdateVo updateVo);

    SystemMenuRespVo convert(SystemMenu systemConfig);

    List<SystemMenuRespVo> convertList(List<SystemMenu> list);
}
