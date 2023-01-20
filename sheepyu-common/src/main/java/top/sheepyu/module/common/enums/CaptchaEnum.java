package top.sheepyu.module.common.enums;

import cn.hutool.core.util.ArrayUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import top.sheepyu.module.common.constants.ErrorCodeConstants;

import static top.sheepyu.module.common.exception.CommonException.exception;

/**
 * @author ygq
 * @date 2023-01-19 15:54
 **/
@Getter
@AllArgsConstructor
public enum CaptchaEnum {
    TEXT(0, "文本验证码"),
    ARITHMETIC(1, "数学验证码"),
    GIF(2, "gif验证码"),
    CHINESE(3, "中文验证码"),
    ;
    private final int code;
    private final String desc;

    public static CaptchaEnum valueOf(int value) {
        CaptchaEnum one = ArrayUtil.firstMatch(captcha -> captcha.getCode() == value, values());
        if (one == null) {
            throw exception(ErrorCodeConstants.CAPTCHA_TYPE_ERROR);
        }
        return one;
    }
}
