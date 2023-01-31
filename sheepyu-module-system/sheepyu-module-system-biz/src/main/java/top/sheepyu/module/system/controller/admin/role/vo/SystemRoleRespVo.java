package top.sheepyu.module.system.controller.admin.role.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.sheepyu.module.system.controller.admin.menu.vo.SystemMenuRespVo;

import java.util.List;

/**
 * @author ygq
 * @date 2023-01-29 18:10
 **/
@Data
@ApiModel("系统角色响应vo")
public class SystemRoleRespVo {
    @ApiModelProperty("角色id")
    private Long id;

    @ApiModelProperty("角色名称")
    private String name;

    @ApiModelProperty("角色code")
    private String code;

    @ApiModelProperty("角色排序")
    private Integer sort;

    @ApiModelProperty("角色状态")
    private Integer status;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("角色的权限")
    private List<SystemMenuRespVo> menus;
}
