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
@EqualsAndHashCode(callSuper = true)
@ApiModel("测试响应vo")
public class SystemDemoRespVo extends SystemDemoBaseVo {
    @ApiModelProperty(value = "id", required = true, example = "1")
    @NotNull(message = "id不能为空")
    private Long id;

    @ApiModelProperty(value = "")
    private String creator;

    @ApiModelProperty(value = "", required = true)
    @NotNull(message = "不能为空")
    private Date createTime;

}
