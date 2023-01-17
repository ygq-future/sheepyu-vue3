package top.sheepyu.framework.job.core;

import org.quartz.*;
import top.sheepyu.framework.job.core.handler.JobHandlerInvoke;

import javax.annotation.Resource;

import static top.sheepyu.framework.job.core.enums.JobDataKeyEnum.*;

/**
 * @author ygq
 * @date 2022-12-01 18:55
 **/
public class SchedulerManager {
    @Resource
    private Scheduler scheduler;

    public void add(Long jobId, String handlerName, String handlerParam, Integer retryCount, Integer retryInterval, String cron) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(JobHandlerInvoke.class)
                .usingJobData(JOB_ID.name(), jobId)
                .usingJobData(JOB_HANDLER_NAME.name(), handlerName)
                .withIdentity(handlerName)
                .build();
        scheduler.scheduleJob(jobDetail, this.buildTrigger(handlerName, handlerParam, retryCount, retryInterval, cron));
    }

    public void update(String handlerName, String handlerParam, Integer retryCount, Integer retryInterval, String cron) throws SchedulerException {
        Trigger trigger = this.buildTrigger(handlerName, handlerParam, retryCount, retryInterval, cron);
        scheduler.rescheduleJob(TriggerKey.triggerKey(handlerName), trigger);
    }

    public void delete(String handlerName) throws SchedulerException {
        scheduler.deleteJob(JobKey.jobKey(handlerName));
    }

    public void pause(String handlerName) throws SchedulerException {
        scheduler.pauseJob(JobKey.jobKey(handlerName));
        scheduler.pauseTrigger(TriggerKey.triggerKey(handlerName));
    }

    public void resume(String handlerName) throws SchedulerException {
        scheduler.resumeJob(JobKey.jobKey(handlerName));
        scheduler.resumeTrigger(TriggerKey.triggerKey(handlerName));
    }

    public void execute(Long jobId, String handlerName, String handlerParam) throws SchedulerException {
        JobDataMap data = new JobDataMap();
        data.put(JOB_HANDLER_PARAM.name(), handlerParam);
        data.put(JOB_ID.name(), jobId);
        scheduler.triggerJob(JobKey.jobKey(handlerName), data);
    }

    private Trigger buildTrigger(String handlerName, String handlerParam, Integer retryCount, Integer retryInterval, String cron) {
        return TriggerBuilder.newTrigger()
                .usingJobData(JOB_HANDLER_PARAM.name(), handlerParam)
                .usingJobData(JOB_RETRY_COUNT.name(), retryCount)
                .usingJobData(JOB_RETRY_INTERVAL.name(), retryInterval)
                .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                .withIdentity(handlerName)
                .build();
    }
}
