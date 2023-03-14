package top.sheepyu.module.system.controller.app.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author ygq
 * @date 2023-03-14 15:48
 **/
@Data
@ApiModel("用户注册vo")
public class AppUserRegisterVo {
    @ApiModelProperty("用户邮箱")
    private String email;

    @ApiModelProperty("用户手机")
    private String mobile;

    @ApiModelProperty("用户昵称")
    private String nickname;

    @ApiModelProperty("用户密码")
    @NotEmpty(message = "用户密码不能为空")
    private String password;
}
