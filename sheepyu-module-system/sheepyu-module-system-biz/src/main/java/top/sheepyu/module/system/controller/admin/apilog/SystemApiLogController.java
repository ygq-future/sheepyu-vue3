package top.sheepyu.module.system.controller.admin.apilog;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.common.common.Result;
import top.sheepyu.module.system.controller.admin.apilog.vo.SystemApiLogQueryVo;
import top.sheepyu.module.system.controller.admin.apilog.vo.SystemApiLogRespVo;
import top.sheepyu.module.system.dao.apilog.SystemApiLog;
import top.sheepyu.module.system.service.apilog.SystemApiLogService;

import javax.annotation.Resource;

import static top.sheepyu.module.system.convert.apilog.SystemApiLogConvert.CONVERT;

/**
 * @author ygq
 * @date 2023-01-23 20:03
 **/
@RestController
@RequestMapping("/system/api-log")
@Api(tags = "管理端 - 系统api日志")
public class SystemApiLogController {
    @Resource
    private SystemApiLogService systemApiLogService;

    @GetMapping("/page")
    @ApiOperation("系统api日志分页")
    @PreAuthorize("@ss.hasPermission('system:api-log:query')")
    public Result<PageResult<SystemApiLogRespVo>> page(SystemApiLogQueryVo queryVo) {
        PageResult<SystemApiLog> pageResult = systemApiLogService.pageApiLog(queryVo);
        return Result.success(CONVERT.convertPage(pageResult));
    }

    @PatchMapping("/process/{id}")
    @ApiOperation("修改api日志的处理状态")
    @PreAuthorize("@ss.hasPermission('system:api-log:update')")
    public Result<Boolean> process(@PathVariable Long id) {
        systemApiLogService.process(id);
        return Result.success(true);
    }
}
