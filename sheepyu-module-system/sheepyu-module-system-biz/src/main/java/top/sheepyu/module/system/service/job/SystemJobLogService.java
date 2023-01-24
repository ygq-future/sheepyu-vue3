package top.sheepyu.module.system.service.job;

import top.sheepyu.framework.mybatisplus.core.query.IServiceX;
import top.sheepyu.module.system.dao.job.SystemJobLog;

import java.util.List;

/**
 * @author ygq
 * @date 2023-01-16 17:24
 **/
public interface SystemJobLogService extends IServiceX<SystemJobLog> {
    List<SystemJobLog> findByJobId(Long id);
}
