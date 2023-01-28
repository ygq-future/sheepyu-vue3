package top.sheepyu.module.system.api.file;

import java.util.List;

/**
 * @author ygq
 * @date 2023-01-25 16:09
 **/
public interface FileApi {
    boolean createFilePart(FilePartDto dto);

    void deletePartByUploadId(String uploadId);

    List<FilePartDto> listByUploadId(String uploadId);

    FileDto createFile(FileDto dto);

    void updateFileByUploadId(FileDto dto);

    FileDto findFileByUploadId(String uploadId);

    boolean deleteFileByUploadId(String uploadId);

    void updatePartIndex(String uploadId, int partIndex);
}
