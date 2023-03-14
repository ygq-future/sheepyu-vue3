package top.sheepyu.module.system.controller.admin.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.sheepyu.module.common.annotations.InEnum;
import top.sheepyu.module.common.enums.UserTypeEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author ygq
 * @date 2023-01-18 15:03
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("系统用户创建vo")
public class SystemUserCreateVo extends SystemUserBaseVo {
    @ApiModelProperty("用户名")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @ApiModelProperty("密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty("用户类型")
    @NotNull(message = "用户类型不能为空")
    @InEnum(UserTypeEnum.class)
    private Integer type;
}
