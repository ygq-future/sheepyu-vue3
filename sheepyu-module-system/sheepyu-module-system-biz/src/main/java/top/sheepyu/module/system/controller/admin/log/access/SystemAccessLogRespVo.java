package top.sheepyu.module.system.controller.admin.log.access;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author ygq
 * @date 2023-01-23 20:09
 **/
@Data
@ApiModel("系统访问日志响应vo")
public class SystemAccessLogRespVo {
    @ApiModelProperty("日志id")
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户类型")
    private Integer userType;

    @ApiModelProperty("登录类型")
    private Integer loginType;

    @ApiModelProperty("登录结果")
    private Integer loginResult;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("用户昵称")
    private String nickname;

    @ApiModelProperty("用户登录ip")
    private String userIp;

    @ApiModelProperty("用户代理")
    private String userAgent;

    @ApiModelProperty("登录时间")
    private LocalDateTime createTime;
}
