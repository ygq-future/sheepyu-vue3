package top.sheepyu.module.system.service.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.framework.job.config.SchedulerManager;
import top.sheepyu.framework.job.util.CronUtils;
import top.sheepyu.framework.mybatisplus.core.query.ServiceImplX;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.common.util.MyStrUtil;
import top.sheepyu.module.system.controller.admin.job.vo.SystemJobCreateVo;
import top.sheepyu.module.system.controller.admin.job.vo.SystemJobQueryVo;
import top.sheepyu.module.system.controller.admin.job.vo.SystemJobUpdateVo;
import top.sheepyu.module.system.dao.job.SystemJob;
import top.sheepyu.module.system.dao.job.SystemJobMapper;
import top.sheepyu.module.system.enums.job.JobStatusEnum;

import javax.annotation.Resource;
import java.util.List;

import static top.sheepyu.module.common.exception.CommonException.exception;
import static top.sheepyu.module.system.constants.ErrorCodeConstants.*;
import static top.sheepyu.module.system.convert.job.SystemJobConvert.CONVERT;
import static top.sheepyu.module.system.enums.job.JobStatusEnum.*;

/**
 * @author ygq
 * @date 2023-01-16 17:54
 **/
@Service
@Slf4j
@Validated
public class SystemJobServiceImpl extends ServiceImplX<SystemJobMapper, SystemJob> implements SystemJobService {
    @Resource
    private SchedulerManager schedulerManager;

    @Transactional
    @Override
    public void createJob(SystemJobCreateVo createVo) throws SchedulerException {
        //校验cron表达式
        validateCronExpression(createVo.getCron());
        SystemJob job = CONVERT.convert(createVo);
        //检查处理器名字是否重复
        checkRepeatByFieldThrow(job, JOB_HANDLER_EXISTS, SystemJob::getHandlerParam);

        job.setStatus(INIT.getCode());
        save(job);

        //将任务添加到quartz中
        schedulerManager.add(job.getId(), job.getHandlerName(), job.getHandlerParam(), job.getRetryCount(), job.getRetryInterval(), job.getCron());
        job.setStatus(NORMAL.getCode());
        updateById(job);
    }

    @Transactional
    @Override
    public void updateJob(SystemJobUpdateVo updateVo) throws SchedulerException {
        validateCronExpression(updateVo.getCron());
        SystemJob job = findByIdValidateExists(updateVo.getId());
        String handlerName = job.getHandlerName();

        //只有开启状态下才能修改, 因为如果是暂停修改后又会开启
        if (!job.getStatus().equals(NORMAL.getCode())) {
            throw exception(JOB_UPDATE_ONLY_NORMAL_STATUS);
        }

        job = CONVERT.convert(updateVo);
        updateById(job);

        schedulerManager.update(handlerName, job.getHandlerParam(), job.getRetryCount(), job.getRetryInterval(), job.getCron());
    }

    @Transactional
    @Override
    public void deleteJobs(String ids) throws SchedulerException {
        List<Long> idList = MyStrUtil.splitToLong(ids, ',');
        for (Long id : idList) {
            schedulerManager.delete(findByIdValidateExists(id).getHandlerName());
        }
        batchDelete(ids, SystemJob::getId);
    }

    @Override
    public void updateJobStatus(Long id) throws SchedulerException {
        SystemJob job = findByIdValidateExists(id);
        JobStatusEnum currentStatus = JobStatusEnum.valueOf(job.getStatus());

        if (currentStatus.equals(NORMAL)) {
            job.setStatus(STOP.getCode());
            schedulerManager.pause(job.getHandlerName());
        } else if (currentStatus.equals(STOP)) {
            job.setStatus(NORMAL.getCode());
            schedulerManager.resume(job.getHandlerName());
        }

        updateById(job);
    }

    @Override
    public void execute(Long id) throws SchedulerException {
        SystemJob job = findByIdValidateExists(id);
        schedulerManager.execute(job.getId(), job.getHandlerName(), job.getHandlerParam());
    }

    @Override
    public SystemJob findJobById(Long id) {
        return findByIdValidateExists(id);
    }

    @Override
    public PageResult<SystemJob> findJobPage(SystemJobQueryVo queryVo) {
        return page(queryVo, buildQuery()
                .likeIfPresent(SystemJob::getName, queryVo.getKeyword())
                .eqIfPresent(SystemJob::getStatus, queryVo.getStatus()));
    }

    private void validateCronExpression(String cron) {
        if (!CronUtils.isValid(cron)) {
            throw exception(CRON_ERROR);
        }
    }

    private SystemJob findByIdValidateExists(Long id) {
        return findByIdValidateExists(id, JOB_HANDLER_NOT_EXISTS);
    }
}
