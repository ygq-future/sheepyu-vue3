package top.sheepyu.module.system.controller.admin.menu;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import top.sheepyu.module.common.common.Result;
import top.sheepyu.module.system.controller.admin.menu.vo.SystemMenuCreateVo;
import top.sheepyu.module.system.controller.admin.menu.vo.SystemMenuQueryVo;
import top.sheepyu.module.system.controller.admin.menu.vo.SystemMenuRespVo;
import top.sheepyu.module.system.controller.admin.menu.vo.SystemMenuUpdateVo;
import top.sheepyu.module.system.dao.menu.SystemMenu;
import top.sheepyu.module.system.service.menu.SystemMenuService;

import javax.annotation.Resource;
import java.util.List;

import static top.sheepyu.module.common.common.Result.success;
import static top.sheepyu.module.system.convert.menu.SystemMenuConvert.CONVERT;

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

    @GetMapping("/{id}")
    @ApiOperation("获取指定系统菜单")
    @PreAuthorize("@ss.hasPermission('system:menu:query')")
    public Result<SystemMenuRespVo> findById(@PathVariable Long id) {
        SystemMenu menu = systemMenuService.findById(id);
        return success(CONVERT.convert(menu));
    }
}
