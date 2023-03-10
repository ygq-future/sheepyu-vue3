package top.sheepyu.module.system.controller.admin.codegen.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.sheepyu.module.system.dao.codegen.SystemCodegenColumn;

import java.util.Date;
import java.util.List;

/**
 * @author ygq
 * @date 2023-03-04 10:12
 **/
@Data
@ApiModel("代码生成响应对象")
public class SystemCodegenRespVo {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("场景 CodegenSceneEnum")
    private Integer scene;

    @ApiModelProperty("表名")
    private String tableName;

    @ApiModelProperty("表注释")
    private String tableComment;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("模块名称")
    private String moduleName;

    @ApiModelProperty("业务名称")
    private String businessName;

    @ApiModelProperty("类名")
    private String className;

    @ApiModelProperty("类注释")
    private String classComment;

    @ApiModelProperty("作者")
    private String author;

    @ApiModelProperty("列表操作")
    private Boolean requireList;

    @ApiModelProperty("分页操作")
    private Boolean requirePage;

    @ApiModelProperty("导出操作")
    private Boolean requireExport;

    @ApiModelProperty("导入操作")
    private Boolean requireImport;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("所有列")
    private List<SystemCodegenColumn> columns;
}
