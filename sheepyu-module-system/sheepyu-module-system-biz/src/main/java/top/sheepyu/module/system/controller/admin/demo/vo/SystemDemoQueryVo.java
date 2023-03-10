package top.sheepyu.module.system.controller.admin.demo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;
import top.sheepyu.module.common.common.PageParam;

import java.util.Date;

import static top.sheepyu.module.common.constants.CommonConstants.DATE_TIME_FORMAT;

/**
* @author ygq
* @date 2023-01-17 10:37
**/
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("测试分页查询vo")
public class SystemDemoQueryVo extends PageParam  {

    @ApiModelProperty("启用状态")
    private Integer status;

    @ApiModelProperty("开始时间")
    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    private Date[] beginTimes;

    @ApiModelProperty()
    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    private Date[] createTimes;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("性别")
    private Integer sex;
}
