package top.sheepyu.module.system.service.joblog;

import top.sheepyu.framework.mybatisplus.core.query.IServiceX;
import top.sheepyu.module.system.dao.joblog.SystemJobLog;

import java.util.List;

/**
 * @author ygq
 * @date 2023-01-16 17:24
 **/
public interface SystemJobLogService extends IServiceX<SystemJobLog> {
    List<SystemJobLog> findByJobId(Long id);
}
