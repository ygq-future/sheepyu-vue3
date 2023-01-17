package top.sheepyu.module.system.convert.job;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.system.controller.admin.job.vo.SystemJobCreateVo;
import top.sheepyu.module.system.controller.admin.job.vo.SystemJobRespVo;
import top.sheepyu.module.system.controller.admin.job.vo.SystemJobUpdateVo;
import top.sheepyu.module.system.dao.job.SystemJob;

/**
 * @author ygq
 * @date 2023-01-16 18:00
 **/
@Mapper
public interface SystemJobConvert {
    SystemJobConvert INSTANCE = Mappers.getMapper(SystemJobConvert.class);

    SystemJob convert(SystemJobCreateVo createVo);

    SystemJob convert(SystemJobUpdateVo updateVo);

    SystemJobRespVo convert(SystemJob job);

    PageResult<SystemJobRespVo> convertPage(PageResult<SystemJob> pageResult);
}
