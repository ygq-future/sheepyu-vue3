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
@ApiModel("测试修改vo")
public class SystemDemoUpdateVo extends SystemDemoBaseVo {
    @ApiModelProperty(value = "id", required = true, example = "1")
    @NotNull(message = "id不能为空")
    private Long id;

}
