package top.sheepyu.module.system.controller.admin.codegen.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.sheepyu.module.common.annotations.InEnum;
import top.sheepyu.module.system.enums.codegen.CodegenSceneEnum;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author ygq
 * @date 2023-03-12 21:21
 **/
@Data
@ApiModel("代码生成基本Vo")
public class SystemCodegenBaseVo {
    @ApiModelProperty("场景")
    @NotNull(message = "场景不能为空")
    @InEnum(CodegenSceneEnum.class)
    private Integer scene;

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

    @ApiModelProperty("列表操作")
    @NotNull(message = "列表操作不能为空")
    private Boolean requireList;

    @ApiModelProperty("分页操作")
    @NotNull(message = "分页操作不能为空")
    private Boolean requirePage;

    @ApiModelProperty("导出操作")
    @NotNull(message = "导出操作不能为空")
    private Boolean requireExport;

    @ApiModelProperty("导入操作")
    @NotNull(message = "导入操作不能为空")
    private Boolean requireImport;
}
