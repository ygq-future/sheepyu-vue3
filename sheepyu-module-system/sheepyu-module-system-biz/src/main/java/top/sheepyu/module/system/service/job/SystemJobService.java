package top.sheepyu.module.system.service.job;

import com.baomidou.mybatisplus.extension.service.IService;
import org.quartz.SchedulerException;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.system.controller.admin.job.vo.SystemJobCreateVo;
import top.sheepyu.module.system.controller.admin.job.vo.SystemJobQueryVo;
import top.sheepyu.module.system.controller.admin.job.vo.SystemJobUpdateVo;
import top.sheepyu.module.system.dao.job.SystemJob;

import javax.validation.Valid;

/**
 * @author ygq
 * @date 2023-01-16 17:54
 **/
public interface SystemJobService extends IService<SystemJob> {

    void createJob(@Valid SystemJobCreateVo createVo) throws SchedulerException;

    void updateJob(SystemJobUpdateVo updateVo) throws SchedulerException;

    void deleteJobs(String ids) throws SchedulerException;

    void updateJobStatus(Long id) throws SchedulerException;

    void execute(Long id) throws SchedulerException;

    SystemJob findJobById(Long id);

    PageResult<SystemJob> findJobPage(SystemJobQueryVo queryVo);
}
