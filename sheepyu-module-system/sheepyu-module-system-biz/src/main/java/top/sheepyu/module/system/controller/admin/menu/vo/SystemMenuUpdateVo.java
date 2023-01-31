package top.sheepyu.module.system.controller.admin.menu.vo;

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
@ApiModel("系统菜单修改vo")
public class SystemMenuUpdateVo extends SystemMenuBaseVo {
    @ApiModelProperty("菜单id")
    @NotNull(message = "菜单id不能为空")
    private Long id;
}
