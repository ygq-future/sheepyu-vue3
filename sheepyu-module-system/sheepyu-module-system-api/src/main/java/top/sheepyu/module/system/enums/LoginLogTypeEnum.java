package top.sheepyu.module.system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 登录日志的类型枚举
 */
@Getter
@AllArgsConstructor
public enum LoginLogTypeEnum {
    LOGIN_USERNAME(100), // 使用账号登录
    LOGIN_SOCIAL(101), // 使用社交登录
    LOGIN_MOBILE(103), // 使用手机登陆
    LOGIN_EMAIL(104), // 使用邮箱登录
    ;

    /**
     * 日志类型
     */
    private final int code;
}
