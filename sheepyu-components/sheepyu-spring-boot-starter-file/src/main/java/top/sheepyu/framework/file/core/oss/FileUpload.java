package top.sheepyu.framework.file.core.oss;

import java.io.InputStream;

/**
 * @author ygq
 * @date 2023-01-25 15:00
 **/
public interface FileUpload {
    /**
     * 完整文件上传
     *
     * @param is       文件流
     * @param filename 文件名称, 用来获取文件的MIME_TYPE
     * @param remark 备注
     * @return 返回访问url
     */
    String upload(InputStream is, String filename, String remark);

    /**
     * 分片上传
     *
     * @param is 文件流
     * @return 返回md5值
     */
    String uploadPart(InputStream is);

    /**
     * 打断分片上传
     *
     * @param fileId 文件id
     */
    void abortPart(String fileId);

    /**
     * 完成分片上传
     *
     * @param fileId 文件id
     * @return 返回访问url
     */
    String completePart(String fileId);

    String getType();
}
