package top.sheepyu.module.system.controller.admin.user.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ygq
 * @date 2023-01-19 14:30
 **/
@Data
@ApiModel("系统用户excel导入导出vo")
public class SystemUserExcelVo {
    @ApiModelProperty("用户id")
    @ExcelProperty("id")
    private Long id;

    @ApiModelProperty("用户类型")
    @ExcelProperty("用户类型")
    private Integer type;

    @ApiModelProperty("用户名")
    @ExcelProperty("用户名")
    private String username;

    @ApiModelProperty("昵称")
    @ExcelProperty("昵称")
    private String nickname;

    @ApiModelProperty("邮箱")
    @ExcelProperty("邮箱")
    private String email;

    @ApiModelProperty("电话号码")
    @ExcelProperty("电话号码")
    private String mobile;

    @ApiModelProperty("部门-职位")
    @ExcelProperty("部门-职位")
    private String deptNames;

    @ApiModelProperty("状态")
    @ExcelProperty("状态")
    private Integer status;

    @ApiModelProperty("备注")
    @ExcelProperty("备注")
    private String remark;
}
