package top.sheepyu.module.system.service.job;

import top.sheepyu.framework.mybatisplus.core.query.IServiceX;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.system.controller.admin.job.vo.SystemJobLogQueryVo;
import top.sheepyu.module.system.dao.job.SystemJobLog;

import javax.validation.Valid;

/**
 * @author ygq
 * @date 2023-01-16 17:24
 **/
public interface SystemJobLogService extends IServiceX<SystemJobLog> {
    PageResult<SystemJobLog> page(@Valid SystemJobLogQueryVo queryVo);
}
