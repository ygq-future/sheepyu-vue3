package top.sheepyu.module.system.controller.admin.dict.type;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author ygq
 * @date 2023-01-24 14:31
 **/
@Data
@ApiModel("字典类型响应vo")
public class SystemDictTypeRespVo {
    @ApiModelProperty("字典类型id")
    private Long id;

    @ApiModelProperty("字典类型")
    private String type;

    @ApiModelProperty("字典类型名称")
    private String name;

    @ApiModelProperty("字典类型状态")
    private Integer status;

    @ApiModelProperty("字典类型备注")
    private String remark;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("创建人")
    private String creator;
}
