package top.sheepyu.framework.file.core.oss.local;

import cn.hutool.core.lang.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.sheepyu.framework.file.config.BaseConfig;
import top.sheepyu.framework.file.config.FileProperties;
import top.sheepyu.framework.file.core.enums.FileUploadTypeEnum;
import top.sheepyu.framework.file.core.oss.FileUpload;
import top.sheepyu.module.common.util.FileUtil;
import top.sheepyu.module.system.api.FileApi;
import top.sheepyu.module.system.dto.FileDto;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static top.sheepyu.module.common.enums.status.StatusEnum.TRUE;

/**
 * @author ygq
 * @date 2023-01-25 16:42
 **/
@Component
@Slf4j
public class LocalFileUpload implements FileUpload {
    @Resource
    private FileProperties fileProperties;
    @Resource
    private FileApi fileApi;
    public BaseConfig config;

    @Override
    public String upload(InputStream in, String filename, String remark) {
        int size = FileUtil.getSize(in);
        String domain = config.getEndpoint();
        String mimeType = FileUtil.getMimeType(filename);
        String randomFilename = UUID.fastUUID().toString(true) + FileUtil.getSuffix(filename);
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("/yyyy/MM/dd/"));
        String path = date + randomFilename;
        FileUtil.writeFromStream(in, new File(config.getPath() + path));

        FileDto file = new FileDto()
                .setComplete(TRUE.getCode())
                .setFilename(filename)
                .setSize(size)
                .setDomain(domain)
                .setMimeType(mimeType)
                .setPath(path)
                .setUrl(domain + "/file" + path)
                .setRemark(remark);

        fileApi.createFile(file);
        return file.getUrl();
    }

    @Override
    public String uploadPart(InputStream is) {
        return null;
    }

    @Override
    public void abortPart(String fileId) {

    }

    @Override
    public String completePart(String fileId) {
        return null;
    }

    @Override
    public String getType() {
        return FileUploadTypeEnum.LOCAL.getCode();
    }

    @PostConstruct
    public void loadConfig() {
        config = fileProperties.getConfig().get(FileUploadTypeEnum.LOCAL.getCode());
    }
}
