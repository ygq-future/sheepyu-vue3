package top.sheepyu.module.system.controller.admin.dept.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ygq
 * @date 2023-01-30 11:39
 **/
@Data
@ApiModel("系统部门查询vo")
public class SystemDeptQueryVo {
    @ApiModelProperty("查询关键字")
    private String keyword;
}
