package top.sheepyu.module.system.controller.admin.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import top.sheepyu.framework.log.core.annotations.RecordLog;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.common.common.Result;
import top.sheepyu.module.common.util.ExcelUtil;
import top.sheepyu.module.system.controller.admin.user.vo.*;
import top.sheepyu.module.system.convert.user.SystemUserConvert;
import top.sheepyu.module.system.dao.user.SystemUser;
import top.sheepyu.module.system.service.user.SystemUserBiz;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static top.sheepyu.framework.log.core.enums.OperateTypeEnum.EXPORT;
import static top.sheepyu.module.common.common.Result.success;

/**
 * @author ygq
 * @date 2023-01-18 14:41
 **/
@RestController
@RequestMapping("/system/user")
@Api(tags = "管理端 - 系统用户")
public class SystemUserController {
    @Resource
    private SystemUserBiz systemUserBiz;

    @GetMapping("/info/{id}")
    @ApiOperation("获取指定用户信息")
    @PreAuthorize("@ss.hasPermission('system:user:query')")
    public Result<SystemUserRespVo> infoById(@PathVariable Long id) {
        SystemUser user = systemUserBiz.infoById(id);
        return success(SystemUserConvert.CONVERT.convert(user));
    }

    @GetMapping("/page")
    @ApiOperation("获取管理员用户信息分页")
    @PreAuthorize("@ss.hasPermission('system:user:query')")
    public Result<PageResult<SystemUserRespVo>> page(SystemUserQueryVo queryVo) {
        PageResult<SystemUser> result = systemUserBiz.pageUser(queryVo);
        return success(SystemUserConvert.CONVERT.convertPage(result));
    }

    @PostMapping("/create")
    @ApiOperation("创建管理员")
    @PreAuthorize("@ss.hasPermission('system:user:create')")
    public Result<Boolean> create(@RequestBody SystemUserCreateVo createVo) {
        systemUserBiz.createUser(createVo);
        return success(true);
    }

    @PutMapping("/update")
    @ApiOperation("修改管理员")
    @PreAuthorize("@ss.hasPermission('system:user:update')")
    public Result<Boolean> update(@RequestBody SystemUserUpdateVo updateVo) {
        systemUserBiz.updateUser(updateVo);
        return success(true);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除管理员")
    @PreAuthorize("@ss.hasPermission('system:user:delete')")
    public Result<Boolean> delete(@PathVariable Long id) {
        systemUserBiz.deleteUser(id);
        return success(true);
    }

    @GetMapping("/export")
    @ApiOperation("导出系统所有用户")
    @PreAuthorize("@ss.hasPermission('system:user:export')")
    @RecordLog(EXPORT)
    public void export(SystemUserQueryVo queryVo, HttpServletResponse response) throws IOException {
        List<SystemUser> list = systemUserBiz.listUser(queryVo);
        List<SystemUserExcelVo> data = SystemUserConvert.CONVERT.convertExcel(list);
        ExcelUtil.write(response, "系统用户", SystemUserExcelVo.class, data);
    }
}
