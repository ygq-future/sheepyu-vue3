package top.sheepyu.module.system.service.file;

import top.sheepyu.framework.mybatisplus.core.query.IServiceX;
import top.sheepyu.module.system.api.file.FilePartDto;
import top.sheepyu.module.system.dao.file.SystemFilePart;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author ygq
 * @date 2023-01-25 15:30
 **/
public interface SystemFilePartService extends IServiceX<SystemFilePart> {
    /**
     * 文件创建失败返回false, 反之
     *
     * @param dto dto
     * @return boolean
     */
    boolean createFilePart(@Valid FilePartDto dto);

    void deletePartByUploadId(@NotNull(message = "文件id不能为空") String uploadId);

    List<SystemFilePart> listByUploadId(String uploadId);
}
