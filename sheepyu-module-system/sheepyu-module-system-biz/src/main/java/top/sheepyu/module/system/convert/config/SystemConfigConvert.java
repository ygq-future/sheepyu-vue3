package top.sheepyu.module.system.convert.config;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.system.controller.admin.config.vo.SystemConfigCreateVo;
import top.sheepyu.module.system.controller.admin.config.vo.SystemConfigRespVo;
import top.sheepyu.module.system.controller.admin.config.vo.SystemConfigUpdateVo;
import top.sheepyu.module.system.dao.config.SystemConfig;

import java.util.List;

/**
 * @author ygq
 * @date 2023-01-21 15:38
 **/
@Mapper
public interface SystemConfigConvert {
    SystemConfigConvert CONVERT = Mappers.getMapper(SystemConfigConvert.class);

    SystemConfig convert(SystemConfigCreateVo createVo);

    SystemConfig convert(SystemConfigUpdateVo updateVo);

    SystemConfigRespVo convert(SystemConfig systemConfig);

    List<SystemConfigRespVo> convertList(List<SystemConfig> list);

    PageResult<SystemConfigRespVo> convertPage(PageResult<SystemConfig> pageResult);
}
