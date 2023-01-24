package top.sheepyu.module.system.controller.admin.log.access;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.sheepyu.module.common.annotations.InEnum;
import top.sheepyu.module.common.common.PageParam;
import top.sheepyu.module.common.enums.UserTypeEnum;
import top.sheepyu.module.system.enums.LoginLogTypeEnum;
import top.sheepyu.module.system.enums.LoginResultEnum;

/**
 * @author ygq
 * @date 2023-01-23 20:05
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("系统访问日志查询vo")
public class SystemAccessLogQueryVo extends PageParam {
    @ApiModelProperty("用户类型")
    @InEnum(UserTypeEnum.class)
    private Integer userType;

    @ApiModelProperty("登录类型")
    @InEnum(LoginLogTypeEnum.class)
    private Integer loginType;

    @ApiModelProperty("登录结果")
    @InEnum(LoginResultEnum.class)
    private Integer loginResult;
}
