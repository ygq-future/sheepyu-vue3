package top.sheepyu.framework.sms.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ygq
 * @date 2023-01-22 15:13
 **/
@AllArgsConstructor
@Getter
public enum SmsTypeEnum {
    DEFAULT("default"),
    EMAIL("email"),
    ;
    private final String code;
}
