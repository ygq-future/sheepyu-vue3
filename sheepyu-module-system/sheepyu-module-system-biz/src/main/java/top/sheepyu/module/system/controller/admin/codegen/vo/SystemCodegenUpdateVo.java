package top.sheepyu.module.system.controller.admin.codegen.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.sheepyu.module.system.dao.codegen.SystemCodegenColumn;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author ygq
 * @date 2023-03-04 10:12
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("代码生成修改对象")
public class SystemCodegenUpdateVo extends SystemCodegenBaseVo {
    @ApiModelProperty("id")
    @NotNull(message = "id不能为空")
    private Long id;

    @ApiModelProperty("所有列")
    @NotEmpty(message = "列不能为空")
    private List<SystemCodegenColumn> columns;
}
