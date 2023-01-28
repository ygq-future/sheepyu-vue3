package top.sheepyu.module.system.api.file;

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

    /**
     * 不管什么上传, 都已uploadId作为唯一标识, 推荐使用uuid
     */
    @NotBlank(message = "uploadId不能为空")
    private String uploadId;

    private Integer partIndex;

    @NotBlank(message = "文件名不能为空")
    private String filename;

    @NotBlank(message = "md5不能为空")
    private String md5;

    private String url;

    private String mimeType;

    private Long size;

    private String domain;

    private String path;

    @NotNull(message = "完成状态不能为空")
    private Integer complete;

    private String remark;
}
