package top.sheepyu.module.system.controller.admin.log.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.sheepyu.framework.log.core.enums.OperateTypeEnum;
import top.sheepyu.module.common.annotations.InEnum;
import top.sheepyu.module.common.common.PageParam;
import top.sheepyu.module.system.enums.log.ApiLogProcessEnum;
import top.sheepyu.module.system.enums.log.ApiLogStatusEnum;

import javax.validation.constraints.Size;

/**
 * @author ygq
 * @date 2023-01-23 20:05
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("系统API日志查询vo")
public class SystemApiLogQueryVo extends PageParam {
    @ApiModelProperty("操作类型")
    @InEnum(OperateTypeEnum.class)
    private Integer type;

    @ApiModelProperty("方法执行时间")
    @Size(min = 2, max = 2, message = "时间查询参数长度错误")
    private Integer[] durations;

    @ApiModelProperty("是否有异常")
    @InEnum(ApiLogStatusEnum.class)
    private Integer status;

    @ApiModelProperty("处理状态")
    @InEnum(ApiLogProcessEnum.class)
    private Integer processStatus;
}
