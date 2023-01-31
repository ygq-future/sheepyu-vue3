package top.sheepyu.module.system.controller.admin.post.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author ygq
 * @date 2023-01-29 18:10
 **/
@Data
@ApiModel("系统职位基本vo")
public class SystemPostBaseVo {
    @ApiModelProperty("职位名称")
    @NotBlank(message = "职位名称不能为空")
    private String name;

    @ApiModelProperty("职位排序")
    @NotNull(message = "职位排序不能为空")
    private Integer sort;
}
