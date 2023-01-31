package top.sheepyu.module.system.controller.admin.config.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author ygq
 * @date 2023-01-21 15:35
 **/
@Data
@ApiModel("系统配置响应vo")
public class SystemConfigRespVo {
    @ApiModelProperty("系统配置id")
    private Long id;

    @ApiModelProperty("配置名称")
    private String name;

    @ApiModelProperty("配置key")
    private String configKey;

    @ApiModelProperty("配置value")
    private String configValue;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建者")
    private String creator;

    @ApiModelProperty("创建时间")
    private Date createTime;
}
