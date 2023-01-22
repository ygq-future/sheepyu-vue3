package top.sheepyu.module.system.controller.app.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import top.sheepyu.framework.security.core.LoginUser;
import top.sheepyu.framework.security.core.annotations.Permit;
import top.sheepyu.framework.web.annotations.FlowLimit;
import top.sheepyu.module.common.common.Result;
import top.sheepyu.module.system.controller.admin.user.vo.SystemUserLoginVo;
import top.sheepyu.module.system.controller.admin.user.vo.SystemUserRespVo;
import top.sheepyu.module.system.controller.app.user.vo.EmailLoginVo;
import top.sheepyu.module.system.convert.user.SystemUserConvert;
import top.sheepyu.module.system.dao.user.SystemUser;
import top.sheepyu.module.system.service.user.SystemUserBiz;

import javax.annotation.Resource;

import static top.sheepyu.module.common.common.Result.success;

/**
 * @author ygq
 * @date 2023-01-19 11:15
 **/
@RestController
@RequestMapping("/system/user")
@Api(tags = "用户端 - 用户授权")
public class AppSystemUserController {
    @Resource
    private SystemUserBiz systemUserBiz;

    @Permit
    @FlowLimit
    @PostMapping("/login")
    @ApiOperation("账号密码登录")
    public Result<LoginUser> login(@RequestBody SystemUserLoginVo loginVo) {
        LoginUser loginUser = systemUserBiz.login(loginVo);
        return success(loginUser);
    }

    @Permit
    @FlowLimit
    @PostMapping("/email-login")
    @ApiOperation("用户邮箱验证码登录")
    public Result<LoginUser> emailLogin(@RequestBody EmailLoginVo loginVo) {
        LoginUser loginUser = systemUserBiz.loginByEmail(loginVo);
        return success(loginUser);
    }

    @Permit
    @FlowLimit(5)
    @PostMapping("/send-code")
    @ApiOperation("发送邮箱验证码")
    public Result<Boolean> emailLogin(@RequestParam String email) {
        systemUserBiz.sendCode(email);
        return success(true);
    }

    @FlowLimit(5)
    @GetMapping("/logout")
    @ApiOperation("注销登录")
    public Result<Boolean> logout() {
        systemUserBiz.logout();
        return success(true);
    }

    @GetMapping("/info")
    @ApiOperation("获取用户信息")
    public Result<SystemUserRespVo> info() {
        SystemUser user = systemUserBiz.info();
        return success(SystemUserConvert.CONVERT.convert(user));
    }
}
