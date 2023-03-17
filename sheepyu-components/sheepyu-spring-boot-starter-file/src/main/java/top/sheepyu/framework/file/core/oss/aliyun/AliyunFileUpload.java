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

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author ygq
 * @date 2023-01-27 14:31
 **/
@Component
@Slf4j
public class AliyunFileUpload implements FileUpload {
    @Resource
    private FileProperties fileProperties;
    private BaseConfig config;

    @PostConstruct
    public void loadConfig() {
        config = fileProperties.getConfig().get(FileUploadTypeEnum.ALIYUN.getCode());
    }

    @Override
    public String upload(InputStream in, String path, long size) {
        AtomicReference<String> md5 = new AtomicReference<>();
        execute(ossClient -> {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(size);
            PutObjectRequest putObjectRequest = new PutObjectRequest(config.getBucket(), path.substring(1), in, metadata);
            putObjectRequest.setProcess("true");
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            //aliyun返回的md5默认是大写, 为了保持一致, 这里转成小写
            md5.set(result.getETag().toLowerCase());
        });
        return md5.get();
    }

    @Override
    public String uploadPart(InputStream in, String uploadId, String path, long size, int partIndex) {
        AtomicReference<String> md5 = new AtomicReference<>();
        execute(oss -> {
            UploadPartRequest uploadPartRequest = new UploadPartRequest();
            uploadPartRequest.setBucketName(config.getBucket());
            uploadPartRequest.setKey(path.substring(1));
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
        });
        return md5.get();
    }

    @Override
    public long completePart(String uploadId, String path) {
        //文件属性收集
        AtomicLong size = new AtomicLong(0);
        //创建文件流序列,合并文件
        List<PartETag> partETags = new ArrayList<>();
        // 查询分片信息
        execute(oss -> {
            PartListing partListing;
            ListPartsRequest listPartsRequest = new ListPartsRequest(config.getBucket(), path.substring(1), uploadId);
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
            CompleteMultipartUploadRequest complete = new CompleteMultipartUploadRequest(config.getBucket(), path.substring(1), uploadId, partETags);
            oss.completeMultipartUpload(complete);
        });
        return size.get();
    }

    @Override
    public void deleteFile(List<String> paths) {
        paths = paths.stream().map(e -> e.substring(1)).collect(Collectors.toList());
        List<String> finalPaths = paths;
        execute(oss -> {
            DeleteObjectsRequest request = new DeleteObjectsRequest(config.getBucket());
            request.setKeys(finalPaths);
            oss.deleteObjects(request);
        });
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

    @Override
    public String buildUploadId(String path) {
        AtomicReference<String> reference = new AtomicReference<>();
        execute(oss -> {
            InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(config.getBucket(), path.substring(1));
            String uploadId = oss.initiateMultipartUpload(request).getUploadId();
            reference.set(uploadId);
        });
        return reference.get();
    }

    @Override
    public String buildDomain() {
        return "https://" + config.getBucket() + "." + config.getEndpoint();
    }
}
