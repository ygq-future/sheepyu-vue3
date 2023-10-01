package top.sheepyu.module.system.controller.admin.dept.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @author ygq
 * @date 2023-01-29 18:10
 **/
@Data
@ApiModel("系统部门基本vo")
public class SystemDeptBaseVo {
    @ApiModelProperty("部门名称")
    @NotBlank(message = "部门名称不能为空")
    private String name;

    @ApiModelProperty("排序")
    @NotNull(message = "排序不能为空")
    private Integer sort;

    @ApiModelProperty("部门负责人ids")
    private Set<Long> leaderUserIds;

    @ApiModelProperty("部门负责人昵称")
    private String leaderNicknames;

    @ApiModelProperty("可查询此部门的目标部门ids")
    private Set<Long> targetDeptIds;

    @ApiModelProperty("部门联系电话")
    private String phone;

    @ApiModelProperty("部门联系邮箱")
    private String email;
}
