package top.sheepyu.module.system.controller.admin.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author ygq
 * @date 2023-01-18 15:03
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("系统用户修改vo")
public class SystemUserUpdateVo extends SystemUserBaseVo {
    @ApiModelProperty("用户id")
    @NotNull(message = "用户id不能为空")
    private Long id;
}
