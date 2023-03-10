package top.sheepyu.module.system.controller.admin.codegen.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;
import top.sheepyu.module.common.annotations.InEnum;
import top.sheepyu.module.common.common.PageParam;
import top.sheepyu.module.system.enums.codegen.CodegenSceneEnum;

import java.util.Date;

import static top.sheepyu.module.common.constants.CommonConstants.DATE_TIME_FORMAT;

/**
 * @author ygq
 * @date 2023-03-09 15:44
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("代码生成查询vo")
public class SystemCodegenQueryVo extends PageParam {
    @ApiModelProperty("场景")
    @InEnum(CodegenSceneEnum.class)
    private Integer scene;

    @ApiModelProperty("创建时间")
    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    private Date[] createTimes;
}
