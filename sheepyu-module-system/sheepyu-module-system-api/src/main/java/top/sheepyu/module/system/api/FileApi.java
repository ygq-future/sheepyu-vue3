package top.sheepyu.module.system.api;

import top.sheepyu.module.system.dto.FileDto;
import top.sheepyu.module.system.dto.FilePartDto;

import java.util.List;

/**
 * @author ygq
 * @date 2023-01-25 16:09
 **/
public interface FileApi {
    void createFilePart(FilePartDto dto);

    void deleteFilePart(Long fileId);

    List<FilePartDto> listByFileId(Long fileId);

    Long createFile(FileDto dto);

    void updateFile(FileDto dto);

    FileDto findFile(Long fileId);
}
