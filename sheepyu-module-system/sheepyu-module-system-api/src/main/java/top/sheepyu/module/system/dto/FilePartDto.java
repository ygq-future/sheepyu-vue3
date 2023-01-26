package top.sheepyu.module.system.dto;

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
    @NotNull(message = "文件id不能为空")
    private Long fileId;

    @NotBlank(message = "path不能为空")
    private String path;

    @NotBlank(message = "md5值不能为空")
    private String md5;

    private byte[] partTag;

    @NotNull(message = "index不能为空")
    private Integer index;

    @NotNull(message = "size不能为空")
    private Integer size;
}
