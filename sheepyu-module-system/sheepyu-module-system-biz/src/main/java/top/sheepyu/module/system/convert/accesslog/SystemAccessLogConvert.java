package top.sheepyu.module.system.convert.accesslog;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.system.controller.admin.accesslog.vo.SystemAccessLogRespVo;
import top.sheepyu.module.system.dao.accesslog.SystemAccessLog;

/**
 * @author ygq
 * @date 2023-01-23 20:13
 **/
@Mapper
public interface SystemAccessLogConvert {
    SystemAccessLogConvert CONVERT = Mappers.getMapper(SystemAccessLogConvert.class);

    PageResult<SystemAccessLogRespVo> convertPage(PageResult<SystemAccessLog> pageResult);
}
