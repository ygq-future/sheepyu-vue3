package top.sheepyu.module.system.service.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.framework.job.service.JobLogFrameworkService;
import top.sheepyu.framework.mybatisplus.core.query.ServiceImplX;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.system.controller.admin.job.vo.SystemJobLogQueryVo;
import top.sheepyu.module.system.dao.job.SystemJobLog;
import top.sheepyu.module.system.dao.job.SystemJobLogMapper;

import java.util.Date;

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
    public Long createJobLog(Long jobId, Date beginTime, String jobHandlerName, String jobHandlerParam, Integer refireCount) {
        SystemJobLog jobLog = new SystemJobLog();
        jobLog.setJobId(jobId).setBeginTime(beginTime).setHandlerName(jobHandlerName).setHandlerParam(jobHandlerParam).setRetryCount(refireCount);
        save(jobLog);
        return jobLog.getId();
    }

    @Async
    @Override
    public void updateJobLogResultAsync(Long logId, Date endTime, Integer duration, boolean success, String result) {
        SystemJobLog jobLog = this.findByIdThrowIfNotExists(logId);
        jobLog.setEndTime(endTime).setDuration(duration).setResult(result);
        jobLog.setStatus(success ? SUCCESS.getCode() : FAILED.getCode());
        updateById(jobLog);
    }

    private SystemJobLog findByIdThrowIfNotExists(Long id) {
        return findByIdThrowIfNotExists(id, LOG_NOT_EXISTS);
    }

    @Override
    public PageResult<SystemJobLog> page(SystemJobLogQueryVo queryVo) {
        return page(queryVo, buildQuery()
                .eqIfPresent(SystemJobLog::getJobId, queryVo.getJobId())
                .betweenIfPresent(SystemJobLog::getDuration, queryVo.getDurations())
                .eqIfPresent(SystemJobLog::getStatus, queryVo.getStatus())
                .orderByDesc(SystemJobLog::getCreateTime));
    }
}
