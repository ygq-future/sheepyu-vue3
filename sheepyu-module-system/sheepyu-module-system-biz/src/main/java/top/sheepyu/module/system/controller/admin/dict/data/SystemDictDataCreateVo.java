package top.sheepyu.module.system.controller.admin.dict.data;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author ygq
 * @date 2023-01-24 14:28
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("字典数据创建vo")
public class SystemDictDataCreateVo extends SystemDictDataBaseVo {
    @ApiModelProperty("字典标签")
    @NotNull(message = "字典类型不能为空")
    private Long dictTypeId;

    @ApiModelProperty("字典值")
    @NotBlank(message = "字典值不能为空")
    private String value;
}
