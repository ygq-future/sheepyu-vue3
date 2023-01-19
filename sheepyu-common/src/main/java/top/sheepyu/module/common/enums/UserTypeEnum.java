package top.sheepyu.module.common.enums;

import cn.hutool.core.util.ArrayUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import top.sheepyu.module.common.common.IterableEnum;
import top.sheepyu.module.common.constants.ErrorCodeConstants;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static top.sheepyu.module.common.exception.CommonException.exception;

/**
 * 全局用户类型枚举
 */
@AllArgsConstructor
@Getter
public enum UserTypeEnum implements IterableEnum {

    MEMBER(1, "会员"), // 面向 c 端，普通用户
    ADMIN(2, "管理员"); // 面向 b 端，管理后台

    private final Integer value;
    private final String name;

    public static UserTypeEnum valueOf(Integer value) {
        UserTypeEnum one = ArrayUtil.firstMatch(userType -> userType.getValue().equals(value), UserTypeEnum.values());
        if (one == null) {
            throw exception(ErrorCodeConstants.USERTYPE_ERROR);
        }
        return one;
    }

    @Override
    public List<Integer> list() {
        return Arrays.stream(values()).map(UserTypeEnum::getValue).collect(Collectors.toList());
    }
}
