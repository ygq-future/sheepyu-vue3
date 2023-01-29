package top.sheepyu.module.system.enums.log;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.sheepyu.module.common.common.IterableEnum;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ygq
 * @date 2023-01-29 16:54
 **/
@AllArgsConstructor
@Getter
public enum ApiLogStatusEnum implements IterableEnum {
    NORMAL(1), //正常
    EXCEPTION(0), //异常
    ;
    private final int code;

    @Override
    public List<Integer> list() {
        return Arrays.stream(values()).map(ApiLogStatusEnum::getCode).collect(Collectors.toList());
    }
}
