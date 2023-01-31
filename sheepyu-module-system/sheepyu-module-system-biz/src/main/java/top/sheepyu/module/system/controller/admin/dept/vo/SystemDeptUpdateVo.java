package top.sheepyu.module.system.controller.admin.dept.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author ygq
 * @date 2023-01-29 18:14
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("系统部门修改vo")
public class SystemDeptUpdateVo extends SystemDeptBaseVo {
    @ApiModelProperty("id")
    @NotNull(message = "id不能为空")
    private Long id;
}
