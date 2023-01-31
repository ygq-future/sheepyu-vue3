package top.sheepyu.module.system.controller.admin.role;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.common.common.Result;
import top.sheepyu.module.system.controller.admin.role.vo.SystemRoleCreateVo;
import top.sheepyu.module.system.controller.admin.role.vo.SystemRoleQueryVo;
import top.sheepyu.module.system.controller.admin.role.vo.SystemRoleRespVo;
import top.sheepyu.module.system.controller.admin.role.vo.SystemRoleUpdateVo;
import top.sheepyu.module.system.dao.role.SystemRole;
import top.sheepyu.module.system.service.role.SystemRoleService;

import javax.annotation.Resource;
import java.util.List;

import static top.sheepyu.module.common.common.Result.success;
import static top.sheepyu.module.system.convert.role.SystemRoleConvert.CONVERT;

/**
 * @author ygq
 * @date 2023-01-14 15:58
 **/
@RestController
@RequestMapping("/system/role")
@Slf4j
@Api(tags = "管理端 - 系统角色")
public class SystemRoleController {
    @Resource
    private SystemRoleService systemRoleService;

    @PostMapping
    @ApiOperation("添加系统角色")
    @PreAuthorize("@ss.hasPermission('system:role:create')")
    public Result<Boolean> create(@RequestBody SystemRoleCreateVo createVo) {
        systemRoleService.createRole(createVo);
        return success(true);
    }

    @PutMapping
    @ApiOperation("修改系统角色")
    @PreAuthorize("@ss.hasPermission('system:role:update')")
    public Result<Boolean> update(@RequestBody SystemRoleUpdateVo updateVo) {
        systemRoleService.updateRole(updateVo);
        return success(true);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除系统角色")
    @PreAuthorize("@ss.hasPermission('system:role:delete')")
    public Result<Boolean> delete(@PathVariable Long id) {
        systemRoleService.deleteRole(id);
        return success(true);
    }

    @GetMapping("/page")
    @ApiOperation("获取系统角色分页")
    @PreAuthorize("@ss.hasPermission('system:role:query')")
    public Result<PageResult<SystemRoleRespVo>> list(SystemRoleQueryVo queryVo) {
        PageResult<SystemRole> result = systemRoleService.pageRole(queryVo);
        return success(CONVERT.convertPage(result));
    }

    @GetMapping
    @ApiOperation("获取系统角色列表")
    @PreAuthorize("@ss.hasPermission('system:role:query')")
    public Result<List<SystemRoleRespVo>> list() {
        return success(CONVERT.convertList(systemRoleService.listRole()));
    }

    @GetMapping("/{id}")
    @ApiOperation("获取指定系统角色")
    @PreAuthorize("@ss.hasPermission('system:role:query')")
    public Result<SystemRoleRespVo> findById(@PathVariable Long id) {
        SystemRole role = systemRoleService.findById(id);
        return success(CONVERT.convert(role));
    }
}
