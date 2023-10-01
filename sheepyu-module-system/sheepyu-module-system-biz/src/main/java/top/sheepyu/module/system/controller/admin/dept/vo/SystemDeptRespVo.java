package top.sheepyu.module.system.controller.admin.dept.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author ygq
 * @date 2023-01-29 18:10
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("系统部门响应vo")
public class SystemDeptRespVo extends SystemDeptBaseVo {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("父部门id")
    private Long parentId;

    @ApiModelProperty("部门类型")
    private Integer type;

    @ApiModelProperty("是否禁用")
    private Boolean disabled;

    @ApiModelProperty("子部门")
    private List<SystemDeptRespVo> children;
}
