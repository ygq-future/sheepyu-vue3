package top.sheepyu.module.system.controller.admin.dept;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import top.sheepyu.module.common.common.Result;
import top.sheepyu.module.system.controller.admin.dept.vo.SystemDeptCreateVo;
import top.sheepyu.module.system.controller.admin.dept.vo.SystemDeptQueryVo;
import top.sheepyu.module.system.controller.admin.dept.vo.SystemDeptRespVo;
import top.sheepyu.module.system.controller.admin.dept.vo.SystemDeptUpdateVo;
import top.sheepyu.module.system.dao.dept.SystemDept;
import top.sheepyu.module.system.service.dept.SystemDeptBiz;

import javax.annotation.Resource;
import java.util.List;

import static top.sheepyu.module.common.common.Result.success;
import static top.sheepyu.module.system.convert.dept.SystemDeptConvert.CONVERT;

/**
 * @author ygq
 * @date 2023-01-14 15:58
 **/
@RestController
@RequestMapping("/system/dept")
@Slf4j
@Api(tags = "管理端 - 系统部门")
public class SystemDeptController {
    @Resource
    private SystemDeptBiz systemDeptBiz;

    @PostMapping
    @ApiOperation("添加系统部门")
    @PreAuthorize("@ss.hasPermission('system:dept:create')")
    public Result<Boolean> create(@RequestBody SystemDeptCreateVo createVo) {
        systemDeptBiz.createDept(createVo);
        return success(true);
    }

    @PutMapping
    @ApiOperation("修改系统部门")
    @PreAuthorize("@ss.hasPermission('system:dept:update')")
    public Result<Boolean> update(@RequestBody SystemDeptUpdateVo updateVo) {
        systemDeptBiz.updateDept(updateVo);
        return success(true);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除系统部门")
    @PreAuthorize("@ss.hasPermission('system:dept:delete')")
    public Result<Boolean> delete(@PathVariable Long id) {
        systemDeptBiz.deleteDept(id);
        return success(true);
    }

    @GetMapping
    @ApiOperation("获取系统部门列表(权限)")
    @PreAuthorize("@ss.hasPermission('system:dept:query')")
    public Result<List<SystemDeptRespVo>> listDeptByPermission(SystemDeptQueryVo queryVo) {
        List<SystemDept> result = systemDeptBiz.listDeptByPermission(queryVo);
        return success(CONVERT.convertList(result));
    }

    @GetMapping("/dept-user")
    @ApiOperation("获取系统部门职位下的用户树")
    @PreAuthorize("@ss.hasPermission('system:dept:query')")
    public Result<List<SystemDeptRespVo>> listDeptUser() {
        List<SystemDept> result = systemDeptBiz.listDeptUser();
        return success(CONVERT.convertList(result));
    }

    @GetMapping("/tree")
    @ApiOperation("获取系统部门列表树,排除岗位(权限)")
    @PreAuthorize("@ss.hasPermission('system:dept:query')")
    public Result<List<SystemDeptRespVo>> treeByPermission() {
        List<SystemDept> result = systemDeptBiz.treeByPermission();
        return success(CONVERT.convertList(result));
    }

    @GetMapping("/{id}")
    @ApiOperation("获取指定系统部门")
    @PreAuthorize("@ss.hasPermission('system:dept:query')")
    public Result<SystemDeptRespVo> findById(@PathVariable Long id) {
        SystemDept dept = systemDeptBiz.findById(id);
        return success(CONVERT.convert(dept));
    }
}
