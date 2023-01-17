package top.sheepyu.module.system.controller.admin.job.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * @author ygq
 * @date 2023-01-16 18:02
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("系统任务创建vo")
public class SystemJobCreateVo extends SystemJobBaseVo {
    @ApiModelProperty("处理器名称")
    @NotBlank(message = "处理器名称不能为空")
    private String handlerName;
}
