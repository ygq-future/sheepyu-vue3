package top.sheepyu.module.system.controller.admin.job.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.sheepyu.module.common.annotations.InEnum;
import top.sheepyu.module.common.common.PageParam;
import top.sheepyu.module.system.enums.job.JobLogStatusEnum;

/**
 * @author ygq
 * @date 2023-01-17 10:37
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("定时任务日志表分页查询vo")
public class SystemJobLogQueryVo extends PageParam {
    @ApiModelProperty("任务编号")
    private Long jobId;

    @ApiModelProperty("执行时长")
    private Integer[] durations;

    @ApiModelProperty("任务状态(成功,失败)")
    @InEnum(JobLogStatusEnum.class)
    private Integer status;
}
