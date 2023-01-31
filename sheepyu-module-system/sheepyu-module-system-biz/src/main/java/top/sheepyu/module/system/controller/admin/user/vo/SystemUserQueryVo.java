package top.sheepyu.module.system.controller.admin.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.sheepyu.module.common.annotations.InEnum;
import top.sheepyu.module.common.common.PageParam;
import top.sheepyu.module.common.enums.CommonStatusEnum;

import javax.validation.constraints.Size;
import java.util.Date;

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

    @ApiModelProperty("部门id")
    private Integer deptId;

    @ApiModelProperty("最近创建时间")
    @Size(min = 2, max = 2, message = "最近创建时间参数不正确")
    private Date[] createTimes;
}
