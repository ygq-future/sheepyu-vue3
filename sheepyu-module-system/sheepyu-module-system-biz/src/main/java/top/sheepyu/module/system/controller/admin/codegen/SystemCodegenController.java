package top.sheepyu.module.system.controller.admin.codegen;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ZipUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import top.sheepyu.framework.log.core.annotations.RecordLog;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.common.common.Result;
import top.sheepyu.module.common.util.ServletUtil;
import top.sheepyu.module.system.controller.admin.codegen.vo.SystemCodegenQueryVo;
import top.sheepyu.module.system.controller.admin.codegen.vo.SystemCodegenRespVo;
import top.sheepyu.module.system.controller.admin.codegen.vo.SystemCodegenUpdateVo;
import top.sheepyu.module.system.controller.admin.codegen.vo.TableInfoRespVo;
import top.sheepyu.module.system.service.codegen.SystemCodegenBiz;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static top.sheepyu.framework.log.core.enums.OperateTypeEnum.GENERATE;
import static top.sheepyu.module.common.common.Result.success;
import static top.sheepyu.module.system.convert.codegen.SystemCodegenConvert.CONVERT;

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

    @GetMapping("/page")
    @ApiOperation("代码生成分页")
    @PreAuthorize("@ss.hasPermission('system:codegen:query')")
    public Result<PageResult<SystemCodegenRespVo>> pageCodegen(SystemCodegenQueryVo queryVo) {
        return success(CONVERT.convertPage(systemCodegenBiz.pageCodegen(queryVo)));
    }

    @GetMapping("/{id}")
    @ApiOperation("查找代码生成")
    @PreAuthorize("@ss.hasPermission('system:codegen:query')")
    public Result<SystemCodegenRespVo> findCodegen(@PathVariable Long id) {
        return success(CONVERT.convert(systemCodegenBiz.findCodegen(id)));
    }

    @PostMapping
    @ApiOperation("创建代码生成")
    @PreAuthorize("@ss.hasPermission('system:codegen:create')")
    public Result<Boolean> createCodegen(@RequestBody List<String> tableNames) {
        systemCodegenBiz.createCodegen(tableNames);
        return success(true);
    }

    @PutMapping
    @ApiOperation("修改代码生成")
    @PreAuthorize("@ss.hasPermission('system:codegen:update')")
    public Result<Boolean> updateCodegen(@RequestBody SystemCodegenUpdateVo updateVo) {
        systemCodegenBiz.updateCodegen(updateVo);
        return success(true);
    }

    @DeleteMapping("/{ids}")
    @ApiOperation("删除代码生成")
    @PreAuthorize("@ss.hasPermission('system:codegen:delete')")
    public Result<Boolean> deleteCodegen(@PathVariable String ids) {
        systemCodegenBiz.deleteCodegen(ids);
        return success(true);
    }

    @PatchMapping("/sync/{id}")
    @ApiOperation("同步")
    @PreAuthorize("@ss.hasPermission('system:codegen:create')")
    public Result<Boolean> syncCodegen(@PathVariable Long id) {
        systemCodegenBiz.syncCodegen(id);
        return success(true);
    }

    @GetMapping("/table-list")
    @ApiOperation("数据库表列表")
    @PreAuthorize("@ss.hasPermission('system:codegen:query')")
    public Result<List<TableInfoRespVo>> tableList(@RequestParam(required = false, defaultValue = "") String keyword) {
        return success(systemCodegenBiz.tableList(keyword));
    }

    @GetMapping("/generate/{id}")
    @ApiOperation("生成代码")
    @PreAuthorize("@ss.hasPermission('system:codegen:generate')")
    @RecordLog(GENERATE)
    public void generate(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Map<String, String> result = systemCodegenBiz.generateCode(id);
        String[] paths = result.keySet().toArray(new String[]{});
        ByteArrayInputStream[] ins = result.values().stream().map(IoUtil::toUtf8Stream).toArray(ByteArrayInputStream[]::new);
        ServletUtil.responseFile(response, "codegen.zip");
        ZipUtil.zip(response.getOutputStream(), paths, ins);
    }

    @GetMapping("/preview/{id}")
    @ApiOperation("预览")
    @PreAuthorize("@ss.hasPermission('system:codegen:generate')")
    public Result<Map<String, String>> preview(@PathVariable Long id) {
        return success(systemCodegenBiz.generateCode(id));
    }
}
