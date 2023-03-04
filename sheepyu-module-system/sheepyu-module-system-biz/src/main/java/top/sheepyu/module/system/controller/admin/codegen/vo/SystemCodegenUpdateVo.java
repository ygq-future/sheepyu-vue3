package top.sheepyu.module.system.controller.admin.codegen.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.sheepyu.module.common.annotations.InEnum;
import top.sheepyu.module.system.dao.codegen.SystemCodegenColumn;
import top.sheepyu.module.system.enums.codegen.CodegenSceneEnum;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author ygq
 * @date 2023-03-04 10:12
 **/
@Data
@ApiModel("代码生成修改对象")
public class SystemCodegenUpdateVo {
    @ApiModelProperty("id")
    @NotNull(message = "id不能为空")
    private Long id;

    @ApiModelProperty("场景")
    @NotNull(message = "场景不能为空")
    @InEnum(CodegenSceneEnum.class)
    private Integer scene;

    @ApiModelProperty("表名")
    @NotEmpty(message = "表名不能为空")
    private String tableName;

    @ApiModelProperty("表注释")
    @NotEmpty(message = "表注释不能为空")
    private String tableComment;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("模块名称")
    @NotEmpty(message = "模块名称不能为空")
    private String moduleName;

    @ApiModelProperty("业务名称")
    @NotEmpty(message = "业务名称不能为空")
    private String businessName;

    @ApiModelProperty("类名")
    @NotEmpty(message = "类名不能为空")
    private String className;

    @ApiModelProperty("类注释")
    private String classComment;

    @ApiModelProperty("作者")
    @NotEmpty(message = "作者不能为空")
    private String author;

    @ApiModelProperty("所有列")
    @NotEmpty(message = "列不能为空")
    private List<SystemCodegenColumn> columns;
}
