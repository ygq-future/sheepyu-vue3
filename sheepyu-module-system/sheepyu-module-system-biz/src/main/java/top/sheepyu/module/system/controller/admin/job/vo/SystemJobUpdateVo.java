package top.sheepyu.module.system.controller.admin.job.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author ygq
 * @date 2023-01-16 18:03
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("系统任务修改vo")
public class SystemJobUpdateVo extends SystemJobBaseVo {
    @ApiModelProperty("任务id")
    @NotNull(message = "任务id不能为空")
    private Long id;
}
