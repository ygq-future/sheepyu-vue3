package top.sheepyu.module.system.controller.admin.dict.data;


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
@ApiModel("字典数据修改vo")
public class SystemDictDataUpdateVo extends SystemDictDataBaseVo {
    @ApiModelProperty("字典id")
    @NotNull(message = "字典id不能为空")
    private Long id;
}
