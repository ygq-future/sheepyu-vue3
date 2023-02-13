package top.sheepyu.module.system.controller.admin.dict.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ygq
 * @date 2023-01-24 14:31
 **/
@Data
@ApiModel("字典数据响应vo")
public class SystemDictDataRespVo {
    @ApiModelProperty("字典id")
    private Long id;

    @ApiModelProperty("字典类型")
    private String dictType;

    @ApiModelProperty("字典排序")
    private Integer sort;

    @ApiModelProperty("字典标签")
    private String label;

    @ApiModelProperty("字典值")
    private String value;

    @ApiModelProperty("字典状态")
    private Integer status;

    @ApiModelProperty("字典显示的组件")
    private String colorType;

    @ApiModelProperty("字典备注")
    private String remark;
}
