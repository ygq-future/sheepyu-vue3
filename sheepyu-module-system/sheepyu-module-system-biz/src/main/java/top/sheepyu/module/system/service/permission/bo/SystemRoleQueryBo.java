package top.sheepyu.module.system.service.permission.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.sheepyu.module.system.controller.admin.permission.role.SystemRoleQueryVo;

import java.util.List;
import java.util.Set;

/**
 * @author ygq
 * @date 2023-09-26 15:10
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class SystemRoleQueryBo extends SystemRoleQueryVo {
    @ApiModelProperty("创建者")
    private List<String> creators;

    @ApiModelProperty("角色id")
    private Set<Long> roleIds;
}
