package top.sheepyu.module.system.controller.admin.permission.role;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.sheepyu.module.common.common.PageParam;

/**
 * @author ygq
 * @date 2023-01-30 16:01
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("系统角色查询vo")
public class SystemRoleQueryVo extends PageParam {
    @ApiModelProperty("部门id")
    private Long deptId;
}
