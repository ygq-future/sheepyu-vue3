package top.sheepyu.module.system.service.captcha;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.ChineseCaptcha;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.framework.redis.util.RedisUtil;
import top.sheepyu.module.common.constants.ErrorCodeConstants;
import top.sheepyu.module.common.enums.CaptchaEnum;
import top.sheepyu.module.system.controller.admin.captcha.vo.CaptchaRespVo;
import top.sheepyu.module.system.service.config.SystemConfigService;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static top.sheepyu.module.common.exception.CommonException.exception;
import static top.sheepyu.module.system.constants.RedisConstants.CAPTCHA_IMAGE_KEY;
import static top.sheepyu.module.system.constants.RedisConstants.CAPTCHA_IMAGE_TTL;
import static top.sheepyu.module.system.enums.SystemConfigKeyEnum.CAPTCHA_ENABLE;

/**
 * @author ygq
 * @date 2023-01-19 16:12
 **/
@Service
@Slf4j
@Validated
public class CaptchaServiceImpl implements CaptchaService {
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private SystemConfigService systemConfigService;

    @Override
    public CaptchaRespVo generateCaptcha(CaptchaEnum type) {
        switch (type) {
            case GIF:
                GifCaptcha gifCaptcha = new GifCaptcha();
                return fillData(gifCaptcha);
            case TEXT:
                SpecCaptcha specCaptcha = new SpecCaptcha();
                return fillData(specCaptcha);
            case CHINESE:
                ChineseCaptcha chineseCaptcha = new ChineseCaptcha();
                return fillData(chineseCaptcha);
            case ARITHMETIC:
                ArithmeticCaptcha arithmeticCaptcha = new ArithmeticCaptcha();
                String arithmetic = arithmeticCaptcha.getArithmeticString();
                arithmetic = arithmetic.replace("?", "");

                String key = UUID.fastUUID().toString(true);
                redisUtil.set(CAPTCHA_IMAGE_KEY.concat(key), arithmetic + arithmeticCaptcha.text(), CAPTCHA_IMAGE_TTL, TimeUnit.MINUTES);
                return new CaptchaRespVo(arithmeticCaptcha.toBase64(), arithmetic, key);
        }
        return null;
    }

    @Override
    public boolean verifyCaptcha(String key, String code) {
        Boolean captchaEnable = systemConfigService.get(CAPTCHA_ENABLE);
        //如果验证码没有开启
        if (!captchaEnable) {
            return true;
        }

        if (StrUtil.hasBlank(key, code)) {
            throw exception(ErrorCodeConstants.INVALID_PARAMS);
        }

        key = CAPTCHA_IMAGE_KEY.concat(key);
        String value = redisUtil.get(key);
        redisUtil.del(key);
        return StrUtil.isNotBlank(value) && Objects.equals(code, value);
    }

    private CaptchaRespVo fillData(Captcha captcha) {
        String key = UUID.fastUUID().toString(true);
        redisUtil.set(CAPTCHA_IMAGE_KEY.concat(key), captcha.text(), CAPTCHA_IMAGE_TTL, TimeUnit.MINUTES);
        return new CaptchaRespVo(captcha.toBase64(), null, key);
    }
}
