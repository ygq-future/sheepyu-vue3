package top.sheepyu.module.system.controller.admin.dict.type;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author ygq
 * @date 2023-01-24 14:28
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("字典类型修改vo")
public class SystemDictTypeUpdateVo extends SystemDictTypeBaseVo {
    @ApiModelProperty("字典类型id")
    @NotNull(message = "字典类型id不能为空")
    private Long id;
}
