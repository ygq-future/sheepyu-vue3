package top.sheepyu.module.system.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author ygq
 * @date 2023-01-25 16:21
 **/
@Data
@Accessors(chain = true)
public class FileDto {
    private Long id;

    @NotBlank(message = "文件名不能为空")
    private String filename;

    private String url;

    private String mimeType;

    private Integer size;

    private String domain;

    private String path;

    @NotNull(message = "完成状态不能为空")
    private Integer complete;

    private String remark;
}
