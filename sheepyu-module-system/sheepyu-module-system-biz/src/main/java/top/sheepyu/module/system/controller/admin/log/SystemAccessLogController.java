package top.sheepyu.module.system.controller.admin.log;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.common.common.Result;
import top.sheepyu.module.system.controller.admin.log.access.SystemAccessLogQueryVo;
import top.sheepyu.module.system.controller.admin.log.access.SystemAccessLogRespVo;
import top.sheepyu.module.system.dao.log.SystemAccessLog;
import top.sheepyu.module.system.service.log.SystemAccessLogService;

import javax.annotation.Resource;

import static top.sheepyu.module.system.convert.log.SystemAccessLogConvert.CONVERT;

/**
 * @author ygq
 * @date 2023-01-23 20:03
 **/
@RestController
@RequestMapping("/system/access-log")
@Api(tags = "管理端 - 系统访问日志")
public class SystemAccessLogController {
    @Resource
    private SystemAccessLogService systemAccessLogService;

    @GetMapping("/page")
    @ApiOperation("系统访问日志分页")
    @PreAuthorize("@ss.hasPermission('system:access-log:query')")
    public Result<PageResult<SystemAccessLogRespVo>> page(SystemAccessLogQueryVo queryVo) {
        PageResult<SystemAccessLog> pageResult = systemAccessLogService.pageAccessLog(queryVo);
        return Result.success(CONVERT.convertPage(pageResult));
    }
}
