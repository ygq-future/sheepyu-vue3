package top.sheepyu.module.system.api.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.sheepyu.module.system.dao.file.SystemFilePart;
import top.sheepyu.module.system.service.file.SystemFilePartService;
import top.sheepyu.module.system.service.file.SystemFileService;

import javax.annotation.Resource;
import java.util.List;

import static top.sheepyu.module.system.convert.file.SystemFileConvert.CONVERT;

/**
 * @author ygq
 * @date 2023-01-25 16:37
 **/
@Service
@Slf4j
public class FileApiImpl implements FileApi {
    @Resource
    private SystemFilePartService systemFilePartService;
    @Resource
    private SystemFileService systemFileService;

    @Override
    public boolean createFilePart(FilePartDto dto) {
        return systemFilePartService.createFilePart(dto);
    }

    @Override
    public void deletePartByUploadId(String uploadId) {
        systemFilePartService.deletePartByUploadId(uploadId);
    }

    @Override
    public List<FilePartDto> listByUploadId(String uploadId) {
        List<SystemFilePart> parts = systemFilePartService.listByUploadId(uploadId);
        return CONVERT.convertDtoList(parts);
    }

    @Override
    public void updateFileByUploadId(FileDto dto) {
        systemFileService.updateFileByUploadId(dto);
    }
}
