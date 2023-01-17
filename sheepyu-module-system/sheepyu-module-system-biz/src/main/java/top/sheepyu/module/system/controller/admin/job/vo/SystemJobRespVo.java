package top.sheepyu.module.system.controller.admin.job.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author ygq
 * @date 2023-01-17 10:39
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("系统任务响应vo")
public class SystemJobRespVo extends SystemJobBaseVo {
    @ApiModelProperty("任务id")
    private Long id;
    @ApiModelProperty("处理器名称")
    private String handlerName;
    @ApiModelProperty("创建者")
    private String creator;
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
