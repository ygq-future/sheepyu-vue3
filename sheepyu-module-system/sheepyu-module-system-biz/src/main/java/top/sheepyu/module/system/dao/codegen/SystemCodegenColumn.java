package top.sheepyu.module.system.dao.codegen;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import top.sheepyu.framework.mybatisplus.core.model.BaseModel;

/**
 * @author ygq
 * @date 2023-03-01 20:37
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SystemCodegenColumn extends BaseModel {
    private Long id;
    private Long tableId;
    private String name;
    private String type;
    private String comment;
    private Boolean nullable;
    private Boolean primaryKey;
    private Boolean autoIncrement;
    private String javaType;
    private String javaField;
    private String dictType;
    private String example;
    private Boolean createOperation;
    private Boolean updateOperation;
    private Boolean queryOperation;
    private String queryCondition;
    private Boolean listOperationResult;
    private String formShowType;
    private Boolean quickSearch;
    private String sort;
}
