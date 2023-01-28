package top.sheepyu.module.system.service.file;

import top.sheepyu.framework.mybatisplus.core.query.IServiceX;
import top.sheepyu.module.system.api.file.FileDto;
import top.sheepyu.module.system.dao.file.SystemFile;

import javax.validation.Valid;

/**
 * @author ygq
 * @date 2023-01-25 16:13
 **/
public interface SystemFileService extends IServiceX<SystemFile> {
    FileDto createFile(@Valid FileDto dto);

    void updateFileByUploadId(@Valid FileDto dto);

    SystemFile findFileByUploadId(String uploadId);

    void updatePartIndex(String uploadId, Integer partIndex);

    SystemFile findFileByMd5(String md5);

    boolean deleteFileByUploadId(String uploadId);
}
