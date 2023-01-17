package top.sheepyu.module.system.controller.admin.job.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author ygq
 * @date 2023-01-16 18:01
 **/
@Data
@ApiModel("系统任务基本vo")
public class SystemJobBaseVo {
    @ApiModelProperty("任务名称")
    @NotBlank(message = "任务名称不能为空")
    private String name;

    @ApiModelProperty("处理器参数")
    private String handlerParam;

    @ApiModelProperty("cron表达式")
    @NotBlank(message = "cron表达式不能为空")
    private String cron;

    @ApiModelProperty("重试次数")
    private Integer retryCount;

    @ApiModelProperty("重试间隔(秒)")
    private Integer retryInterval;
}
