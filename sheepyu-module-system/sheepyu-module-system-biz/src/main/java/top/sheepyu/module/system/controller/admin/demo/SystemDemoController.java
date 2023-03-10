package top.sheepyu.module.system.controller.admin.demo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.sheepyu.framework.log.core.annotations.RecordLog;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.common.common.Result;
import top.sheepyu.module.common.util.ExcelUtil;
import top.sheepyu.module.system.controller.admin.demo.vo.*;
import top.sheepyu.module.system.dao.demo.SystemDemo;
import top.sheepyu.module.system.service.demo.SystemDemoService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static top.sheepyu.framework.log.core.enums.OperateTypeEnum.EXPORT;
import static top.sheepyu.framework.log.core.enums.OperateTypeEnum.IMPORT;
import static top.sheepyu.module.common.common.Result.success;
import static top.sheepyu.module.system.convert.demo.SystemDemoConvert.CONVERT;

/**
 * @author sheepyu
 * @date 2023-03-10 21:40:22
 **/
@RestController
@RequestMapping("/system/demo")
@Api(tags = "管理端 - 测试")
public class SystemDemoController {
    @Resource
    private SystemDemoService systemDemoService;

    @PostMapping
    @ApiOperation("创建测试")
    @PreAuthorize("@ss.hasPermission('system:demo:create')")
    public Result<Boolean> create(@RequestBody SystemDemoCreateVo createVo) {
        systemDemoService.create(createVo);
        return success(true);
    }

    @PutMapping
    @ApiOperation("修改测试")
    @PreAuthorize("@ss.hasPermission('system:demo:update')")
    public Result<Boolean> update(@RequestBody SystemDemoUpdateVo updateVo) {
        systemDemoService.update(updateVo);
        return success(true);
    }

    @DeleteMapping("/{ids}")
    @ApiOperation("删除测试")
    @PreAuthorize("@ss.hasPermission('system:demo:delete')")
    public Result<Boolean> delete(@PathVariable String ids) {
        systemDemoService.delete(ids);
        return success(true);
    }


    @GetMapping("/page")
    @ApiOperation("获取测试分页")
    @PreAuthorize("@ss.hasPermission('system:demo:query')")
    public Result<PageResult<SystemDemoRespVo>> page(SystemDemoQueryVo queryVo) {
        return success(CONVERT.convertPage(systemDemoService.page(queryVo)));
    }

    @GetMapping("/{id}")
    @ApiOperation("获取指定测试")
    @PreAuthorize("@ss.hasPermission('system:demo:query')")
    public Result<SystemDemoRespVo> findById(@PathVariable Long id) {
        return success(CONVERT.convert(systemDemoService.findById(id)));
    }

    @GetMapping("/export")
    @ApiOperation("导出所有测试")
    @PreAuthorize("@ss.hasPermission('system:demo:export')")
    @RecordLog(EXPORT)
    public void export(SystemDemoQueryVo queryVo, HttpServletResponse response) throws IOException {
        List<SystemDemo> list = systemDemoService.list(queryVo);
        List<SystemDemoExcelVo> data = CONVERT.convert2Excel(list);
        ExcelUtil.write(response, "测试", SystemDemoExcelVo.class, data);
    }

    @PostMapping("/batchImport")
    @ApiOperation("批量导入测试")
    @PreAuthorize("@ss.hasPermission('system:demo:import')")
    @RecordLog(IMPORT)
    public Result<Boolean> batchImport(MultipartFile file) throws IOException {
        List<SystemDemoExcelVo> result = ExcelUtil.read(file, SystemDemoExcelVo.class);
        systemDemoService.batchImport(result);
        return success(true);
    }
}
