package top.sheepyu.module.common.enums;

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
public enum CommonStatusEnum implements IterableEnum {
    //为了区分语义所以定义了多个一样code的枚举
    //也就说了这个枚举类本来就是全局系统状态, 用来管理所有状态,
    // 所以定义1标识良好的状态,0标识不好的状态
    OPEN(1, "开启"),
    SUCCESS(1, "成功"),
    SHOW(1, "显示"),
    TRUE(1, "正确"),

    CLOSE(0, "关闭"),
    FAILED(0, "失败"),
    HIDE(0, "隐藏"),
    FALSE(0, "错误"),
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
        return Arrays.stream(values()).map(CommonStatusEnum::getCode).collect(Collectors.toList());
    }
}
