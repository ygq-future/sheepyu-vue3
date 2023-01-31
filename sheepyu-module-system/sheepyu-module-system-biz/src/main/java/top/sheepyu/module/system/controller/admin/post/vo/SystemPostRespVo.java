package top.sheepyu.module.system.controller.admin.post.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author ygq
 * @date 2023-01-29 18:10
 **/
@Data
@ApiModel("系统职位响应vo")
public class SystemPostRespVo {
    @ApiModelProperty("职位id")
    private Long id;

    @ApiModelProperty("职位名称")
    private String name;

    @ApiModelProperty("职位排序")
    private Integer sort;

    @ApiModelProperty("创建人")
    private String creator;

    @ApiModelProperty("创建时间")
    private Date createTime;
}
