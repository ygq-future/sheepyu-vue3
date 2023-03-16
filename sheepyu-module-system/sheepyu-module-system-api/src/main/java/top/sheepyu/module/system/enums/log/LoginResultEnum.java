package top.sheepyu.module.system.enums.log;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.sheepyu.module.common.common.IterableEnum;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 登录结果的枚举类
 */
@Getter
@AllArgsConstructor
public enum LoginResultEnum implements IterableEnum {
    SUCCESS(0), // 成功
    BAD_CREDENTIALS(10), // 账号或密码不正确
    USER_DISABLED(20), // 用户被禁用
    CAPTCHA_CODE_ERROR(30), // 验证码错误
    ;

    private final int code;

    @Override
    public List<Integer> list() {
        return Arrays.stream(values()).map(LoginResultEnum::getCode).collect(Collectors.toList());
    }
}
