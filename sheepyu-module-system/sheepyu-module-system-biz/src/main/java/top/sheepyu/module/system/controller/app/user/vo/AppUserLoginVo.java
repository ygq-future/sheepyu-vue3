package top.sheepyu.module.system.controller.app.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author ygq
 * @date 2023-01-30 17:31
 **/
@Data
@ApiModel("app用户登录vo")
public class AppUserLoginVo {
    @ApiModelProperty("账号,可以是username, mobile 或者 email")
    @NotBlank(message = "账号不能为空")
    private String login;

    @ApiModelProperty("密码")
    @NotBlank(message = "密码不能为空")
    private String password;
}
