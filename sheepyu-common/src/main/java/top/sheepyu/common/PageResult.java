package top.sheepyu.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@ApiModel("分页结果")
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class PageResult<T> implements Serializable {
    @ApiModelProperty(value = "数据", required = true)
    private List<T> list;

    @ApiModelProperty(value = "总量", required = true)
    private Long total;
}
