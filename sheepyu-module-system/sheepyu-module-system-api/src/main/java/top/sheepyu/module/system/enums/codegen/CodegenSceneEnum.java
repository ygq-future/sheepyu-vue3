package top.sheepyu.module.system.enums.codegen;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.sheepyu.module.common.common.IterableEnum;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ygq
 * @date 2023-03-04 11:06
 **/
@AllArgsConstructor
@Getter
public enum CodegenSceneEnum implements IterableEnum {
    ADMIN(1), //管理端
    APP(2), //用户端
    ;
    private final int code;

    @Override
    public List<Integer> list() {
        return Arrays.stream(values()).map(CodegenSceneEnum::getCode).collect(Collectors.toList());
    }
}
