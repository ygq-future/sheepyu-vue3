package top.sheepyu.module.system.service.captcha;

import top.sheepyu.module.common.enums.CaptchaEnum;
import top.sheepyu.module.system.controller.app.captcha.vo.CaptchaRespVo;

/**
 * @author ygq
 * @date 2023-01-19 16:11
 **/
public interface CaptchaService {
    CaptchaRespVo generateCaptcha(CaptchaEnum type);

    /**
     * 校验验证码, 验证成功返回true
     * @param key key
     * @param code code
     * @return 返回true or false
     */
    boolean verifyCaptcha(String key, String code);
}
