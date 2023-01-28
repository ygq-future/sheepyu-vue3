package top.sheepyu.framework.file.core.oss;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.InputStream;

/**
 * @author ygq
 * @date 2023-01-25 15:00
 **/
public interface FileUpload {
    /**
     * 完整文件上传,这个接口需要请开发者注意, 如果是小文件那么md5可以不传
     * 比如用户端上传头像啥的, 但是后台如果上传的文件比较大推荐用分片上传
     * 如果要用这个就先用前端的MD5worker计算出md5再传过来, 不要让后端计算
     * 因为如果文件过大, 后端转化为byte[]来计算可能会造成内存溢出,所以上传
     * 小文件使用下面的uploadMini, 其实就是调用upload(in, null, filename, null)
     *
     * @param in       文件流
     * @param md5      文件md5, 图片小文件不必传, 附件压缩包超过50M的需要传一下md5
     * @param filename 文件名称, 用来获取文件的MIME_TYPE
     * @param remark   备注
     * @return 返回访问url
     */
    String upload(@NotNull(message = "文件流不能为空") InputStream in,
                  @NotBlank(message = "md5不能为空") String md5,
                  @NotBlank(message = "文件名不能为空") String filename,
                  String remark);

    /**
     * 上传小文件, 图片之类的
     *
     * @param in       文件流
     * @param filename 文件名
     * @return 返回url
     */
    String uploadMini(@NotNull(message = "文件流不能为空") InputStream in,
                      @NotBlank(message = "文件名不能为空") String filename);

    /**
     * 分片上传准备工作, 会创建一个file, 然后返回fileId
     * 分片上传必须有MD5值
     *
     * @param md5      整个文件的md5
     * @param filename 文件名称
     * @param remark   备注
     * @return 返回uploadId
     */
    String preparePart(@NotBlank(message = "md5不能为空") String md5,
                       @NotBlank(message = "文件名不能为空") String filename,
                       String remark);

    /**
     * 分片上传
     *
     * @param uploadId  文件id
     * @param in        文件流
     * @param partIndex 第几个part, 从1开始
     * @return 返回md5值
     */
    String uploadPart(@NotNull(message = "文件id不能为空") String uploadId,
                      @NotNull(message = "文件流不能为空") InputStream in,
                      @NotNull(message = "partIndex不能为空")
                      @Min(value = 1, message = "partIndex最小值为1") int partIndex);

    /**
     * 完成分片上传
     *
     * @param uploadId uploadId
     * @return 返回访问url
     */
    String completePart(@NotNull(message = "文件id不能为空") String uploadId);

    boolean deleteFile(String uploadId);

    String getType();
}
