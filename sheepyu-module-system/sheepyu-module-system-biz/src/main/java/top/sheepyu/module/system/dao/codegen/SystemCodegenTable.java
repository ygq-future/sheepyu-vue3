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
public class SystemCodegenTable extends BaseModel {
    private Long id;
    private Integer scene;
    private String tableName;
    private String tableComment;
    private String remark;
    private String moduleName;
    private String businessName;
    private String className;
    private String classComment;
    private String author;

}
