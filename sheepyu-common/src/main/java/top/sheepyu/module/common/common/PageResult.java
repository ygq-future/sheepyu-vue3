package top.sheepyu.module.common.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
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

    public static <T> PageResult<T> emptyPage() {
        return new PageResult<>(Collections.emptyList(), 0L);
    }
}
