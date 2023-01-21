package top.sheepyu.module.system.controller.admin.config.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author ygq
 * @date 2023-01-21 15:27
 **/
@Data
@ApiModel("系统配置基本vo")
public class SystemConfigBaseVo {
    @ApiModelProperty("配置名称")
    @NotBlank(message = "配置名称不能为空")
    private String name;

    @ApiModelProperty("配置value")
    @NotBlank(message = "配置value不能为空")
    private String configValue;

    @ApiModelProperty("备注")
    private String remark;
}
