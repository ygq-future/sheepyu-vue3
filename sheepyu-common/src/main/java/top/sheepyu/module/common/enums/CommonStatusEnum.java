package top.sheepyu.module.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.sheepyu.module.common.common.IterableEnum;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 功能状态枚举
 */
@Getter
@AllArgsConstructor
public enum CommonStatusEnum implements IterableEnum {
    ENABLE(1, "启用"),
    DISABLE(0, "禁用"),
    ;

    /**
     * 状态值
     */
    private final int code;
    /**
     * 状态名
     */
    private final String desc;

    @Override
    public List<Integer> list() {
        return Arrays.stream(values()).map(CommonStatusEnum::getCode).collect(Collectors.toList());
    }
}
