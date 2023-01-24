package top.sheepyu.module.common.enums.status;

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
public enum FunctionStatusEnum implements IterableEnum {
    ENABLE(1, "启用"),
    DISABLE(0, "禁用"),
    ;

    /**
     * 状态值
     */
    private final Integer code;
    /**
     * 状态名
     */
    private final String desc;

    @Override
    public List<Integer> list() {
        return Arrays.stream(values()).map(FunctionStatusEnum::getCode).collect(Collectors.toList());
    }
}
