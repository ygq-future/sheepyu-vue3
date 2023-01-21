package top.sheepyu.module.system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ygq
 * @date 2023-01-21 15:00
 **/
@AllArgsConstructor
@Getter
public enum SystemConfigKeyEnum {
    CAPTCHA_ENABLE("captcha.enable", Boolean.class),
    ;
    private final String key;
    private final Class<?> clazz;
}
