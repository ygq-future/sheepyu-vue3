package top.sheepyu.module.system.service.captcha;

import top.sheepyu.module.common.enums.CaptchaEnum;
import top.sheepyu.module.system.controller.app.captcha.vo.CaptchaRespVo;

/**
 * @author ygq
 * @date 2023-01-19 16:11
 **/
public interface CaptchaService {
    CaptchaRespVo generateCaptcha(CaptchaEnum type);

    boolean verifyCaptcha(String key, String code);
}
