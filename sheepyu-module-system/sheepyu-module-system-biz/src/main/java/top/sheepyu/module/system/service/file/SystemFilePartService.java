package top.sheepyu.module.system.service.file;

import top.sheepyu.framework.mybatisplus.core.query.IServiceX;
import top.sheepyu.module.system.dao.file.SystemFilePart;
import top.sheepyu.module.system.api.file.FilePartDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author ygq
 * @date 2023-01-25 15:30
 **/
public interface SystemFilePartService extends IServiceX<SystemFilePart> {
    void createFilePart(@Valid FilePartDto dto);

    void deleteFilePart(@NotNull(message = "文件id不能为空") Long fileId);

    List<FilePartDto> listByFileId(Long fileId);

    Integer findIndexByFileId(Long id);
}
