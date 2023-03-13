package top.sheepyu.module.system.controller.admin.post.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author ygq
 * @date 2023-01-29 18:10
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("系统职位响应vo")
public class SystemPostRespVo extends SystemPostBaseVo {
    @ApiModelProperty("职位id")
    private Long id;

    @ApiModelProperty("创建人")
    private String creator;

    @ApiModelProperty("创建时间")
    private Date createTime;
}
