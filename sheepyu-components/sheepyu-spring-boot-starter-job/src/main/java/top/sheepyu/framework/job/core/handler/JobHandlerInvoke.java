package top.sheepyu.framework.job.core.handler;

import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import top.sheepyu.framework.job.core.enums.JobDataKeyEnum;
import top.sheepyu.framework.job.service.JobLogFrameworkService;

import javax.annotation.Resource;
import java.util.Date;

import static cn.hutool.core.exceptions.ExceptionUtil.getRootCauseMessage;

/**
 * @author ygq
 * @date 2022-12-01 19:11
 **/
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
@Slf4j
public class JobHandlerInvoke implements Job {
    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private JobLogFrameworkService jobLogFrameworkService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap data = context.getMergedJobDataMap();
        Long jobId = data.getLong(JobDataKeyEnum.JOB_ID.name());
        String jobHandlerName = data.getString(JobDataKeyEnum.JOB_HANDLER_NAME.name());
        String jobHandlerParam = data.getString(JobDataKeyEnum.JOB_HANDLER_PARAM.name());
        int retryCount = (int) data.getOrDefault(JobDataKeyEnum.JOB_RETRY_COUNT.name(), 0);
        int retryInterval = (int) data.getOrDefault(JobDataKeyEnum.JOB_RETRY_INTERVAL.name(), 0);
        int refireCount = context.getRefireCount();

        Long jogLogId = null;
        Date beginTime = new Date();
        String result = null;
        Throwable ex = null;
        try {
            //创建日志
            jogLogId = jobLogFrameworkService.createJobLog(jobId, beginTime, jobHandlerName, jobHandlerParam, refireCount + 1);
            result = this.execute(jobHandlerName, jobHandlerParam);
        } catch (Throwable e) {
            ex = e;
        }

        //异步记录任务执行结果
        this.updateJobLogResultAsync(jogLogId, jobHandlerName, beginTime, result, ex);
        handlerException(ex, refireCount, retryCount, retryInterval);
    }

    private String execute(String jobHandlerName, String jobHandlerParam) throws Exception {
        JobHandler jobHandler = applicationContext.getBean(jobHandlerName, JobHandler.class);
        Assert.notNull(jobHandler, String.format("没有找到对应的JobHandlerName: %s", jobHandlerName));
        return jobHandler.execute(jobHandlerParam);
    }

    private void updateJobLogResultAsync(Long jobLogId, String jobName, Date beginTime, String result, Throwable exception) {
        Date endTime = new Date();
        // 处理是否成功
        boolean success = exception == null;
        if (!success) {
            result = getRootCauseMessage(exception);
        }
        // 更新日志
        try {
            long start = beginTime.getTime();
            long end = endTime.getTime();
            jobLogFrameworkService.updateJobLogResultAsync(jobLogId, endTime, (int) (end - start), success, result);
        } catch (Exception ex) {
            log.error("[executeInternal][Job({}) logId({}) 记录执行日志失败({}/{})]", jobName, jobLogId, success, result);
        }
    }

    private void handlerException(Throwable ex, int refireCount, int retryCount, int retryInterval) throws JobExecutionException {
        if (ex == null) {
            return;
        }
        if (refireCount >= retryCount) {
            throw new JobExecutionException(ex);
        }
        if (retryInterval > 0) {
            ThreadUtil.sleep(retryInterval * 1000L);
        }
        throw new JobExecutionException(ex, true);
    }
}
