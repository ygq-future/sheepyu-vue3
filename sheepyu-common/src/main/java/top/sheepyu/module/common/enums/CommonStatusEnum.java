package top.sheepyu.module.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用状态枚举
 */
@Getter
@AllArgsConstructor
public enum CommonStatusEnum {
    //为了区分语义所以定义了多个一样code的枚举
    //也就说了这个枚举类本来就是全局系统状态, 用来管理所有状态,
    // 所以定义0标识良好的状态,1标识不好的状态
    OPEN(0, "开启"),
    SUCCESS(0, "成功"),
    SHOW(0, "显示"),

    CLOSE(1, "关闭"),
    FAILED(1, "失败"),
    HIDE(1, "隐藏"),
    ;

    /**
     * 状态值
     */
    private final Integer code;
    /**
     * 状态名
     */
    private final String desc;
}
