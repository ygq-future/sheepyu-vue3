package top.sheepyu.module.system.controller.admin.dept.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.sheepyu.module.common.annotations.InEnum;
import top.sheepyu.module.system.enums.dept.DeptTypeEnum;

import javax.validation.constraints.NotNull;

/**
 * @author ygq
 * @date 2023-01-29 18:14
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("系统部门创建vo")
public class SystemDeptCreateVo extends SystemDeptBaseVo {
    @ApiModelProperty("父部门id")
    @NotNull(message = "父部门id不能为空")
    private Long parentId;

    @ApiModelProperty("部门类型")
    @NotNull(message = "部门类型不能为空")
    @InEnum(DeptTypeEnum.class)
    private Integer type;
}
