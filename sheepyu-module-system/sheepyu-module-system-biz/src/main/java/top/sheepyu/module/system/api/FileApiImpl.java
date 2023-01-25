package top.sheepyu.module.system.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.sheepyu.module.system.dto.FileDto;
import top.sheepyu.module.system.dto.FilePartDto;
import top.sheepyu.module.system.service.file.SystemFilePartService;
import top.sheepyu.module.system.service.file.SystemFileService;

import javax.annotation.Resource;
import java.util.List;

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
    public void createFilePart(FilePartDto dto) {
        systemFilePartService.createFilePart(dto);
    }

    @Override
    public void deleteFilePart(Long fileId) {
        systemFilePartService.deleteFilePart(fileId);
    }

    @Override
    public List<FilePartDto> listByFileId(Long fileId) {
        return systemFilePartService.listByFileId(fileId);
    }

    @Override
    public Long createFile(FileDto dto) {
        return systemFileService.createFile(dto);
    }

    @Override
    public void updateFile(FileDto dto) {
        systemFileService.updateFile(dto);
    }
}
