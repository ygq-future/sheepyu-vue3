package top.sheepyu.framework.sms.core.enums;

import cn.hutool.core.util.ArrayUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static top.sheepyu.module.common.constants.ErrorCodeConstants.SMS_SENDER_DONT_MATCH;
import static top.sheepyu.module.common.exception.CommonException.exception;

/**
 * @author ygq
 * @date 2023-01-22 15:13
 **/
@AllArgsConstructor
@Getter
public enum SmsSenderTypeEnum {
    DEFAULT("DEFAULT"),
    EMAIL("EMAIL"),
    ;
    private final String code;

    public static SmsSenderTypeEnum value(String code) {
        SmsSenderTypeEnum typeEnum = ArrayUtil.firstMatch(type -> code.equalsIgnoreCase(type.getCode()), values());
        if (typeEnum == null) {
            throw exception(SMS_SENDER_DONT_MATCH);
        }
        return typeEnum;
    }
}
