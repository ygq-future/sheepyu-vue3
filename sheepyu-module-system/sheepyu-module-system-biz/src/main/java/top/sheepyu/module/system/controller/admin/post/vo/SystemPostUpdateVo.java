package top.sheepyu.module.system.controller.admin.post.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author ygq
 * @date 2023-01-29 18:14
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("系统职位修改vo")
public class SystemPostUpdateVo extends SystemPostBaseVo {
    @ApiModelProperty("职位id")
    @NotNull(message = "职位id不能为空")
    private Long id;
}
