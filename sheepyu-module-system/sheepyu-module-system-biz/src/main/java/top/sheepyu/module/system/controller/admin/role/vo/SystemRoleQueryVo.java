package top.sheepyu.module.system.controller.admin.role.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.sheepyu.module.common.annotations.InEnum;
import top.sheepyu.module.common.common.PageParam;
import top.sheepyu.module.common.enums.CommonStatusEnum;

/**
 * @author ygq
 * @date 2023-01-30 16:01
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("系统角色查询vo")
public class SystemRoleQueryVo extends PageParam {
    @ApiModelProperty("角色状态")
    @InEnum(CommonStatusEnum.class)
    private Integer status;
}
