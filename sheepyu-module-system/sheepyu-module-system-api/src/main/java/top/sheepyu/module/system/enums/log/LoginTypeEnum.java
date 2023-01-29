package top.sheepyu.module.system.enums.log;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.sheepyu.module.common.common.IterableEnum;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 登录日志的类型枚举
 */
@Getter
@AllArgsConstructor
public enum LoginTypeEnum implements IterableEnum {
    LOGIN_USERNAME(100), // 使用账号登录
    LOGIN_SOCIAL(101), // 使用社交登录
    LOGIN_MOBILE(103), // 使用手机登陆
    LOGIN_EMAIL(104), // 使用邮箱登录
    ;

    /**
     * 日志类型
     */
    private final int code;

    @Override
    public List<Integer> list() {
        return Arrays.stream(values()).map(LoginTypeEnum::getCode).collect(Collectors.toList());
    }
}
