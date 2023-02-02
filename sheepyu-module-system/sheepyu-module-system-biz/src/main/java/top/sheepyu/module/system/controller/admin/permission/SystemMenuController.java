package top.sheepyu.module.system.controller.admin.permission;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import top.sheepyu.module.common.common.Result;
import top.sheepyu.module.system.controller.admin.permission.menu.SystemMenuCreateVo;
import top.sheepyu.module.system.controller.admin.permission.menu.SystemMenuQueryVo;
import top.sheepyu.module.system.controller.admin.permission.menu.SystemMenuRespVo;
import top.sheepyu.module.system.controller.admin.permission.menu.SystemMenuUpdateVo;
import top.sheepyu.module.system.dao.permission.menu.SystemMenu;
import top.sheepyu.module.system.service.permission.PermissionBiz;
import top.sheepyu.module.system.service.permission.SystemMenuService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

import static top.sheepyu.module.common.common.Result.success;
import static top.sheepyu.module.system.convert.permission.SystemMenuConvert.CONVERT;

/**
 * @author ygq
 * @date 2023-01-14 15:58
 **/
@RestController
@RequestMapping("/system/menu")
@Slf4j
@Api(tags = "管理端 - 系统菜单")
public class SystemMenuController {
    @Resource
    private SystemMenuService systemMenuService;
    @Resource
    private PermissionBiz permissionBiz;

    @PostMapping
    @ApiOperation("添加系统菜单")
    @PreAuthorize("@ss.hasPermission('system:menu:create')")
    public Result<Boolean> create(@RequestBody SystemMenuCreateVo createVo) {
        systemMenuService.createMenu(createVo);
        return success(true);
    }

    @PutMapping
    @ApiOperation("修改系统菜单")
    @PreAuthorize("@ss.hasPermission('system:menu:update')")
    public Result<Boolean> update(@RequestBody SystemMenuUpdateVo updateVo) {
        systemMenuService.updateMenu(updateVo);
        return success(true);
    }

    @PutMapping("/assign/{roleId}")
    @ApiOperation("修改角色的菜单")
    @PreAuthorize("@ss.hasPermission('system:menu:assign')")
    public Result<Boolean> assignMenu(@PathVariable Long roleId, @RequestBody Set<Long> menuIds) {
        permissionBiz.assignMenu(roleId, menuIds);
        return success(true);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除系统菜单")
    @PreAuthorize("@ss.hasPermission('system:menu:delete')")
    public Result<Boolean> delete(@PathVariable Long id) {
        systemMenuService.deleteMenu(id);
        return success(true);
    }

    @GetMapping
    @ApiOperation("获取系统菜单列表")
    @PreAuthorize("@ss.hasPermission('system:menu:query')")
    public Result<List<SystemMenuRespVo>> list(SystemMenuQueryVo queryVo) {
        List<SystemMenu> list = systemMenuService.listMenu(queryVo);
        return success(CONVERT.convertList(list));
    }

    @GetMapping("/role/{roleId}")
    @ApiOperation("获取角色的菜单id")
    @PreAuthorize("@ss.hasPermission('system:menu:query')")
    public Result<Set<Long>> listMenuByRoleId(@PathVariable Long roleId) {
        Set<Long> list = permissionBiz.listMenuIdByRoleId(roleId);
        return success(list);
    }

    @GetMapping("/{id}")
    @ApiOperation("获取指定系统菜单")
    @PreAuthorize("@ss.hasPermission('system:menu:query')")
    public Result<SystemMenuRespVo> findById(@PathVariable Long id) {
        SystemMenu menu = systemMenuService.findById(id);
        return success(CONVERT.convert(menu));
    }
}
