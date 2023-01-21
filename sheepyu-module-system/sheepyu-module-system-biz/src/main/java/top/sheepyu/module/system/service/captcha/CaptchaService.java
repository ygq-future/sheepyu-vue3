package top.sheepyu.module.system.service.captcha;

import top.sheepyu.module.common.enums.CaptchaEnum;
import top.sheepyu.module.system.controller.app.captcha.vo.CaptchaRespVo;

import javax.validation.constraints.NotBlank;

/**
 * @author ygq
 * @date 2023-01-19 16:11
 **/
public interface CaptchaService {
    CaptchaRespVo generateCaptcha(CaptchaEnum type);

    /**
     * 校验验证码, 验证成功返回true
     *
     * @param key  key
     * @param code code
     * @return 返回true or false
     */
    boolean verifyCaptcha(@NotBlank(message = "验证码不能为空") String key,
                          @NotBlank(message = "验证码不能为空") String code);
}
