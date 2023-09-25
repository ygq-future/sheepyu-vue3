package top.sheepyu.module.system.controller.admin.permission.role;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ygq
 * @date 2023-01-29 18:10
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("系统角色响应vo")
public class SystemRoleRespVo extends SystemRoleBaseVo {
    @ApiModelProperty("角色id")
    private Long id;

    @ApiModelProperty("创建者")
    private String creator;
}
