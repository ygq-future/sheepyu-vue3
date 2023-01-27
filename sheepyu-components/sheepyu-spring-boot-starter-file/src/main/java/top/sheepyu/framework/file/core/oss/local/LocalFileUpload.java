package top.sheepyu.framework.file.core.oss.local;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.framework.file.config.FileProperties;
import top.sheepyu.framework.file.config.FileProperties.BaseConfig;
import top.sheepyu.framework.file.core.enums.FileUploadTypeEnum;
import top.sheepyu.framework.file.core.oss.FileUpload;
import top.sheepyu.module.common.constants.ErrorCodeConstants;
import top.sheepyu.module.common.util.FileUtil;
import top.sheepyu.module.system.api.file.FileApi;
import top.sheepyu.module.system.api.file.FileDto;
import top.sheepyu.module.system.api.file.FilePartDto;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.InputStream;
import java.util.List;
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
    public BaseConfig config;

    @Override
    public String upload(InputStream in, String md5, String filename, String remark) {
        //获取基本信息
        int size = getSize(in);
        String domain = config.getEndpoint();
        String mimeType = getMimeType(filename);
        String path = getDatePath() + getUUID() + getSuffix(filename);
        String url = domain + "/file" + path;

        //写入磁盘, 如果md5为空会计算md5
        if (StrUtil.isBlank(md5)) {
            byte[] data = readAllData(in);
            md5 = MD5.create().digestHex(data);
            writeBytes(data, buildLocalPathFile(path));
        } else {
            writeFromStream(in, buildLocalPathFile(path));
        }

        //创建文件
        FileDto file = new FileDto()
                .setComplete(TRUE.getCode())
                .setMd5(md5)
                .setFilename(filename)
                .setSize(size)
                .setDomain(domain)
                .setMimeType(mimeType)
                .setPath(path)
                .setUrl(url)
                .setRemark(remark);
        return fileApi.createFile(file).getUrl();
    }

    @Override
    public String uploadMini(InputStream in, String filename) {
        return upload(in, null, filename, null);
    }

    @Override
    public Long preparePart(String md5, String filename, String remark) {
        //封装基本属性
        String mimeType = getMimeType(filename);
        FileDto file = new FileDto()
                .setComplete(FALSE.getCode())
                .setMd5(md5)
                .setFilename(filename)
                .setMimeType(mimeType)
                .setRemark(remark);
        file = fileApi.createFile(file);
        if (StrUtil.isNotBlank(file.getUrl())) {
            throw exception(ErrorCodeConstants.EXISTS);
        }
        return file.getId();
    }

    @Override
    public String uploadPart(Long fileId, InputStream in, int index) {
        //获取基本信息
        byte[] data = FileUtil.readAllData(in);
        int size = data.length;
        String md5 = MD5.create().digestHex(data);
        String path = getDatePath() + fileId + "/" + getUUID();
        //写入磁盘
        FileUtil.writeBytes(data, buildLocalPathFile(path));

        //创建文件分片信息
        FilePartDto filePart = new FilePartDto()
                .setFileId(fileId)
                .setIdx(index)
                .setSize(size)
                .setMd5(md5)
                .setPath(path);
        fileApi.createFilePart(filePart);
        return md5;
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
        //合并流
        merge(vector.elements(), buildLocalPathStr(path));

        //设置参数, 更新文件信息
        file.setComplete(TRUE.getCode())
                .setSize(size)
                .setDomain(domain)
                .setPath(path)
                .setUrl(url);
        fileApi.updateFile(file);

        //清理文件分片信息
        del(new File(buildLocalPathStr(partList.get(0).getPath())).getParentFile());
        fileApi.deleteFilePart(fileId);
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
