package top.sheepyu.module.system.controller.admin.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import top.sheepyu.framework.log.core.annotations.RecordLog;
import top.sheepyu.framework.security.config.LoginUser;
import top.sheepyu.framework.security.core.annotations.Permit;
import top.sheepyu.framework.web.core.annotations.FlowLimit;
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

    /**
     * 管理端采用用户名加密码登录
     *
     * @param loginVo 登录vo对象
     * @return 返回令牌
     */
    @Permit
    @FlowLimit
    @PostMapping("/login")
    @ApiOperation("账号密码登录")
    public Result<LoginUser> login(@RequestBody SystemUserLoginVo loginVo) {
        LoginUser loginUser = systemUserBiz.login(loginVo);
        return success(loginUser);
    }

    @FlowLimit(5)
    @GetMapping("/logout")
    @ApiOperation("注销登录")
    public Result<Boolean> logout() {
        systemUserBiz.logout();
        return success(true);
    }

    @Permit
    @FlowLimit(5)
    @PostMapping("/refreshToken/{refreshToken}")
    @ApiOperation("刷新令牌")
    public Result<LoginUser> refreshToken(@PathVariable String refreshToken) {
        LoginUser loginUser = systemUserBiz.refreshToken(refreshToken);
        return success(loginUser);
    }

    @GetMapping("/info")
    @ApiOperation("获取用户信息")
    public Result<SystemUserRespVo> info() {
        SystemUser user = systemUserBiz.info();
        return success(SystemUserConvert.CONVERT.convert(user));
    }

    @PatchMapping("/nickname")
    @ApiOperation("修改用户昵称")
    public Result<Boolean> updateNickname(@RequestParam String nickname) {
        systemUserBiz.updateNickname(nickname);
        return success(true);
    }

    @PatchMapping("/mobile")
    @ApiOperation("修改用户手机号")
    public Result<Boolean> updateMobile(@RequestParam String mobile) {
        systemUserBiz.updateMobile(mobile);
        return success(true);
    }

    @PatchMapping("/email")
    @ApiOperation("修改用户邮箱")
    public Result<Boolean> updateEmail(@RequestParam String email) {
        systemUserBiz.updateEmail(email);
        return success(true);
    }

    @PatchMapping("/avatar")
    @ApiOperation("修改用户头像")
    public Result<Boolean> updateAvatar(@RequestParam String avatar) {
        systemUserBiz.updateAvatar(avatar);
        return success(true);
    }

    @PatchMapping("/password")
    @ApiOperation("修改用户密码")
    public Result<Boolean> updatePassword(@RequestBody SystemUpdatePassVo updatePassVo) {
        systemUserBiz.updatePassword(updatePassVo);
        return success(true);
    }

    @GetMapping("/{id}")
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

    @PostMapping
    @ApiOperation("创建管理员")
    @PreAuthorize("@ss.hasPermission('system:user:create')")
    public Result<Boolean> create(@RequestBody SystemUserCreateVo createVo) {
        systemUserBiz.createUser(createVo);
        return success(true);
    }

    @PutMapping
    @ApiOperation("修改管理员")
    @PreAuthorize("@ss.hasPermission('system:user:update')")
    public Result<Boolean> update(@RequestBody SystemUserUpdateVo updateVo) {
        systemUserBiz.updateUser(updateVo);
        return success(true);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除管理员")
    @PreAuthorize("@ss.hasPermission('system:user:delete')")
    public Result<Boolean> delete(@PathVariable Long id) {
        systemUserBiz.deleteUser(id);
        return success(true);
    }

    @PatchMapping("/reset-password/{id}")
    @ApiOperation(value = "重置指定用户密码", notes = "默认重置为系统配置中的默认密码, 如果传了newPass, 就会为newPass")
    @PreAuthorize("@ss.hasPermission('system:user:reset-password')")
    public Result<Boolean> resetPassword(@PathVariable Long id, @RequestParam(required = false) String newPass) {
        systemUserBiz.resetPassword(id, newPass);
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

    @GetMapping("/statistics")
    @ApiOperation("用户统计")
    @PreAuthorize("@ss.hasPermission('dashboard')")
    public Result<SystemUserStatisticsVo> statistics() {
        return success(systemUserBiz.statistics());
    }
}
