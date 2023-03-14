package top.sheepyu.module.system.controller.admin.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author ygq
 * @date 2023-01-18 17:06
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("系统用户响应vo")
public class SystemUserRespVo extends SystemUserBaseVo {
    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("用户类型")
    private Integer type;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("上一次登录ip")
    private String loginIp;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("岗位名称")
    private String postNames;

    @ApiModelProperty("上一次登录时间")
    private Date loginTime;

    @ApiModelProperty("注册时间")
    private Date createTime;
}
