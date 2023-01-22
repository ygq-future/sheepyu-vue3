package top.sheepyu.module.system.controller.admin.captcha.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author ygq
 * @date 2023-01-19 16:23
 **/
@Data
@AllArgsConstructor
@ApiModel("验证码vo")
public class CaptchaRespVo {
    @ApiModelProperty("base64图形")
    private String base64;
    @ApiModelProperty("数学运算表达式")
    private String arithmetic;
    @ApiModelProperty("redis key")
    private String key;
}
