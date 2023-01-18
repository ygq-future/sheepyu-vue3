package top.sheepyu.module.system.controller.admin.job;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.quartz.SchedulerException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.common.common.Result;
import top.sheepyu.module.system.controller.admin.job.vo.SystemJobCreateVo;
import top.sheepyu.module.system.controller.admin.job.vo.SystemJobQueryVo;
import top.sheepyu.module.system.controller.admin.job.vo.SystemJobRespVo;
import top.sheepyu.module.system.controller.admin.job.vo.SystemJobUpdateVo;
import top.sheepyu.module.system.dao.job.SystemJob;
import top.sheepyu.module.system.service.job.SystemJobService;

import javax.annotation.Resource;

import static top.sheepyu.module.common.common.Result.success;
import static top.sheepyu.module.system.convert.job.SystemJobConvert.CONVERT;

/**
 * @author ygq
 * @date 2023-01-16 18:01
 **/
@RestController
@RequestMapping("/job")
@Validated
@Api(tags = "系统定时任务")
public class SystemJobController {
    @Resource
    private SystemJobService systemJobService;

    @PostMapping
    @ApiOperation("创建定时任务")
    @PreAuthorize("@ss.hasPermission('system:job:create')")
    public Result<Boolean> create(@RequestBody SystemJobCreateVo createVo) throws SchedulerException {
        systemJobService.createJob(createVo);
        return success(true);
    }

    @PutMapping
    @ApiOperation("修改定时任务")
    @PreAuthorize("@ss.hasPermission('system:job:update')")
    public Result<Boolean> update(@RequestBody SystemJobUpdateVo updateVo) throws SchedulerException {
        systemJobService.updateJob(updateVo);
        return success(true);
    }

    @DeleteMapping("/{ids}")
    @ApiOperation("删除定时任务")
    @PreAuthorize("@ss.hasPermission('system:job:delete')")
    public Result<Boolean> delete(@PathVariable String ids) throws SchedulerException {
        systemJobService.deleteJobs(ids);
        return success(true);
    }

    @PatchMapping("/updateJobStatus/{id}")
    @ApiOperation("修改定时任务状态")
    @PreAuthorize("@ss.hasPermission('system:job:update')")
    public Result<Boolean> updateJobStatus(@PathVariable Long id) throws SchedulerException {
        systemJobService.updateJobStatus(id);
        return success(true);
    }

    @PatchMapping("/execute/{id}")
    @ApiOperation("执行一次定时任务")
    @PreAuthorize("@ss.hasPermission('system:job:update')")
    public Result<Boolean> execute(@PathVariable Long id) throws SchedulerException {
        systemJobService.execute(id);
        return success(true);
    }

    @GetMapping("/{id}")
    @ApiOperation("获取指定定时任务")
    @PreAuthorize("@ss.hasPermission('system:job:query')")
    public Result<SystemJobRespVo> queryById(@PathVariable Long id) {
        SystemJob job = systemJobService.findJobById(id);
        return success(CONVERT.convert(job));
    }

    @GetMapping("/page")
    @ApiOperation("获取定时任务分页")
    @PreAuthorize("@ss.hasPermission('system:job:query')")
    public Result<PageResult<SystemJobRespVo>> queryById(SystemJobQueryVo queryVo) {
        PageResult<SystemJob> pageData = systemJobService.findJobPage(queryVo);
        return success(CONVERT.convertPage(pageData));
    }
}
