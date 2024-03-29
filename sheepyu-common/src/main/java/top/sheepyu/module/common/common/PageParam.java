package top.sheepyu.module.common.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ApiModel("分页参数")
@Data
@NoArgsConstructor
public class PageParam implements Serializable {
    private static final Integer CURRENT = 1;
    private static final Integer SIZE = 10;

    @ApiModelProperty(value = "页码，从 1 开始", required = true, example = "1")
    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码最小值为 1")
    private Integer current = CURRENT;

    @ApiModelProperty(value = "每页条数，最大值为 100", required = true, example = "10")
    @NotNull(message = "每页条数不能为空")
    @Min(value = 1, message = "每页条数最小值为 1")
    @Max(value = 100, message = "每页条数最大值为 100")
    private Integer size = SIZE;

    @ApiModelProperty("模糊查询关键字")
    private String keyword;

    public PageParam(Integer current, Integer size) {
        this.current = current;
        this.size = size;
    }
}
