package top.sheepyu.framework.file.core.oss;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.InputStream;

/**
 * @author ygq
 * @date 2023-01-25 15:00
 **/
public interface FileUpload {
    /**
     * 完整文件上传
     *
     * @param in       文件流
     * @param filename 文件名称, 用来获取文件的MIME_TYPE
     * @param remark   备注
     * @return 返回访问url
     */
    String upload(@NotNull(message = "文件流不能为空") InputStream in,
                  @NotBlank(message = "文件名不能为空") String filename,
                  String remark);

    /**
     * 分片上传准备工作, 会创建一个file, 然后返回fileId
     *
     * @param filename 文件名称
     * @param remark   备注
     * @return 返回文件id
     */
    Long preparePart(@NotBlank(message = "文件名不能为空") String filename,
                     String remark);

    /**
     * 分片上传
     *
     * @param fileId 文件id
     * @param in     文件流
     * @param index  第几个part, 从0开始
     * @return 返回md5值
     */
    String uploadPart(@NotNull(message = "文件id不能为空") Long fileId,
                      @NotNull(message = "文件流不能为空") InputStream in, int index);

    /**
     * 打断分片上传
     *
     * @param fileId 文件id
     */
    void abortPart(@NotNull(message = "文件id不能为空") Long fileId);

    /**
     * 完成分片上传
     *
     * @param fileId 文件id
     * @return 返回访问url
     */
    String completePart(@NotNull(message = "文件id不能为空") Long fileId);

    String getType();
}
