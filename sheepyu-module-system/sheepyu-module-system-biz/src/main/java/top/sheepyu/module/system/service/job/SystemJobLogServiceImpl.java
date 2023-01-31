package top.sheepyu.module.system.service.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.framework.job.service.JobLogFrameworkService;
import top.sheepyu.framework.mybatisplus.core.query.ServiceImplX;
import top.sheepyu.module.system.dao.job.SystemJobLog;
import top.sheepyu.module.system.dao.job.SystemJobLogMapper;

import java.util.Date;
import java.util.List;

import static top.sheepyu.module.system.constants.ErrorCodeConstants.LOG_NOT_EXISTS;
import static top.sheepyu.module.system.enums.job.JobLogStatusEnum.FAILED;
import static top.sheepyu.module.system.enums.job.JobLogStatusEnum.SUCCESS;

/**
 * @author ygq
 * @date 2023-01-16 17:25
 **/
@Service
@Slf4j
@Validated
public class SystemJobLogServiceImpl extends ServiceImplX<SystemJobLogMapper, SystemJobLog> implements SystemJobLogService, JobLogFrameworkService {
    @Override
    public Long createJobLog(Long jobId, Date beginTime, String jobHandlerName, String jobHandlerParam, Integer executeIndex) {
        SystemJobLog jobLog = new SystemJobLog();
        jobLog.setJobId(jobId).setBeginTime(beginTime).setHandlerName(jobHandlerName).setHandlerParam(jobHandlerParam).setRetryCount(executeIndex);
        save(jobLog);
        return jobLog.getId();
    }

    @Async
    @Override
    public void updateJobLogResultAsync(Long logId, Date endTime, Integer duration, boolean success, String result) {
        SystemJobLog jobLog = this.findByIdValidateExists(logId);
        jobLog.setEndTime(endTime).setDuration(duration).setResult(result);
        jobLog.setStatus(success ? SUCCESS.getCode() : FAILED.getCode());
        updateById(jobLog);
    }

    private SystemJobLog findByIdValidateExists(Long id) {
        return findByIdValidateExists(id, LOG_NOT_EXISTS);
    }

    @Override
    public List<SystemJobLog> findByJobId(Long id) {
        return lambdaQuery().eq(SystemJobLog::getJobId, id).list();
    }
}
