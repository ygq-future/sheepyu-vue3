package top.sheepyu.module.system.controller.admin.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author ygq
 * @date 2023-01-18 15:12
 **/
@Data
@ApiModel("系统用户登录vo")
public class SystemUserLoginVo {
    @ApiModelProperty("用户名")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @ApiModelProperty("密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty("验证码key")
    @NotBlank(message = "验证码key不能为空")
    private String key;

    @ApiModelProperty("验证码")
    @NotBlank(message = "验证码不能为空")
    private String code;
}
