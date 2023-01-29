package top.sheepyu.module.system.controller.admin.dict;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.common.common.Result;
import top.sheepyu.module.system.controller.admin.dict.data.SystemDictDataCreateVo;
import top.sheepyu.module.system.controller.admin.dict.data.SystemDictDataRespVo;
import top.sheepyu.module.system.controller.admin.dict.data.SystemDictDataUpdateVo;
import top.sheepyu.module.system.controller.admin.dict.type.SystemDictTypeCreateVo;
import top.sheepyu.module.system.controller.admin.dict.type.SystemDictTypeQueryVo;
import top.sheepyu.module.system.controller.admin.dict.type.SystemDictTypeRespVo;
import top.sheepyu.module.system.controller.admin.dict.type.SystemDictTypeUpdateVo;
import top.sheepyu.module.system.dao.dict.SystemDictData;
import top.sheepyu.module.system.dao.dict.SystemDictType;
import top.sheepyu.module.system.service.dict.SystemDictBiz;

import javax.annotation.Resource;
import java.util.List;

import static top.sheepyu.module.system.convert.dict.SystemDictConvert.CONVERT;

/**
 * @author ygq
 * @date 2023-01-24 14:11
 **/
@RestController
@RequestMapping("/system/dict")
@Api(tags = "管理端 - 字典数据")
public class SystemDictController {
    @Resource
    private SystemDictBiz systemDictBiz;

    @PostMapping("/type")
    @ApiOperation("添加字典类型")
    @PreAuthorize("@ss.hasPermission('system:dict:create')")
    public Result<Boolean> createDictType(@RequestBody SystemDictTypeCreateVo createVo) {
        systemDictBiz.createDictType(createVo);
        return Result.success(true);
    }

    @PutMapping("/type")
    @ApiOperation("修改字典类型")
    @PreAuthorize("@ss.hasPermission('system:dict:update')")
    public Result<Boolean> updateDictType(@RequestBody SystemDictTypeUpdateVo updateVo) {
        systemDictBiz.updateDictType(updateVo);
        return Result.success(true);
    }

    @DeleteMapping("/type/{id}")
    @ApiOperation("删除字典类型")
    @PreAuthorize("@ss.hasPermission('system:dict:delete')")
    public Result<Boolean> deleteDictType(@PathVariable Long id) {
        systemDictBiz.deleteDictType(id);
        return Result.success(true);
    }

    @GetMapping("/type/page")
    @ApiOperation("字典类型分页")
    @PreAuthorize("@ss.hasPermission('system:dict:query')")
    public Result<PageResult<SystemDictTypeRespVo>> pageDictType(SystemDictTypeQueryVo queryVo) {
        PageResult<SystemDictType> pageResult = systemDictBiz.pageDictType(queryVo);
        return Result.success(CONVERT.convertPage(pageResult));
    }

    @GetMapping("/type/{id}")
    @ApiOperation("获取字典类型")
    @PreAuthorize("@ss.hasPermission('system:dict:query')")
    public Result<SystemDictTypeRespVo> findDictType(@PathVariable Long id) {
        SystemDictType dictType = systemDictBiz.findDictType(id);
        return Result.success(CONVERT.convert(dictType));
    }

    @PostMapping("/data")
    @ApiOperation("添加字典数据")
    @PreAuthorize("@ss.hasPermission('system:dict:create')")
    public Result<Boolean> createDictData(@RequestBody SystemDictDataCreateVo createVo) {
        systemDictBiz.createDictData(createVo);
        return Result.success(true);
    }

    @PutMapping("/data")
    @ApiOperation("修改字典数据")
    @PreAuthorize("@ss.hasPermission('system:dict:update')")
    public Result<Boolean> updateDictData(@RequestBody SystemDictDataUpdateVo updateVo) {
        systemDictBiz.updateDictData(updateVo);
        return Result.success(true);
    }

    @DeleteMapping("/data/{ids}")
    @ApiOperation("批量删除字典数据")
    @PreAuthorize("@ss.hasPermission('system:dict:delete')")
    public Result<Boolean> batchDeleteDictData(@PathVariable String ids) {
        systemDictBiz.batchDeleteDictData(ids);
        return Result.success(true);
    }

    @GetMapping("/data/{type}")
    @ApiOperation("字典数据列表")
    @PreAuthorize("@ss.hasPermission('system:dict:query')")
    public Result<List<SystemDictDataRespVo>> listDictData(@PathVariable String type) {
        List<SystemDictData> list = systemDictBiz.listDictData(type);
        return Result.success(CONVERT.convertList(list));
    }

    @GetMapping("/data/{type}/{value}")
    @ApiOperation("根据类型和value获取字典数据")
    @PreAuthorize("@ss.hasPermission('system:dict:query')")
    public Result<SystemDictDataRespVo> findDictData(@PathVariable String type, @PathVariable String value) {
        SystemDictData dictData = systemDictBiz.findDictData(type, value);
        return Result.success(CONVERT.convert(dictData));
    }

    @GetMapping("/data/id/{id}")
    @ApiOperation("根据id获取字典数据")
    @PreAuthorize("@ss.hasPermission('system:dict:query')")
    public Result<SystemDictDataRespVo> findDictData(@PathVariable Long id) {
        SystemDictData dictData = systemDictBiz.findDictData(id);
        return Result.success(CONVERT.convert(dictData));
    }
}
