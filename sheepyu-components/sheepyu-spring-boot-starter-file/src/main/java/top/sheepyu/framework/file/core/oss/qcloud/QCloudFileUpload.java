package top.sheepyu.framework.file.core.oss.qcloud;


import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.*;
import com.qcloud.cos.model.DeleteObjectsRequest.KeyVersion;
import com.qcloud.cos.region.Region;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.sheepyu.framework.file.config.FileProperties;
import top.sheepyu.framework.file.config.FileProperties.BaseConfig;
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

import static top.sheepyu.framework.file.core.enums.FileUploadTypeEnum.QCLOUD;

/**
 * @author ygq
 * @date 2023-01-27 18:09
 **/
@Component
@Slf4j
public class QCloudFileUpload implements FileUpload {
    @Resource
    private FileProperties fileProperties;
    private BaseConfig config;

    @PostConstruct
    public void loadConfig() {
        config = fileProperties.getConfig().get(QCLOUD.getCode());
    }

    @Override
    public String upload(InputStream in, String path, long size) {
        AtomicReference<String> md5 = new AtomicReference<>();
        execute(cos -> {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(size);
            PutObjectResult putObjectResult = cos.putObject(new PutObjectRequest(config.getBucket(), path, in, metadata));
            md5.set(putObjectResult.getContentMd5());
        });
        return md5.get();
    }

    @Override
    public String uploadPart(InputStream in, String uploadId, String path, long size, int partIndex) {
        AtomicReference<String> md5 = new AtomicReference<>();
        execute(cos -> {
            UploadPartRequest uploadPartRequest = new UploadPartRequest();
            uploadPartRequest.setBucketName(config.getBucket());
            uploadPartRequest.setKey(path);
            uploadPartRequest.setUploadId(uploadId);
            uploadPartRequest.setInputStream(in);
            // 设置分片大小。除了最后一个分片没有大小限制，其他的分片最小为100 KB。
            uploadPartRequest.setPartSize(size);
            // 设置分片号。每一个上传的分片都有一个分片号，取值范围是1~10000，如果超出此范围，OSS将返回InvalidArgument错误码。
            uploadPartRequest.setPartNumber(partIndex);
            UploadPartResult uploadPartResult = cos.uploadPart(uploadPartRequest);
            // 每次上传分片之后，OSS的返回结果包含PartETag。PartETag将被保存在partETags中。
            PartETag partETag = uploadPartResult.getPartETag();
            md5.set(partETag.getETag());
        });
        return md5.get();
    }

    @Override
    public long completePart(String uploadId, String path) {
        AtomicLong size = new AtomicLong(0);
        //创建文件流序列,合并文件
        List<PartETag> partETags = new ArrayList<>();
        //查询分片信息
        execute(cos -> {
            PartListing partListing;
            ListPartsRequest listPartsRequest = new ListPartsRequest(config.getBucket(), path, uploadId);
            do {
                partListing = cos.listParts(listPartsRequest);
                for (PartSummary part : partListing.getParts()) {
                    partETags.add(new PartETag(part.getPartNumber(), part.getETag()));
                    size.set(size.get() + part.getSize());
                }
                listPartsRequest.setPartNumberMarker(partListing.getNextPartNumberMarker());
            } while (partListing.isTruncated());
        });

        //合并分片
        execute(cos -> {
            CompleteMultipartUploadRequest complete = new CompleteMultipartUploadRequest(config.getBucket(), path, uploadId, partETags);
            cos.completeMultipartUpload(complete);
        });
        return size.get();
    }

    @Override
    public void deleteFile(List<String> paths) {
        execute(cos -> {
            DeleteObjectsRequest request = new DeleteObjectsRequest(config.getBucket());
            List<KeyVersion> keys = paths.stream().map(KeyVersion::new).collect(Collectors.toList());
            request.setKeys(keys);
            cos.deleteObjects(request);
        });
    }

    @Override
    public String getType() {
        return QCLOUD.getCode();
    }

    private void execute(Consumer<COSClient> cosClientConsumer) {
        COSClient cosClient = buildClient();

        Throwable e;
        try {
            cosClientConsumer.accept(cosClient);
            return;
        } catch (CosServiceException cse) {
            log.error("Error Message: {}", cse.getErrorMessage());
            log.error("Error Code: {}", cse.getErrorCode());
            log.error("Request ID: {}", cse.getRequestId());
            e = cse;
        } catch (CosClientException ce) {
            log.error("Error Message: {}", ce.getMessage());
            e = ce;
        } finally {
            if (cosClient != null) {
                cosClient.shutdown();
            }
        }
        throw new RuntimeException(e);
    }

    private COSClient buildClient() {
        COSCredentials cred = new BasicCOSCredentials(config.getKey(), config.getSecret());
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setRegion(new Region(config.getEndpoint()));
        return new COSClient(cred, clientConfig);
    }

    @Override
    public String buildUploadId(String path) {
        AtomicReference<String> reference = new AtomicReference<>();
        execute(cos -> {
            InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(config.getBucket(), path);
            String uploadId = cos.initiateMultipartUpload(request).getUploadId();
            reference.set(uploadId);
        });
        return reference.get();
    }

    @Override
    public String buildDomain() {
        return "https://" + config.getBucket() + "." + config.getEndpoint() + ".myqcloud.com";
    }

}
