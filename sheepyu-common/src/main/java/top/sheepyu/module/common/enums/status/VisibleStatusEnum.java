package top.sheepyu.module.common.enums.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.sheepyu.module.common.common.IterableEnum;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 显示状态枚举
 */
@Getter
@AllArgsConstructor
public enum VisibleStatusEnum implements IterableEnum {
    VISIBLE(1, "显示"),
    INVISIBLE(0, "隐藏"),
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
        return Arrays.stream(values()).map(VisibleStatusEnum::getCode).collect(Collectors.toList());
    }
}
