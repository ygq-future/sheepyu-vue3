package top.sheepyu.module.system.controller.admin.log;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.common.common.Result;
import top.sheepyu.module.system.controller.admin.log.access.SystemAccessLogQueryVo;
import top.sheepyu.module.system.controller.admin.log.access.SystemAccessLogRespVo;
import top.sheepyu.module.system.controller.admin.log.api.SystemApiLogQueryVo;
import top.sheepyu.module.system.controller.admin.log.api.SystemApiLogRespVo;
import top.sheepyu.module.system.convert.log.SystemApiLogConvert;
import top.sheepyu.module.system.dao.log.SystemAccessLog;
import top.sheepyu.module.system.dao.log.SystemApiLog;
import top.sheepyu.module.system.service.log.SystemAccessLogService;
import top.sheepyu.module.system.service.log.SystemApiLogService;

import static top.sheepyu.module.system.convert.log.SystemAccessLogConvert.CONVERT;

/**
 * @author ygq
 * @date 2023-02-13 09:56
 **/
@RestController
@RequestMapping("/system/log")
@Api(tags = "管理端 - 系统日志")
@AllArgsConstructor
public class SystemLogController {
    private final SystemAccessLogService systemAccessLogService;
    private final SystemApiLogService systemApiLogService;

    @GetMapping("/access/page")
    @ApiOperation("系统访问日志分页")
    @PreAuthorize("@ss.hasPermission('system:log-access:query')")
    public Result<PageResult<SystemAccessLogRespVo>> page(SystemAccessLogQueryVo queryVo) {
        PageResult<SystemAccessLog> pageResult = systemAccessLogService.pageAccessLog(queryVo);
        return Result.success(CONVERT.convertPage(pageResult));
    }

    @GetMapping("/api/page")
    @ApiOperation("系统api日志分页")
    @PreAuthorize("@ss.hasPermission('system:log-api:query')")
    public Result<PageResult<SystemApiLogRespVo>> page(SystemApiLogQueryVo queryVo) {
        PageResult<SystemApiLog> pageResult = systemApiLogService.pageApiLog(queryVo);
        return Result.success(SystemApiLogConvert.CONVERT.convertPage(pageResult));
    }

    @PatchMapping("/api/process/{id}")
    @ApiOperation("修改api日志的处理状态")
    @PreAuthorize("@ss.hasPermission('system:log-api:update')")
    public Result<Boolean> process(@PathVariable Long id) {
        systemApiLogService.process(id);
        return Result.success(true);
    }
}
