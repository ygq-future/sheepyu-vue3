package top.sheepyu.module.system.controller.admin.job.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.sheepyu.module.common.annotations.InEnum;
import top.sheepyu.module.common.common.PageParam;
import top.sheepyu.module.system.enums.job.JobStatusEnum;

/**
 * @author ygq
 * @date 2023-01-17 10:37
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("系统任务分页查询vo")
public class SystemJobQueryVo extends PageParam {
    @ApiModelProperty("任务状态")
    @InEnum(JobStatusEnum.class)
    private Integer status;
}
