package top.sheepyu.module.common.enums.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.sheepyu.module.common.common.IterableEnum;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 通用状态枚举
 */
@Getter
@AllArgsConstructor
public enum StatusEnum implements IterableEnum {
    TRUE(1, "是"),
    FALSE(0, "否"),
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
        return Arrays.stream(values()).map(StatusEnum::getCode).collect(Collectors.toList());
    }
}
