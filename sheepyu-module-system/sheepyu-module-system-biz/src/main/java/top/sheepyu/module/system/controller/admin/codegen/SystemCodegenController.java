package top.sheepyu.module.system.controller.admin.codegen;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import top.sheepyu.module.common.common.Result;
import top.sheepyu.module.system.controller.admin.codegen.vo.SystemCodegenRespVo;
import top.sheepyu.module.system.controller.admin.codegen.vo.SystemCodegenUpdateVo;
import top.sheepyu.module.system.controller.admin.codegen.vo.TableInfoRespVo;
import top.sheepyu.module.system.service.codegen.SystemCodegenBiz;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ygq
 * @date 2023-03-03 19:46
 **/
@RestController
@RequestMapping("/system/codegen")
@Api(tags = "管理端 - 代码生成")
public class SystemCodegenController {
    @Resource
    private SystemCodegenBiz systemCodegenBiz;

    @GetMapping
    @ApiOperation("代码生成列表")
    @PreAuthorize("@ss.hasPermission('system:codegen:query')")
    public Result<List<SystemCodegenRespVo>> listCodegen(@RequestParam(required = false, defaultValue = "") String keyword) {
        return Result.success(systemCodegenBiz.listCodegen(keyword));
    }

    @PostMapping
    @ApiOperation("创建代码生成")
    @PreAuthorize("@ss.hasPermission('system:codegen:create')")
    public Result<Boolean> createCodegen(@RequestBody List<String> tableNames) {
        systemCodegenBiz.createCodegen(tableNames);
        return Result.success(true);
    }

    @PutMapping
    @ApiOperation("修改代码生成")
    @PreAuthorize("@ss.hasPermission('system:codegen:update')")
    public Result<Boolean> updateCodegen(@RequestBody SystemCodegenUpdateVo updateVo) {
        systemCodegenBiz.updateCodegen(updateVo);
        return Result.success(true);
    }

    @DeleteMapping("/{ids}")
    @ApiOperation("删除代码生成")
    @PreAuthorize("@ss.hasPermission('system:codegen:delete')")
    public Result<Boolean> deleteCodegen(@PathVariable String ids) {
        systemCodegenBiz.deleteCodegen(ids);
        return Result.success(true);
    }

    @GetMapping("/table-list")
    @ApiOperation("数据库表列表")
    @PreAuthorize("@ss.hasPermission('system:codegen:query')")
    public Result<List<TableInfoRespVo>> tableList(@RequestParam(required = false, defaultValue = "") String keyword) {
        return Result.success(systemCodegenBiz.tableList()
                .stream()
                .filter(tableInfo -> tableInfo.getName().contains(keyword) ||
                        tableInfo.getComment().contains(keyword))
                .collect(Collectors.toList()));
    }
}
