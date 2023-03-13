package top.sheepyu.module.system.controller.admin.permission.role;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.sheepyu.module.system.controller.admin.permission.menu.SystemMenuRespVo;

import java.util.List;

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

    @ApiModelProperty("角色的权限")
    private List<SystemMenuRespVo> menus;
}
