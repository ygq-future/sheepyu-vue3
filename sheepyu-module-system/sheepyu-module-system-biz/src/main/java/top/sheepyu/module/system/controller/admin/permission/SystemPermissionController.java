package top.sheepyu.module.system.controller.admin.permission;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.common.common.Result;
import top.sheepyu.module.system.controller.admin.permission.menu.SystemMenuCreateVo;
import top.sheepyu.module.system.controller.admin.permission.menu.SystemMenuQueryVo;
import top.sheepyu.module.system.controller.admin.permission.menu.SystemMenuRespVo;
import top.sheepyu.module.system.controller.admin.permission.menu.SystemMenuUpdateVo;
import top.sheepyu.module.system.controller.admin.permission.role.SystemRoleCreateVo;
import top.sheepyu.module.system.controller.admin.permission.role.SystemRoleQueryVo;
import top.sheepyu.module.system.controller.admin.permission.role.SystemRoleRespVo;
import top.sheepyu.module.system.controller.admin.permission.role.SystemRoleUpdateVo;
import top.sheepyu.module.system.convert.permission.SystemRoleConvert;
import top.sheepyu.module.system.dao.permission.menu.SystemMenu;
import top.sheepyu.module.system.dao.permission.role.SystemRole;
import top.sheepyu.module.system.service.permission.PermissionBiz;
import top.sheepyu.module.system.service.permission.SystemMenuService;
import top.sheepyu.module.system.service.permission.SystemRoleService;

import java.util.List;
import java.util.Set;

import static top.sheepyu.module.common.common.Result.success;
import static top.sheepyu.module.system.convert.permission.SystemMenuConvert.CONVERT;

/**
 * @author ygq
 * @date 2023-02-13 09:46
 **/
@RestController
@RequestMapping("/system/permission")
@Slf4j
@Api(tags = "管理端 - 权限控制")
@AllArgsConstructor
public class SystemPermissionController {
    private final PermissionBiz permissionBiz;
    private final SystemRoleService systemRoleService;
    private final SystemMenuService systemMenuService;

    @PostMapping("/menu")
    @ApiOperation("添加系统菜单")
    @PreAuthorize("@ss.hasPermission('system:menu:create')")
    public Result<Boolean> createMenu(@RequestBody SystemMenuCreateVo createVo) {
        systemMenuService.createMenu(createVo);
        return success(true);
    }

    @PutMapping("/menu")
    @ApiOperation("修改系统菜单")
    @PreAuthorize("@ss.hasPermission('system:menu:update')")
    public Result<Boolean> updateMenu(@RequestBody SystemMenuUpdateVo updateVo) {
        systemMenuService.updateMenu(updateVo);
        return success(true);
    }

    @DeleteMapping("/menu/{id}")
    @ApiOperation("删除系统菜单")
    @PreAuthorize("@ss.hasPermission('system:menu:delete')")
    public Result<Boolean> deleteMenu(@PathVariable Long id) {
        systemMenuService.deleteMenu(id);
        return success(true);
    }

    @GetMapping("/menu")
    @ApiOperation("获取系统菜单列表")
    @PreAuthorize("@ss.hasPermission('system:menu:query')")
    public Result<List<SystemMenuRespVo>> listMenu(SystemMenuQueryVo queryVo) {
        List<SystemMenu> list = systemMenuService.listMenu(queryVo);
        return success(CONVERT.convertList(list));
    }

    @GetMapping("/menu/{id}")
    @ApiOperation("获取指定系统菜单")
    @PreAuthorize("@ss.hasPermission('system:menu:query')")
    public Result<SystemMenuRespVo> findMenuById(@PathVariable Long id) {
        SystemMenu menu = systemMenuService.findById(id);
        return success(CONVERT.convert(menu));
    }

