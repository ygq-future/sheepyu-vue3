package top.sheepyu.module.system.controller.admin.permission.menu;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.sheepyu.module.common.annotations.InEnum;
import top.sheepyu.module.common.enums.CommonStatusEnum;
import top.sheepyu.module.system.enums.menu.MenuTypeEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author ygq
 * @date 2023-01-29 18:10
 **/
@Data
@ApiModel("系统菜单基本vo")
public class SystemMenuBaseVo {
    @ApiModelProperty("菜单名称")
    @NotBlank(message = "菜单名称不能为空")
    private String name;

    @ApiModelProperty("菜单权限")
    private String permission;

    @ApiModelProperty("菜单类型(目录, 菜单, 按钮)")
    @NotNull(message = "菜单类型不能为空")
    @InEnum(MenuTypeEnum.class)
    private Integer type;

    @ApiModelProperty("菜单排序")
    @NotNull(message = "菜单排序不能为空")
    private Integer sort;

    @ApiModelProperty("父菜单")
    @NotNull(message = "父菜单不能为空")
    private Long parentId;

    @ApiModelProperty("菜单路由地址")
    private String path;

    @ApiModelProperty("菜单图标")
    private String icon;

    @ApiModelProperty("组件路径")
    private String component;

    @ApiModelProperty("启用状态")
    @NotNull(message = "启用状态不能为空")
    @InEnum(CommonStatusEnum.class)
    private Integer status;

    @ApiModelProperty("是否缓存")
    @InEnum(CommonStatusEnum.class)
    private Integer keepAlive;
}
