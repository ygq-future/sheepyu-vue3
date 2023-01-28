package top.sheepyu.framework.file.core.oss.aliyun;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.sheepyu.framework.file.config.FileProperties;
import top.sheepyu.framework.file.config.FileProperties.BaseConfig;
import top.sheepyu.framework.file.core.enums.FileUploadTypeEnum;
import top.sheepyu.framework.file.core.oss.FileUpload;
import top.sheepyu.module.common.constants.ErrorCodeConstants;
import top.sheepyu.module.system.api.file.FileApi;
import top.sheepyu.module.system.api.file.FileDto;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static top.sheepyu.module.common.enums.status.StatusEnum.FALSE;
import static top.sheepyu.module.common.enums.status.StatusEnum.TRUE;
import static top.sheepyu.module.common.exception.CommonException.exception;
import static top.sheepyu.module.common.util.FileUtil.*;
import static top.sheepyu.module.common.util.MyStrUtil.getDatePath;
import static top.sheepyu.module.common.util.MyStrUtil.getUUID;

/**
 * @author ygq
 * @date 2023-01-27 14:31
 **/
@Component
@Slf4j
public class AliyunFileUpload implements FileUpload {
    @Resource
    private FileProperties fileProperties;
    @Resource
    private FileApi fileApi;
    private BaseConfig config;

    @Override
    public String upload(InputStream in, String md5, String filename, String remark) {
        //获取基本信息
        long size = getSize(in);
        String domain = buildDomain();
        String mimeType = getMimeType(filename);
        String path = getDatePath() + getUUID() + getSuffix(filename);
        String url = domain + path;

        //创建文件
        FileDto file = new FileDto()
                .setComplete(TRUE.getCode())
                .setUploadId(getUUID())
                .setFilename(filename)
                .setSize(size)
                .setDomain(domain)
                .setMimeType(mimeType)
                .setPath(path)
                .setUrl(url)
                .setRemark(remark);

        execute(ossClient -> {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(size);
            PutObjectRequest putObjectRequest = new PutObjectRequest(config.getBucket(), path.substring(1), in, metadata);
            putObjectRequest.setProcess("true");
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            //aliyun返回的md5默认是大写, 为了保持一致, 这里转成小写
            fileApi.createFile(file.setMd5(result.getETag().toLowerCase()));
        });
        return file.getUrl();
    }

    @Override
    public String uploadMini(InputStream in, String filename) {
        return upload(in, null, filename, null);
    }

    @Override
    public String preparePart(String md5, String filename, String remark) {
        //封装基本属性
        String mimeType = getMimeType(filename);
        String domain = buildDomain();
        String path = getDatePath() + getUUID() + getSuffix(filename);
        String url = domain + path;

        FileDto file = new FileDto()
                .setComplete(FALSE.getCode())
                .setMd5(md5)
                .setFilename(filename)
                .setMimeType(mimeType)
                .setDomain(domain)
                .setPath(path)
                .setUrl(url)
                .setUploadId(buildUploadId(path.substring(1)))
                .setRemark(remark);
        file = fileApi.createFile(file);
        if (Objects.equals(file.getComplete(), TRUE.getCode())) {
            throw exception(ErrorCodeConstants.EXISTS);
        }
        return file.getUploadId();
    }

    @Override
    public String uploadPart(String uploadId, InputStream in, int partIndex) {
        FileDto file = fileApi.findFileByUploadId(uploadId);
        //获取基本信息
        long size = getSize(in);
        AtomicReference<String> md5 = new AtomicReference<>();
        execute(oss -> {
            UploadPartRequest uploadPartRequest = new UploadPartRequest();
            uploadPartRequest.setBucketName(config.getBucket());
            uploadPartRequest.setKey(file.getPath().substring(1));
            uploadPartRequest.setUploadId(uploadId);
            uploadPartRequest.setInputStream(in);
            // 设置分片大小。除了最后一个分片没有大小限制，其他的分片最小为100 KB。
            uploadPartRequest.setPartSize(size);
            // 设置分片号。每一个上传的分片都有一个分片号，取值范围是1~10000，如果超出此范围，OSS将返回InvalidArgument错误码。
            uploadPartRequest.setPartNumber(partIndex);
            UploadPartResult uploadPartResult = oss.uploadPart(uploadPartRequest);
            // 每次上传分片之后，OSS的返回结果包含PartETag。PartETag将被保存在partETags中。
            PartETag partETag = uploadPartResult.getPartETag();
            md5.set(partETag.getETag());
            fileApi.updatePartIndex(uploadId, partIndex);
        });
        return md5.get();
    }

    @Override
    public String completePart(String uploadId) {
        //准备参数
        FileDto file = fileApi.findFileByUploadId(uploadId);
        String path = file.getPath().substring(1);

        //文件属性收集
        AtomicLong size = new AtomicLong(0);
        //创建文件流序列,合并文件
        List<PartETag> partETags = new ArrayList<>();
        // 查询分片信息
        execute(oss -> {
            PartListing partListing;
            ListPartsRequest listPartsRequest = new ListPartsRequest(config.getBucket(), path, uploadId);
            do {
                partListing = oss.listParts(listPartsRequest);
                for (PartSummary part : partListing.getParts()) {
                    partETags.add(new PartETag(part.getPartNumber(), part.getETag()));
                    size.set(size.get() + part.getSize());
                }
                listPartsRequest.setPartNumberMarker(partListing.getNextPartNumberMarker());
            } while (partListing.isTruncated());
        });

        //合并分片
        execute(oss -> {
            CompleteMultipartUploadRequest complete = new CompleteMultipartUploadRequest(config.getBucket(), path, uploadId, partETags);
            oss.completeMultipartUpload(complete);
            //设置参数, 更新文件信息
            fileApi.updateFileByUploadId(file.setComplete(TRUE.getCode()).setSize(size.get()));
        });
        return file.getUrl();
    }

    @Override
    public boolean deleteFile(String uploadId) {
        FileDto file = fileApi.findFileByUploadId(uploadId);
        execute(oss -> oss.deleteObject(config.getBucket(), file.getPath().substring(1)));
        return fileApi.deleteFileByUploadId(uploadId);
    }

    @Override
    public String getType() {
        return FileUploadTypeEnum.ALIYUN.getCode();
    }

    private void execute(Consumer<OSS> ossConsumer) {
        OSS ossClient = buildClient();

        Throwable e;
        try {
            ossConsumer.accept(ossClient);
            return;
        } catch (OSSException oe) {
            log.error("Error Message: {}", oe.getErrorMessage());
            log.error("Error Code: {}", oe.getErrorCode());
            log.error("Request ID: {}", oe.getRequestId());
            log.error("Host ID: {}", oe.getHostId());
            e = oe;
        } catch (ClientException ce) {
            log.error("Error Message: {}", ce.getMessage());
            e = ce;
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        throw new RuntimeException(e);
    }

    private OSS buildClient() {
        return new OSSClientBuilder().build(config.getEndpoint(), config.getKey(), config.getSecret());
    }

    private String buildUploadId(String objectName) {
        AtomicReference<String> reference = new AtomicReference<>();
        execute(oss -> {
            InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(config.getBucket(), objectName);
            String uploadId = oss.initiateMultipartUpload(request).getUploadId();
            reference.set(uploadId);
        });
        return reference.get();
    }

    private String buildDomain() {
        return "https://" + config.getBucket() + "." + config.getEndpoint();
    }

    @PostConstruct
    public void loadConfig() {
        config = fileProperties.getConfig().get(FileUploadTypeEnum.ALIYUN.getCode());
    }
}
