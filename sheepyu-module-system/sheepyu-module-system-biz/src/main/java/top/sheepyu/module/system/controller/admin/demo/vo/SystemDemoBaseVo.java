package top.sheepyu.module.system.controller.admin.demo.vo;

import io.swagger.annotations.*;
import lombok.*;
import javax.validation.constraints.*;
import java.util.*;

/**
* @author ygq
* @date 2023-01-18 15:03
**/
@Data
@ApiModel("测试基本vo")
public class SystemDemoBaseVo {
    @ApiModelProperty(value = "测试名称", required = true, example = "demo")
    @NotEmpty(message = "测试名称不能为空")
    private String name;

    @ApiModelProperty(value = "测试内容")
    private String content;

    @ApiModelProperty(value = "启用状态", required = true)
    @NotNull(message = "启用状态不能为空")
    private Integer status;

    @ApiModelProperty(value = "开始时间", required = true)
    @NotNull(message = "开始时间不能为空")
    private Date beginTime;

    @ApiModelProperty(value = "年龄", required = true)
    @NotNull(message = "年龄不能为空")
    private Integer age;

    @ApiModelProperty(value = "性别", required = true)
    @NotNull(message = "性别不能为空")
    private Integer sex;

}
