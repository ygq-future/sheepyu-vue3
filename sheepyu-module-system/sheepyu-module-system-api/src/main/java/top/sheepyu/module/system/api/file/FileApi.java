package top.sheepyu.module.system.api.file;

import java.util.List;

/**
 * @author ygq
 * @date 2023-01-25 16:09
 **/
public interface FileApi {
    void createFilePart(FilePartDto dto);

    void deleteFilePart(Long fileId);

    List<FilePartDto> listByFileId(Long fileId);

    FileDto createFile(FileDto dto);

    void updateFile(FileDto dto);

    FileDto findFile(Long fileId);
}
