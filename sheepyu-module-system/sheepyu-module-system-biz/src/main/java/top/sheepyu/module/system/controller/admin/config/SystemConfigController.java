package top.sheepyu.module.system.controller.admin.config;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import top.sheepyu.framework.security.core.annotations.Permit;
import top.sheepyu.framework.web.core.annotations.FlowLimit;
import top.sheepyu.module.common.common.Result;
import top.sheepyu.module.system.controller.admin.config.vo.SystemConfigCreateVo;
import top.sheepyu.module.system.controller.admin.config.vo.SystemConfigRespVo;
import top.sheepyu.module.system.controller.admin.config.vo.SystemConfigUpdateVo;
import top.sheepyu.module.system.dao.config.SystemConfig;
import top.sheepyu.module.system.enums.config.SystemConfigKeyEnum;
import top.sheepyu.module.system.service.config.SystemConfigService;

import javax.annotation.Resource;
import java.util.List;

import static top.sheepyu.module.common.common.Result.success;
import static top.sheepyu.module.system.convert.config.SystemConfigConvert.CONVERT;

/**
 * @author ygq
 * @date 2023-01-21 15:26
 **/
@RestController
@RequestMapping("/system/config")
@Api(tags = "管理端 - 系统配置")
public class SystemConfigController {
    @Resource
    private SystemConfigService systemConfigService;

    @PostMapping
    @ApiOperation("添加系统配置")
    @PreAuthorize("@ss.hasPermission('system:config:create')")
    public Result<Boolean> create(@RequestBody SystemConfigCreateVo createVo) {
        systemConfigService.createConfig(createVo);
        return success(true);
    }

    @PutMapping
    @ApiOperation("修改系统配置")
    @PreAuthorize("@ss.hasPermission('system:config:update')")
    public Result<Boolean> update(@RequestBody SystemConfigUpdateVo updateVo) {
        systemConfigService.updateConfig(updateVo);
        return success(true);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除系统配置")
    @PreAuthorize("@ss.hasPermission('system:config:delete')")
    public Result<Boolean> delete(@PathVariable Long id) {
        systemConfigService.deleteConfig(id);
        return success(true);
    }

    @GetMapping
    @ApiOperation("获取系统配置列表")
    @PreAuthorize("@ss.hasPermission('system:config:query')")
    public Result<List<SystemConfigRespVo>> list(@RequestParam(required = false) String keyword) {
        List<SystemConfig> result = systemConfigService.listConfig(keyword);
        return success(CONVERT.convertList(result));
    }

    @GetMapping("/{id}")
    @ApiOperation("获取指定系统配置")
    @PreAuthorize("@ss.hasPermission('system:config:query')")
    public Result<SystemConfigRespVo> findById(@PathVariable Long id) {
        SystemConfig config = systemConfigService.findById(id);
        return success(CONVERT.convert(config));
    }

    @FlowLimit
    @Permit
    @GetMapping("/by-key/{key}")
    @ApiOperation("根据指定key获取系统配置")
    public Result<Object> findByKey(@PathVariable String key) {
        Object obj = systemConfigService.get(SystemConfigKeyEnum.value(key));
        return success(obj);
    }
}
