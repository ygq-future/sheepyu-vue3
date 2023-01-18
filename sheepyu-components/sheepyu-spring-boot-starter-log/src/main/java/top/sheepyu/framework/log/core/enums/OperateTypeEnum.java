package top.sheepyu.framework.log.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.sheepyu.framework.log.core.annotations.RecordLog;

/**
 * 操作日志的操作类型
 */
@Getter
@AllArgsConstructor
public enum OperateTypeEnum {
    /**
     * 查询
     * <p>
     * 绝大多数情况下，不会记录查询动作，因为过于大量显得没有意义。
     * 在有需要的时候，通过声明 {@link RecordLog} 注解来记录
     */
    GET(1, "查询"),
    /**
     * 新增
     */
    CREATE(2, "新增"),
    /**
     * 修改
     */
    UPDATE(3, "修改"),
    /**
     * 删除
     */
    DELETE(4, "删除"),
    /**
     * 其它
     * <p>
     * 在无法归类时，可以选择使用其它。因为还有操作名可以进一步标识
     */
    OTHER(0, "其他");

    /**
     * 类型
     */
    private final int code;
    private final String desc;
}
