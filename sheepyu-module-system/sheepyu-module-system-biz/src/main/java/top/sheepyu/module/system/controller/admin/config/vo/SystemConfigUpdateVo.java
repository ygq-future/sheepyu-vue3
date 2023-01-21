package top.sheepyu.module.system.controller.admin.config.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author ygq
 * @date 2023-01-21 15:29
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("系统配置修改vo")
public class SystemConfigUpdateVo extends SystemConfigBaseVo {
    @ApiModelProperty("系统配置id")
    @NotNull(message = "系统配置id不能为空")
    private Long id;
}
