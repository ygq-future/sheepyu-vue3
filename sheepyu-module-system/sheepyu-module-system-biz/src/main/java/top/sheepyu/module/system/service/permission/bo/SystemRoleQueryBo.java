package top.sheepyu.module.system.service.permission.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.sheepyu.module.system.controller.admin.permission.role.SystemRoleQueryVo;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

/**
 * @author ygq
 * @date 2023-09-26 15:10
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class SystemRoleQueryBo extends SystemRoleQueryVo {
    @ApiModelProperty("部门ids")
    @NotEmpty(message = "查询的部门id不能为空")
    private Set<Long> deptIds;
}
