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
    private String name;
    private String type;
    private String comment;
    private Integer nullable;
    private Integer primaryKey;
    private Integer autoIncrement;
    private String javaType;
    private String javaField;
    private String dictType;
    private String example;
    private Integer createOperation;
    private Integer updateOperation;
    private Integer queryOperation;
    private String queryCondition;
    private Integer listOperationResult;
    private String showType;
    private Integer quickSearch;
    private String sort;
}