    @PostMapping("/role")
    @ApiOperation("添加系统角色")
    @PreAuthorize("@ss.hasPermission('system:role:create')")
    public Result<Boolean> createRole(@RequestBody SystemRoleCreateVo createVo) {
        systemRoleService.createRole(createVo);
        return success(true);
    }

    @PutMapping("/role")
    @ApiOperation("修改系统角色")
    @PreAuthorize("@ss.hasPermission('system:role:update')")
    public Result<Boolean> updateRole(@RequestBody SystemRoleUpdateVo updateVo) {
        systemRoleService.updateRole(updateVo);
        return success(true);
    }

    @DeleteMapping("/role/{id}")
    @ApiOperation("删除系统角色")
    @PreAuthorize("@ss.hasPermission('system:role:delete')")
    public Result<Boolean> deleteRole(@PathVariable Long id) {
        systemRoleService.deleteRole(id);
        return success(true);
    }

    @GetMapping("/role/page")
    @ApiOperation("获取系统角色分页")
    @PreAuthorize("@ss.hasPermission('system:role:query')")
    public Result<PageResult<SystemRoleRespVo>> pageRole(SystemRoleQueryVo queryVo) {
        PageResult<SystemRole> result = systemRoleService.pageRole(queryVo);
        return success(SystemRoleConvert.CONVERT.convertPage(result));
    }

    @GetMapping("/role")
    @ApiOperation("获取系统角色列表")
    @PreAuthorize("@ss.hasPermission('system:role:query')")
    public Result<List<SystemRoleRespVo>> list() {
        return success(SystemRoleConvert.CONVERT.convertList(systemRoleService.listRole()));
    }

    @GetMapping("/role/{id}")
    @ApiOperation("获取指定系统角色")
    @PreAuthorize("@ss.hasPermission('system:role:query')")
    public Result<SystemRoleRespVo> findById(@PathVariable Long id) {
        SystemRole role = systemRoleService.findById(id);
        return success(SystemRoleConvert.CONVERT.convert(role));
    }

    @PutMapping("/role/assign/{userId}")
    @ApiOperation("修改用户的角色")
    @PreAuthorize("@ss.hasPermission('system:role:assign')")
    public Result<Boolean> assignRole(@PathVariable Long userId, @RequestBody Set<Long> roleIds) {
        permissionBiz.assignRole(userId, roleIds);
        return success(true);
    }

    @PutMapping("/menu/assign/{roleId}")
    @ApiOperation("修改角色的菜单")
    @PreAuthorize("@ss.hasPermission('system:menu:assign')")
    public Result<Boolean> assignMenu(@PathVariable Long roleId, @RequestBody Set<Long> menuIds) {
        permissionBiz.assignMenu(roleId, menuIds);
        return success(true);
    }

    @GetMapping("/role-by-user/{userId}")
    @ApiOperation("获取用户的角色")
    @PreAuthorize("@ss.hasPermission('system:role:query')")
    public Result<List<SystemRoleRespVo>> listByUser(@PathVariable Long userId) {
        return success(SystemRoleConvert.CONVERT.convertList(permissionBiz.findRoleByUserId(userId)));
    }

    @GetMapping("/menu-by-role/{roleId}")
    @ApiOperation("获取角色的菜单id")
    @PreAuthorize("@ss.hasPermission('system:menu:query')")
    public Result<Set<Long>> listMenuByRoleId(@PathVariable Long roleId) {
        Set<Long> list = permissionBiz.listMenuIdByRoleId(roleId);
        return success(list);
    }

    @GetMapping("/user-permission")
    @ApiOperation("获取当前登录用户的权限列表")
    public Result<Set<String>> listPermissionByUser() {
        Set<String> list = permissionBiz.listPermissionByUser();
        return success(list);
    }

    @GetMapping("/user-menu")
    @ApiOperation("获取当前登录用户的菜单列表")
    public Result<List<SystemMenuRespVo>> listMenuByUser() {
        return success(CONVERT.convertList(permissionBiz.listMenuByUser()));
    }
}
