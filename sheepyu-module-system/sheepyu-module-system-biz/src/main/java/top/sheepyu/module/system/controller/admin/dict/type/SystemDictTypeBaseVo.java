package top.sheepyu.module.system.controller.admin.dict.type;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.sheepyu.module.common.annotations.InEnum;
import top.sheepyu.module.common.enums.status.VisibleStatusEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author ygq
 * @date 2023-01-24 14:24
 **/
@Data
@ApiModel("字典类型基本vo")
public class SystemDictTypeBaseVo {
    @ApiModelProperty("字典类型名称")
    @NotBlank(message = "字典类型名称不能为空")
    private String name;

    @ApiModelProperty("字典类型状态")
    @NotNull(message = "字典类型状态不能为空")
    @InEnum(VisibleStatusEnum.class)
    private Integer visible;

    @ApiModelProperty("字典备注")
    private String remark;
}
