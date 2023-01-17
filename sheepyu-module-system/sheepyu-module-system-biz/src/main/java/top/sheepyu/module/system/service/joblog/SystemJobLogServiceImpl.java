package top.sheepyu.module.system.service.joblog;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.framework.job.service.JobLogFrameworkService;
import top.sheepyu.framework.mybatisplus.core.query.ServiceImplX;
import top.sheepyu.module.system.dao.joblog.SystemJobLog;
import top.sheepyu.module.system.dao.joblog.SystemJobLogMapper;

import java.time.LocalDateTime;

import static top.sheepyu.module.common.enums.CommonStatusEnum.FAILED;
import static top.sheepyu.module.common.enums.CommonStatusEnum.SUCCESS;
import static top.sheepyu.module.system.constants.ErrorCodeConstants.LOG_NOT_EXISTS;

/**
 * @author ygq
 * @date 2023-01-16 17:25
 **/
@Service
@Slf4j
@Validated
public class SystemJobLogServiceImpl extends ServiceImplX<SystemJobLogMapper, SystemJobLog> implements SystemJobLogService, JobLogFrameworkService {
    @Override
    public Long createJobLog(Long jobId, LocalDateTime beginTime, String jobHandlerName, String jobHandlerParam, Integer executeIndex) {
        SystemJobLog jobLog = new SystemJobLog();
        jobLog.setJobId(jobId).setBeginTime(beginTime).setHandlerName(jobHandlerName).setHandlerParam(jobHandlerParam).setRetryCount(executeIndex);
        save(jobLog);
        return jobLog.getId();
    }

    @Async
    @Override
    public void updateJobLogResultAsync(Long logId, LocalDateTime endTime, Integer duration, boolean success, String result) {
        SystemJobLog jobLog = this.findByIdValidateExists(logId);
        jobLog.setEndTime(endTime).setDuration(duration).setResult(result);
        jobLog.setStatus(success ? SUCCESS.getCode() : FAILED.getCode());
        updateById(jobLog);
    }

    private SystemJobLog findByIdValidateExists(Long id) {
        return findByIdValidateExists(id, LOG_NOT_EXISTS);
    }
}
