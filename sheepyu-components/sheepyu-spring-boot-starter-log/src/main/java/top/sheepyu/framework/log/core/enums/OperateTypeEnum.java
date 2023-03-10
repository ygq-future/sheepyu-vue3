package top.sheepyu.framework.log.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.sheepyu.framework.log.core.annotations.RecordLog;
import top.sheepyu.module.common.common.IterableEnum;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 操作日志的操作类型
 */
@Getter
@AllArgsConstructor
public enum OperateTypeEnum implements IterableEnum {
    /**
     * 绝大多数情况下，不会记录查询动作，因为过于大量显得没有意义。
     * 在有需要的时候，通过声明 {@link RecordLog} 注解来记录
     */
    GET(1, "查询"),
    CREATE(2, "新增"),
    UPDATE(3, "修改"),
    DELETE(4, "删除"),
    EXPORT(5, "导出"),
    IMPORT(6, "导入"),
    GENERATE(7, "代码生成"),
    DOWNLOAD(8, "下载"),
    OTHER(0, "其他"),
    ;

    /**
     * 类型
     */
    private final int code;
    private final String desc;

    @Override
    public List<Integer> list() {
        return Arrays.stream(values()).map(OperateTypeEnum::getCode).collect(Collectors.toList());
    }
}
