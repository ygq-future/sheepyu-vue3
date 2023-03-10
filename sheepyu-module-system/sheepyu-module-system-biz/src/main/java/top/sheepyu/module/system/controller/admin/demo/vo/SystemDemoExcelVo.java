package top.sheepyu.module.system.controller.admin.demo.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.*;
/**
* @author ygq
* @date 2023-01-19 14:30
**/
@Data
@ApiModel("测试excel导入导出vo")
public class SystemDemoExcelVo {
    @ApiModelProperty("id")
    @ExcelProperty("id")
    private Long id;

    @ApiModelProperty("测试名称")
    @ExcelProperty("测试名称")
    private String name;

    @ApiModelProperty("测试内容")
    @ExcelProperty("测试内容")
    private String content;

    @ApiModelProperty("启用状态")
    @ExcelProperty("启用状态")
    private Integer status;

    @ApiModelProperty("开始时间")
    @ExcelProperty("开始时间")
    private Date beginTime;

    @ApiModelProperty("年龄")
    @ExcelProperty("年龄")
    private Integer age;

    @ApiModelProperty("性别")
    @ExcelProperty("性别")
    private Integer sex;

}
