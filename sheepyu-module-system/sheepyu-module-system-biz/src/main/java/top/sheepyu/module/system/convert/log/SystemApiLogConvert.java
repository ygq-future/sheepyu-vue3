package top.sheepyu.module.system.convert.log;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.system.controller.admin.log.api.SystemApiLogRespVo;
import top.sheepyu.module.system.dao.log.SystemApiLog;
import top.sheepyu.module.system.dto.ApiLogDto;

/**
 * @author ygq
 * @date 2023-01-18 11:28
 **/
@Mapper
public interface SystemApiLogConvert {
    SystemApiLogConvert CONVERT = Mappers.getMapper(SystemApiLogConvert.class);

    SystemApiLog convert(ApiLogDto apiLogDto);

    PageResult<SystemApiLogRespVo> convertPage(PageResult<SystemApiLog> pageResult);
}
