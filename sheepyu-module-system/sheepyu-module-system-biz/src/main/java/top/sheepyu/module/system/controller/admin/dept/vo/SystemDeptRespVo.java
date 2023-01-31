package top.sheepyu.module.system.controller.admin.dept.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author ygq
 * @date 2023-01-29 18:10
 **/
@Data
@ApiModel("系统部门响应vo")
public class SystemDeptRespVo {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("部门名称")
    private String name;

    @ApiModelProperty("父部门id")
    private Long parentId;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("部门负责人id")
    private Long leaderUserId;

    @ApiModelProperty("部门联系电话")
    private String phone;

    @ApiModelProperty("部门联系邮箱")
    private String email;

    @ApiModelProperty("子部门")
    private List<SystemDeptRespVo> children;
}
