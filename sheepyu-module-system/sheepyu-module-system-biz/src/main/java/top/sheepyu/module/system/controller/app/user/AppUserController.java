package top.sheepyu.module.system.controller.app.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import top.sheepyu.framework.security.config.LoginUser;
import top.sheepyu.framework.security.core.annotations.Permit;
import top.sheepyu.framework.web.core.annotations.FlowLimit;
import top.sheepyu.module.common.common.Result;
import top.sheepyu.module.system.controller.app.user.vo.AppUserLoginVo;
import top.sheepyu.module.system.controller.app.user.vo.AppUserRespVo;
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
public class AppUserController {
    @Resource
    private SystemUserBiz systemUserBiz;

    @Permit
    @FlowLimit
    @PostMapping("/login")
    @ApiOperation("账号密码登录")
    public Result<LoginUser> login(@RequestBody AppUserLoginVo loginVo) {
        LoginUser loginUser = systemUserBiz.loginOfApp(loginVo);
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

    @Permit
    @FlowLimit(5)
    @PostMapping("/refreshToken/{refreshToken}")
    @ApiOperation("刷新令牌")
    public Result<LoginUser> refreshToken(@PathVariable String refreshToken) {
        LoginUser loginUser = systemUserBiz.refreshToken(refreshToken);
        return success(loginUser);
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
    public Result<AppUserRespVo> info() {
        SystemUser user = systemUserBiz.info();
        return success(SystemUserConvert.CONVERT.convertApp(user));
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
    public Result<Boolean> updatePassword(@RequestParam String password) {
        systemUserBiz.updatePassword(password);
        return success(true);
    }
}
