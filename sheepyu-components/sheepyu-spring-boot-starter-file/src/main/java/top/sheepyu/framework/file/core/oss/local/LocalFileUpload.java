package top.sheepyu.framework.file.core.oss.local;

import cn.hutool.crypto.digest.MD5;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.framework.file.config.FileProperties;
import top.sheepyu.framework.file.config.FileProperties.BaseConfig;
import top.sheepyu.framework.file.core.enums.FileUploadTypeEnum;
import top.sheepyu.framework.file.core.oss.FileUpload;
import top.sheepyu.module.common.util.FileUtil;
import top.sheepyu.module.system.api.FileApi;
import top.sheepyu.module.system.dto.FileDto;
import top.sheepyu.module.system.dto.FilePartDto;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Vector;

import static top.sheepyu.module.common.enums.status.StatusEnum.FALSE;
import static top.sheepyu.module.common.enums.status.StatusEnum.TRUE;
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
    public BaseConfig config;

    @Override
    public String upload(InputStream in, String filename, String remark) {
        int size = getSize(in);
        String domain = config.getEndpoint();
        String mimeType = getMimeType(filename);
        String path = getDatePath() + getUUID() + getSuffix(filename);
        String url = domain + "/file" + path;
        writeFromStream(in, buildLocalPathFile(path));

        FileDto file = new FileDto()
                .setComplete(TRUE.getCode())
                .setFilename(filename)
                .setSize(size)
                .setDomain(domain)
                .setMimeType(mimeType)
                .setPath(path)
                .setUrl(url)
                .setRemark(remark);

        fileApi.createFile(file);
        return url;
    }

    @Override
    public Long preparePart(String filename, String remark) {
        String mimeType = getMimeType(filename);
        FileDto file = new FileDto()
                .setComplete(FALSE.getCode())
                .setFilename(filename)
                .setMimeType(mimeType)
                .setRemark(remark);
        return fileApi.createFile(file);
    }

    @Override
    public String uploadPart(Long fileId, InputStream in, int index) {
        byte[] data = FileUtil.readAllData(in);
        int size = data.length;
        String md5 = MD5.create().digestHex(data);
        String path = getDatePath() + fileId + "/" + getUUID();
        FileUtil.writeBytes(data, buildLocalPathFile(path));

        FilePartDto filePart = new FilePartDto()
                .setFileId(fileId)
                .setIndex(index)
                .setSize(size)
                .setMd5(md5)
                .setPath(path);

        fileApi.createFilePart(filePart);
        log.info("part{}: {}", index, filePart);
        return md5;
    }

    @Override
    public void abortPart(Long fileId) {
    }

    @Override
    public String completePart(Long fileId) {
        //准备参数
        FileDto file = fileApi.findFile(fileId);
        List<FilePartDto> partList = fileApi.listByFileId(fileId);
        Vector<InputStream> vector = new Vector<>(partList.size());

        //文件属性收集
        int size = 0;
        String domain = config.getEndpoint();
        String path = getDatePath() + getUUID() + getSuffix(file.getFilename());
        String url = domain + "/file" + path;

        //创建文件流序列,合并文件
        for (FilePartDto filePart : partList) {
            InputStream in = createIn(buildLocalPathStr(filePart.getPath()));
            size += getSize(in);
            vector.add(in);
        }
        merge(vector.elements(), path);

        file.setComplete(TRUE.getCode())
                .setSize(size)
                .setDomain(domain)
                .setPath(path)
                .setUrl(url);

        fileApi.updateFile(file);
        return url;
    }

    @Override
    public String getType() {
        return FileUploadTypeEnum.LOCAL.getCode();
    }

    @PostConstruct
    public void loadConfig() {
        config = fileProperties.getConfig().get(FileUploadTypeEnum.LOCAL.getCode());
    }

    private File buildLocalPathFile(String path) {
        return new File(config.getPath() + path);
    }

    private String buildLocalPathStr(String path) {
        return config.getPath() + path;
    }
}
