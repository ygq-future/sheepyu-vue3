package top.sheepyu.module.common.enums.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.sheepyu.module.common.common.IterableEnum;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 结果状态枚举
 */
@Getter
@AllArgsConstructor
public enum ResultStatusEnum implements IterableEnum {
    SUCCESS(1, "成功"),
    FAILED(0, "失败"),
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
        return Arrays.stream(values()).map(ResultStatusEnum::getCode).collect(Collectors.toList());
    }
}
