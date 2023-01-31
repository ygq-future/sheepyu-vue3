package top.sheepyu.module.system.controller.app.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author ygq
 * @date 2023-01-30 17:29
 **/
@Data
@ApiModel("app用户响应vo")
public class AppUserRespVo {
    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("电话号码")
    private String mobile;

    @ApiModelProperty("头像url")
    private String avatar;

    @ApiModelProperty("上一次登录ip")
    private String loginIp;

    @ApiModelProperty("上一次登录时间")
    private Date loginTime;
}
