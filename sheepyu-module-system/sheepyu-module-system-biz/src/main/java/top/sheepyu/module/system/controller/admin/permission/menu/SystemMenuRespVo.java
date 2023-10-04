package top.sheepyu.module.system.controller.admin.permission.menu;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author ygq
 * @date 2023-01-29 18:10
 **/
@Data
@ApiModel("系统菜单响应vo")
public class SystemMenuRespVo {
    @ApiModelProperty("菜单id")
    private Long id;

    @ApiModelProperty("菜单名称")
    private String name;

    @ApiModelProperty("菜单权限")
    private String permission;

    @ApiModelProperty("菜单类型(目录, 菜单, 按钮)")
    private Integer type;

    @ApiModelProperty("菜单排序")
    private Integer sort;

    @ApiModelProperty("父菜单")
    private Long parentId;

    @ApiModelProperty("菜单路由地址")
    private String path;

    @ApiModelProperty("菜单图标")
    private String icon;

    @ApiModelProperty("组件路径")
    private String component;

    @ApiModelProperty("启用状态")
    private Integer status;

    @ApiModelProperty("是否缓存")
    private Integer keepAlive;

    @ApiModelProperty("子菜单")
    private List<SystemMenuRespVo> children;

    @ApiModelProperty("是否还有子菜单")
    private Boolean hasChildren;
}
