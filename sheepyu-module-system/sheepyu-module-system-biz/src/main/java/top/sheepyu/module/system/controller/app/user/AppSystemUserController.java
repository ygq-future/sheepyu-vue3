package top.sheepyu.module.system.controller.app.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import top.sheepyu.framework.security.core.LoginUser;
import top.sheepyu.framework.security.core.annotations.Permit;
import top.sheepyu.module.common.common.Result;
import top.sheepyu.module.common.enums.CaptchaEnum;
import top.sheepyu.module.system.controller.admin.user.vo.SystemUserLoginVo;
import top.sheepyu.module.system.controller.admin.user.vo.SystemUserRespVo;
import top.sheepyu.module.system.convert.user.SystemUserConvert;
import top.sheepyu.module.system.dao.user.SystemUser;
import top.sheepyu.module.system.service.user.SystemUserBiz;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static top.sheepyu.module.common.common.Result.success;

/**
 * @author ygq
 * @date 2023-01-19 11:15
 **/
@RestController
@RequestMapping("/system/user")
@Api(tags = "用户端 - 用户相关")
public class AppSystemUserController {
    @Resource
    private SystemUserBiz systemUserBiz;

    @Permit
    @PostMapping("/login")
    @ApiOperation("用户账号密码登录")
    public Result<LoginUser> create(@RequestBody SystemUserLoginVo loginVo) {
        LoginUser loginUser = systemUserBiz.login(loginVo);
        return success(loginUser);
    }

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

    @GetMapping("/captcha/{type}")
    @ApiOperation("获取验证码图片")
    @Permit
    public void captcha(@PathVariable Integer type, HttpServletRequest req, HttpServletResponse res) throws IOException {
        CaptchaEnum captchaEnum = CaptchaEnum.valueOf(type);

    }
}
