package top.sheepyu.module.system.controller.admin.config.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * @author ygq
 * @date 2023-01-21 15:30
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("系统配置创建vo")
public class SystemConfigCreateVo extends SystemConfigBaseVo {
    @ApiModelProperty("配置key")
    @NotBlank(message = "配置key不能为空")
    private String configKey;
}
