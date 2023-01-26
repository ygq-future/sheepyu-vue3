package top.sheepyu.module.system.service.file;

import org.springframework.web.multipart.MultipartFile;
import top.sheepyu.framework.mybatisplus.core.query.IServiceX;
import top.sheepyu.module.system.dao.file.SystemFile;
import top.sheepyu.module.system.dto.FileDto;

import javax.validation.Valid;
import java.io.IOException;

/**
 * @author ygq
 * @date 2023-01-25 16:13
 **/
public interface SystemFileService extends IServiceX<SystemFile> {
    Long createFile(@Valid FileDto dto);

    void updateFile(@Valid FileDto dto);

    String uploadFile(MultipartFile file, String remark) throws IOException;

    FileDto findFile(Long fileId);
}
