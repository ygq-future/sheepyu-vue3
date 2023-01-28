package top.sheepyu.module.system.api.file;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author ygq
 * @date 2023-01-25 15:57
 **/
@Data
@Accessors(chain = true)
public class FilePartDto {
    private Long id;

    @NotNull(message = "uploadId不能为空")
    private String uploadId;

    @NotBlank(message = "path不能为空")
    private String path;

    @NotBlank(message = "md5值不能为空")
    private String md5;

    @NotNull(message = "index不能为空")
    private Integer partIndex;

    @NotNull(message = "size不能为空")
    private Integer size;
}
