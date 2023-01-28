package top.sheepyu.framework.file.core.oss.local;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.framework.file.config.FileProperties;
import top.sheepyu.framework.file.config.FileProperties.BaseConfig;
import top.sheepyu.framework.file.core.enums.FileUploadTypeEnum;
import top.sheepyu.framework.file.core.oss.FileUpload;
import top.sheepyu.module.common.constants.ErrorCodeConstants;
import top.sheepyu.module.common.util.FileUtil;
import top.sheepyu.module.common.util.MyStrUtil;
import top.sheepyu.module.system.api.file.FileApi;
import top.sheepyu.module.system.api.file.FileDto;
import top.sheepyu.module.system.api.file.FilePartDto;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

import static top.sheepyu.module.common.enums.status.StatusEnum.FALSE;
import static top.sheepyu.module.common.enums.status.StatusEnum.TRUE;
import static top.sheepyu.module.common.exception.CommonException.exception;
import static top.sheepyu.module.common.util.FileUtil.*;
import static top.sheepyu.module.common.util.MyStrUtil.getDatePath;
import static top.sheepyu.module.common.util.MyStrUtil.getUUID;

/**
 * @author ygq
 * @date 2023-01-25 16:42
 **/
@Component
@Slf4j
@Validated
public class LocalFileUpload implements FileUpload {
    @Resource
    private FileProperties fileProperties;
    @Resource
    private FileApi fileApi;
    private BaseConfig config;

    @Override
    public String upload(InputStream in, String md5, String filename, String remark) {
        //获取基本信息
        long size = getSize(in);
        String domain = config.getEndpoint();
        String mimeType = getMimeType(filename);
        String path = getDatePath() + getUUID() + getSuffix(filename);
        String url = domain + "/file" + path;

        //写入磁盘, 如果md5为空会计算md5
        if (StrUtil.isBlank(md5)) {
            byte[] data = readAllData(in);
            md5 = MD5.create().digestHex(data);
            in = new ByteArrayInputStream(data);
        }

        //创建文件
        FileDto file = new FileDto()
                .setComplete(TRUE.getCode())
                .setUploadId(getUUID())
                .setMd5(md5)
                .setFilename(filename)
                .setSize(size)
                .setDomain(domain)
                .setMimeType(mimeType)
                .setPath(path)
                .setUrl(url)
                .setRemark(remark);
        file = fileApi.createFile(file);

        //写入磁盘
        writeFromStream(in, buildLocalPathFile(path));
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
        String domain = config.getEndpoint();
        String path = getDatePath() + getUUID() + getSuffix(filename);
        String url = domain + "/file" + path;

        FileDto file = new FileDto()
                .setComplete(FALSE.getCode())
                .setMd5(md5)
                .setFilename(filename)
                .setMimeType(mimeType)
                .setDomain(domain)
                .setPath(path)
                .setUrl(url)
                .setUploadId(MyStrUtil.getUUID())
                .setRemark(remark);
        file = fileApi.createFile(file);
        if (Objects.equals(file.getComplete(), TRUE.getCode())) {
            throw exception(ErrorCodeConstants.EXISTS);
        }
        return file.getUploadId();
    }

    @Override
    public String uploadPart(String uploadId, InputStream in, int partIndex) {
        //获取基本信息
        byte[] data = FileUtil.readAllData(in);
        int size = data.length;
        String md5 = MD5.create().digestHex(data);
        String path = getDatePath() + uploadId + "/" + getUUID();

        //创建文件分片信息
        FilePartDto filePart = new FilePartDto()
                .setUploadId(uploadId)
                .setPartIndex(partIndex)
                .setSize(size)
                .setMd5(md5)
                .setPath(path);
        boolean result = fileApi.createFilePart(filePart);
        //result为false说明创建失败, part已存在, 不用重复写入磁盘了
        if (!result) {
            return md5;
        }

        //写入磁盘
        FileUtil.writeBytes(data, buildLocalPathFile(path));
        //更新文件index
        fileApi.updatePartIndex(uploadId, partIndex);
        return md5;
    }

    @Override
    public String completePart(String uploadId) {
        //准备参数
        FileDto file = fileApi.findFileByUploadId(uploadId);
        List<FilePartDto> partList = fileApi.listByUploadId(uploadId);
        Vector<InputStream> vector = new Vector<>(partList.size());

        //文件属性收集
        long size = 0;
        //创建文件流序列,合并文件
        for (FilePartDto filePart : partList) {
            InputStream in = createIn(buildLocalPathStr(filePart.getPath()));
            size += filePart.getSize();
            vector.add(in);
        }
        //异步合并流
        mergeAsync(uploadId, vector.elements(), buildLocalPathStr(file.getPath()));

        //设置参数, 更新文件信息
        fileApi.updateFileByUploadId(file.setComplete(TRUE.getCode()).setSize(size));

        //清理文件分片信息
        del(new File(buildLocalPathStr(partList.get(0).getPath())).getParentFile());
        fileApi.deletePartByUploadId(uploadId);
        return file.getUrl();
    }

    @Override
    public boolean deleteFile(String uploadId) {
        //根据uploadId查找文件
        FileDto file = fileApi.findFileByUploadId(uploadId);
        //删除本地磁盘上的文件
        del(buildLocalPathFile(file.getPath()));
        return fileApi.deleteFileByUploadId(uploadId);
    }

    @Override
    public String getType() {
        return FileUploadTypeEnum.LOCAL.getCode();
    }

    @PostConstruct
    public void loadConfig() {
        config = fileProperties.getConfig().get(FileUploadTypeEnum.LOCAL.getCode());
    }

    @Async
    public void mergeAsync(String uploadId, Enumeration<InputStream> es, String mergeFile) {
        boolean result = merge(es, mergeFile);
        if (!result) {
            log.error("uploadId[{}], 合并流失败! 回调修改文件状态!", uploadId);
            fileApi.updateFileByUploadId(new FileDto().setUploadId(uploadId).setComplete(FALSE.getCode()));
        }
    }

    private File buildLocalPathFile(String path) {
        return new File(config.getPath() + path);
    }

    private String buildLocalPathStr(String path) {
        return config.getPath() + path;
    }
}
