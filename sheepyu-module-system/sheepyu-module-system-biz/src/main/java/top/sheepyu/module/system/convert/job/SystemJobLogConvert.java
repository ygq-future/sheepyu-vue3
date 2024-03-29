package top.sheepyu.module.system.convert.job;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.system.controller.admin.job.vo.SystemJobLogRespVo;
import top.sheepyu.module.system.dao.job.SystemJobLog;

import java.util.List;

/**
 * @author ygq
 * @date 2023-01-23 20:51
 **/
@Mapper
public interface SystemJobLogConvert {
    SystemJobLogConvert CONVERT = Mappers.getMapper(SystemJobLogConvert.class);

    PageResult<SystemJobLogRespVo> convertPage(PageResult<SystemJobLog> list);
}
