package top.sheepyu.module.system.controller.admin.job.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author ygq
 * @date 2023-01-23 20:45
 **/
@Data
@ApiModel("系统定时任务日志响应vo")
public class SystemJobLogRespVo {
    @ApiModelProperty("日志id")
    private Long id;

    @ApiModelProperty("定时任务id")
    private Long jobId;

    @ApiModelProperty("处理器名称")
    private String handlerName;

    @ApiModelProperty("处理器参数")
    private String handlerParam;

    @ApiModelProperty("重试次数")
    private Integer retryCount;

    @ApiModelProperty("执行开始时间")
    private Date beginTime;

    @ApiModelProperty("执行结束时间")
    private Date endTime;

    @ApiModelProperty("执行持续时间")
    private Integer duration;

    @ApiModelProperty("执行状态")
    private Integer status;

    @ApiModelProperty("返回结果")
    private String result;
}
