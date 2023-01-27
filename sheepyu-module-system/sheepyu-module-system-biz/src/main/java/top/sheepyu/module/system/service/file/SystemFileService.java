package top.sheepyu.module.system.service.file;

import org.springframework.web.multipart.MultipartFile;
import top.sheepyu.framework.mybatisplus.core.query.IServiceX;
import top.sheepyu.module.system.dao.file.SystemFile;
import top.sheepyu.module.system.api.file.FileDto;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author ygq
 * @date 2023-01-25 16:13
 **/
public interface SystemFileService extends IServiceX<SystemFile> {
    FileDto createFile(@Valid FileDto dto);

    void updateFile(@Valid FileDto dto);

    String uploadFile(MultipartFile file, String md5, String remark) throws IOException;

    FileDto findFile(Long fileId);

    Long preparePart(String md5, String filename, String remark);

    String uploadPart(Long fileId, InputStream inputStream, Integer index);

    String completePart(Long fileId);

    SystemFile findFileByMd5(String md5);
}
