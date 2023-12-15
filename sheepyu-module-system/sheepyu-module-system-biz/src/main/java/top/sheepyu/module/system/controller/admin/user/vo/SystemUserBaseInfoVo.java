package top.sheepyu.module.system.controller.admin.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author ygq
 * @date 2023-01-18 15:03
 **/
@Data
@ApiModel("系统用户基本信息vo")
public class SystemUserBaseInfoVo {
    @ApiModelProperty("昵称")
    @NotEmpty(message = "昵称不能为空")
    private String nickname;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("电话号码")
    private String mobile;

    @ApiModelProperty("头像url")
    private String avatar;
}
