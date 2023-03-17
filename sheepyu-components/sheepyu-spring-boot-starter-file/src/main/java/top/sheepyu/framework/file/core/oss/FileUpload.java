package top.sheepyu.framework.file.core.oss;

import java.io.InputStream;
import java.util.List;

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
     *
     * @param in   文件输入流
     * @param path 文件路径, 路径以/开头
     * @param size 文件大小
     * @return 返回md5
     */
    String upload(InputStream in, String path, long size);

    String uploadPart(InputStream in, String uploadId, String path, long size, int partIndex);

    long completePart(String uploadId, String path);

    void deleteFile(List<String> paths);

    String getType();

    String buildUploadId(String path);

    String buildDomain();
}
