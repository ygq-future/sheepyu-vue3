package top.sheepyu.framework.file.core.oss.qcloud;


import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.sheepyu.framework.file.config.FileProperties;
import top.sheepyu.framework.file.config.FileProperties.BaseConfig;
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

import static top.sheepyu.framework.file.core.enums.FileUploadCompleteEnum.COMPLETE;
import static top.sheepyu.framework.file.core.enums.FileUploadCompleteEnum.INCOMPLETE;
import static top.sheepyu.framework.file.core.enums.FileUploadTypeEnum.QCLOUD;
import static top.sheepyu.module.common.exception.CommonException.exception;
import static top.sheepyu.module.common.util.FileUtil.*;
import static top.sheepyu.module.common.util.MyStrUtil.getDatePath;
import static top.sheepyu.module.common.util.MyStrUtil.getUUID;

/**
 * @author ygq
 * @date 2023-01-27 18:09
 **/
@Component
@Slf4j
public class QCloudFileUpload implements FileUpload {
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
                .setComplete(COMPLETE.getCode())
                .setUploadId(getUUID())
                .setFilename(filename)
                .setSize(size)
                .setDomain(domain)
                .setMimeType(mimeType)
                .setPath(path)
                .setUrl(url)
                .setRemark(remark);

        execute(cos -> {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(size);
            PutObjectResult putObjectResult = cos.putObject(new PutObjectRequest(config.getBucket(), path, in, metadata));
            fileApi.createFile(file.setMd5(putObjectResult.getContentMd5()));
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
                .setComplete(INCOMPLETE.getCode())
                .setMd5(md5)
                .setFilename(filename)
                .setMimeType(mimeType)
                .setDomain(domain)
                .setPath(path)
                .setUrl(url)
                .setUploadId(buildUploadId(path))
                .setRemark(remark);
        file = fileApi.createFile(file);
        if (Objects.equals(file.getComplete(), COMPLETE.getCode())) {
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
        execute(cos -> {
            UploadPartRequest uploadPartRequest = new UploadPartRequest();
            uploadPartRequest.setBucketName(config.getBucket());
            uploadPartRequest.setKey(file.getPath());
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
            fileApi.updatePartIndex(uploadId, partIndex);
        });
        return md5.get();
    }

    @Override
    public String completePart(String uploadId) {
        //准备参数
        FileDto file = fileApi.findFileByUploadId(uploadId);
        //文件属性收集
        AtomicLong size = new AtomicLong(0);
        //创建文件流序列,合并文件
        List<PartETag> partETags = new ArrayList<>();
        //查询分片信息
        execute(cos -> {
            PartListing partListing;
            ListPartsRequest listPartsRequest = new ListPartsRequest(config.getBucket(), file.getPath(), uploadId);
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
            CompleteMultipartUploadRequest complete = new CompleteMultipartUploadRequest(config.getBucket(), file.getPath(), uploadId, partETags);
            cos.completeMultipartUpload(complete);
            //设置参数, 更新文件信息
            fileApi.updateFileByUploadId(file.setComplete(COMPLETE.getCode()).setSize(size.get()));
        });
        return file.getUrl();
    }

    @Override
    public boolean deleteFile(String uploadId) {
        FileDto file = fileApi.findFileByUploadId(uploadId);
        execute(cos -> cos.deleteObject(config.getBucket(), file.getPath()));
        return fileApi.deleteFileByUploadId(uploadId);
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

    private String buildUploadId(String objectName) {
        AtomicReference<String> reference = new AtomicReference<>();
        execute(cos -> {
            InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(config.getBucket(), objectName);
            String uploadId = cos.initiateMultipartUpload(request).getUploadId();
            reference.set(uploadId);
        });
        return reference.get();
    }

    private String buildDomain() {
        return "https://" + config.getBucket() + "." + config.getEndpoint() + ".myqcloud.com";
    }

    @PostConstruct
    public void loadConfig() {
        config = fileProperties.getConfig().get(QCLOUD.getCode());
    }

}
