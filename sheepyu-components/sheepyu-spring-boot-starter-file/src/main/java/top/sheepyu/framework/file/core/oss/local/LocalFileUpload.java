package top.sheepyu.framework.file.core.oss.local;

import cn.hutool.crypto.digest.MD5;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.framework.file.config.FileProperties;
import top.sheepyu.framework.file.config.FileProperties.BaseConfig;
import top.sheepyu.framework.file.core.enums.FileUploadTypeEnum;
import top.sheepyu.framework.file.core.oss.FileUpload;
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
import java.util.Vector;

import static top.sheepyu.framework.file.core.enums.FileUploadCompleteEnum.INCOMPLETE;
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

    @PostConstruct
    public void loadConfig() {
        config = fileProperties.getConfig().get(FileUploadTypeEnum.LOCAL.getCode());
    }

    @Override
    public String upload(InputStream in, String path, long size) {
        byte[] data = readAllData(in);
        String md5 = MD5.create().digestHex(data);
        in = new ByteArrayInputStream(data);
        writeFromStream(in, buildLocalPathFile(path));
        return md5;
    }

    @Override
    public String uploadPart(InputStream in, String uploadId, String path, long size, int partIndex) {
        //获取基本信息
        byte[] data = FileUtil.readAllData(in);
        String md5 = MD5.create().digestHex(data);
        path = getDatePath() + uploadId + "/" + getUUID();

        //创建文件分片信息
        FilePartDto filePart = new FilePartDto()
                .setUploadId(uploadId)
                .setPartIndex(partIndex)
                .setSize(Long.valueOf(size).intValue())
                .setMd5(md5)
                .setPath(path);
        boolean result = fileApi.createFilePart(filePart);
        //result为false说明创建失败, part已存在, 不用重复写入磁盘了
        if (!result) {
            return md5;
        }

        //写入磁盘
        FileUtil.writeBytes(data, buildLocalPathFile(path));
        return md5;
    }

    @Override
    public long completePart(String uploadId, String path) {
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
        mergeAsync(uploadId, vector.elements(), buildLocalPathStr(path));

        //清理文件分片信息
        del(new File(buildLocalPathStr(partList.get(0).getPath())).getParentFile());
        fileApi.deletePartByUploadId(uploadId);
        return size;
    }

    @Override
    public void deleteFile(List<String> paths) {
        //删除本地磁盘上的文件
        paths.forEach(path -> del(buildLocalPathFile(path)));
    }

    @Override
    public String getType() {
        return FileUploadTypeEnum.LOCAL.getCode();
    }

    @Override
    public String buildUploadId(String path) {
        return MyStrUtil.getUUID();
    }

    @Override
    public String buildDomain() {
        return config.getEndpoint() + "/file";
    }

    @Async
    public void mergeAsync(String uploadId, Enumeration<InputStream> es, String mergeFile) {
        boolean result = merge(es, mergeFile);
        if (!result) {
            log.error("uploadId[{}], 合并流失败! 回调修改文件状态!", uploadId);
            fileApi.updateFileByUploadId(new FileDto().setUploadId(uploadId).setComplete(INCOMPLETE.getCode()));
        }
    }

    private File buildLocalPathFile(String path) {
        return new File(config.getPath() + path);
    }

    private String buildLocalPathStr(String path) {
        return config.getPath() + path;
    }
}
