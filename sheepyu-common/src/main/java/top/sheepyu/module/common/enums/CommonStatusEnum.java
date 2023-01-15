package top.sheepyu.module.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用状态枚举
 *
 * @author 芋道源码
 */
@Getter
@AllArgsConstructor
public enum CommonStatusEnum {
    ENABLE(0, "开启"),
    DISABLE(1, "关闭");

    /**
     * 状态值
     */
    private final Integer code;
    /**
     * 状态名
     */
    private final String desc;
}
