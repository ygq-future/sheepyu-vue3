package top.sheepyu.module.system.controller.admin.dict.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.sheepyu.module.common.annotations.InEnum;
import top.sheepyu.module.common.enums.CommonStatusEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author ygq
 * @date 2023-01-24 14:24
 **/
@Data
@ApiModel("字典数据基本vo")
public class SystemDictDataBaseVo {
    @ApiModelProperty("字典排序")
    private Integer sort;

    @ApiModelProperty("字典标签")
    @NotBlank(message = "字典标签不能为空")
    private String label;

    @ApiModelProperty("字典状态")
    @NotNull(message = "字典状态不能为空")
    @InEnum(CommonStatusEnum.class)
    private Integer status;

    @ApiModelProperty("颜色类型")
    private String colorType;

    @ApiModelProperty("字典备注")
    private String remark;
}
