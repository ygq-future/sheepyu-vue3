package top.sheepyu.module.system.controller.app.user.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * @author ygq
 * @date 2023-01-22 11:33
 **/
@Data
@ApiModel("邮箱登录vo")
public class EmailLoginVo {
    @Email(message = "邮箱格式错误")
    @NotBlank(message = "邮箱不能为空")
    private String email;
    @NotBlank(message = "验证码不能为空")
    private String code;
}
