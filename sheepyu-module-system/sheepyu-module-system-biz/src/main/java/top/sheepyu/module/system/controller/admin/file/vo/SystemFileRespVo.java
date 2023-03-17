package top.sheepyu.module.system.controller.admin.file.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author ygq
 * @date 2023-01-27 11:35
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("系统文件响应vo")
public class SystemFileRespVo extends SystemFileBaseVo {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("创建人")
    private String creator;

    @ApiModelProperty("创建时间")
    private Date createTime;
}
