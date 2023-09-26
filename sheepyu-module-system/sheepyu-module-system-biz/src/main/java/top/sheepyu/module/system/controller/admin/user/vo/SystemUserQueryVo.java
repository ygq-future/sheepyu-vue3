package top.sheepyu.module.system.controller.admin.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;
import top.sheepyu.module.common.annotations.InEnum;
import top.sheepyu.module.common.common.PageParam;
import top.sheepyu.module.common.enums.CommonStatusEnum;

import java.util.Date;

import static top.sheepyu.module.common.constants.CommonConstants.DATE_TIME_FORMAT;
import static top.sheepyu.module.common.enums.UserTypeEnum.ADMIN;

/**
 * @author ygq
 * @date 2023-01-17 10:37
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("系统用户分页查询vo")
public class SystemUserQueryVo extends PageParam {
    @ApiModelProperty("用户状态")
    @InEnum(CommonStatusEnum.class)
    private Integer status;

    @ApiModelProperty("用户类型")
    private Integer type = ADMIN.getCode();

    @ApiModelProperty("部门id")
    private Long deptId;

    @ApiModelProperty("最近登录时间")
    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    private Date[] loginTimes;

    @ApiModelProperty("最近创建时间")
    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    private Date[] createTimes;
}
