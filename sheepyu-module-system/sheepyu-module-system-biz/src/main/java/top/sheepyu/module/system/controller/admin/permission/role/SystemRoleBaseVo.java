package top.sheepyu.module.system.controller.admin.permission.role;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author ygq
 * @date 2023-01-29 18:10
 **/
@Data
@ApiModel("系统角色基本vo")
public class SystemRoleBaseVo {
    @ApiModelProperty("角色名称")
    @NotBlank(message = "角色名称不能为空")
    private String name;

    @ApiModelProperty("角色code")
    @NotBlank(message = "角色code不能为空")
    private String code;

    @ApiModelProperty("角色排序")
    @NotNull(message = "角色排序不能为空")
    private Integer sort;

    @ApiModelProperty("备注")
    private String remark;
}
