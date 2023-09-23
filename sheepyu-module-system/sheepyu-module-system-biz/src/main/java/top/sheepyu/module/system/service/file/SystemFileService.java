package top.sheepyu.module.system.service.file;

import org.springframework.web.multipart.MultipartFile;
import top.sheepyu.framework.mybatisplus.core.query.IServiceX;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.system.api.file.FileDto;
import top.sheepyu.module.system.controller.admin.file.vo.SystemFileQueryVo;
import top.sheepyu.module.system.controller.admin.file.vo.SystemFileStatisticsVo;
import top.sheepyu.module.system.dao.file.SystemFile;

import javax.validation.Valid;
import java.io.IOException;

/**
 * @author ygq
 * @date 2023-01-25 16:13
 **/
public interface SystemFileService extends IServiceX<SystemFile> {
    void updateFileByUploadId(@Valid FileDto dto);

    SystemFile findFileByUploadId(String uploadId);

    SystemFile findFileByMd5(String md5);

    PageResult<SystemFile> page(@Valid SystemFileQueryVo queryVo);

    void delete(String ids);

    String simpleUpload(MultipartFile file) throws IOException;

    String upload(MultipartFile file, String md5, String remark) throws IOException;

    String preparePart(String md5, String filename, String remark);

    String uploadPart(MultipartFile part, String uploadId, Integer index) throws IOException;

    String completePart(String uploadId);

    SystemFileStatisticsVo statistics();
}
