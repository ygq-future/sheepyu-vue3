package top.sheepyu.module.system.controller.admin.role.vo;

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
@ApiModel("系统角色修改vo")
public class SystemRoleUpdateVo extends SystemRoleBaseVo {
    @ApiModelProperty("角色id")
    @NotNull(message = "角色id不能为空")
    private Long id;
}
