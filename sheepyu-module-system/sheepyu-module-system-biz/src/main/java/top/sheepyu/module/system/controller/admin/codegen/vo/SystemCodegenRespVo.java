package top.sheepyu.module.system.controller.admin.codegen.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.sheepyu.module.system.dao.codegen.SystemCodegenColumn;

import java.util.Date;
import java.util.List;

/**
 * @author ygq
 * @date 2023-03-04 10:12
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("代码生成响应对象")
public class SystemCodegenRespVo extends SystemCodegenBaseVo {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("表名")
    private String tableName;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("所有列")
    private List<SystemCodegenColumn> columns;
}
