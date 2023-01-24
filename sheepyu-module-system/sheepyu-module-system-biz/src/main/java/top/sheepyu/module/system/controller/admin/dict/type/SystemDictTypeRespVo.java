package top.sheepyu.module.system.controller.admin.dict.type;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author ygq
 * @date 2023-01-24 14:31
 **/
@Data
@ApiModel("字典类型响应vo")
public class SystemDictTypeRespVo {
    @ApiModelProperty("字典类型id")
    private Long id;

    @ApiModelProperty("字典类型名称")
    private String name;

    @ApiModelProperty("字典类型状态")
    private Integer visible;

    @ApiModelProperty("字典类型备注")
    private String remark;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("创建人")
    private String creator;
}
