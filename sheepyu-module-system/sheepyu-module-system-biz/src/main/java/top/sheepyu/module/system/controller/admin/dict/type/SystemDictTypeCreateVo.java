package top.sheepyu.module.system.controller.admin.dict.type;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * @author ygq
 * @date 2023-01-24 14:28
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("字典类型创建vo")
public class SystemDictTypeCreateVo extends SystemDictTypeBaseVo {
    @ApiModelProperty("字典类型")
    @NotBlank(message = "字典类型不能为空")
    private String type;
}
