package top.sheepyu.module.system.controller.admin.captcha;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.sheepyu.framework.web.annotations.FlowLimit;
import top.sheepyu.module.common.common.Result;
import top.sheepyu.module.system.controller.admin.captcha.vo.CaptchaRespVo;
import top.sheepyu.module.system.service.captcha.CaptchaService;

import javax.annotation.Resource;

import static top.sheepyu.module.common.common.Result.success;
import static top.sheepyu.module.common.enums.CaptchaEnum.ARITHMETIC;

/**
 * @author ygq
 * @date 2023-01-19 16:22
 **/
@RestController
@RequestMapping("/captcha")
@Api(tags = "管理端 - 验证码")
public class CaptchaController {
    @Resource
    private CaptchaService captchaService;

    @FlowLimit(20)
    @GetMapping
    @ApiOperation("获取验证码")
    public Result<CaptchaRespVo> captcha() {
        return success(captchaService.generateCaptcha(ARITHMETIC));
    }

    @FlowLimit(20)
    @GetMapping("/verify/{key}/{code}")
    @ApiOperation("验证")
    public Result<Boolean> verify(@PathVariable String key, @PathVariable String code) {
        return success(captchaService.verifyCaptcha(key, code));
    }
}
