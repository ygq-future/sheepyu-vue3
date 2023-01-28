package top.sheepyu.module.system.service.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.framework.mybatisplus.core.query.ServiceImplX;
import top.sheepyu.framework.web.util.WebFrameworkUtil;
import top.sheepyu.module.system.api.file.FileDto;
import top.sheepyu.module.system.dao.file.SystemFile;
import top.sheepyu.module.system.dao.file.SystemFileMapper;

import java.time.LocalDateTime;

import static top.sheepyu.module.common.exception.CommonException.exception;
import static top.sheepyu.module.system.constants.ErrorCodeConstants.FILE_NOT_EXISTS;
import static top.sheepyu.module.system.convert.file.SystemFileConvert.CONVERT;

/**
 * @author ygq
 * @date 2023-01-25 16:14
 **/
@Service
@Slf4j
@Validated
public class SystemFileServiceImpl extends ServiceImplX<SystemFileMapper, SystemFile> implements SystemFileService {
    @Override
    public FileDto createFile(FileDto dto) {
        //根据md5查询文件是否存在
        SystemFile file = findFileByMd5(dto.getMd5());

        //如果文件为空, 说明文件已经上传过了或者压根没有这个文件,那么本次就算新的文件, 保存即可
        if (file == null) {
            file = CONVERT.convert(dto).setCreator(WebFrameworkUtil.getLoginUserUsername()).setCreateTime(LocalDateTime.now());
            save(file);
        }

        return CONVERT.convertDto(file);
    }

    @Override
    public void updateFileByUploadId(FileDto dto) {
        SystemFile file = CONVERT.convert(dto);
        update(file, buildQuery().eq(SystemFile::getUploadId, file.getUploadId()));
    }

    @Override
    public SystemFile findFileByUploadId(String uploadId) {
        SystemFile file = lambdaQuery().eq(SystemFile::getUploadId, uploadId).one();
        if (file == null) {
            throw exception(FILE_NOT_EXISTS);
        }
        return file;
    }

    @Override
    public void updatePartIndex(String uploadId, Integer partIndex) {
        lambdaUpdate().eq(SystemFile::getUploadId, uploadId)
                .set(SystemFile::getPartIndex, partIndex)
                .update();
    }

    @Override
    public SystemFile findFileByMd5(String md5) {
        return lambdaQuery().eq(SystemFile::getMd5, md5).one();
    }

    @Override
    public boolean deleteFileByUploadId(String uploadId) {
        return lambdaUpdate().eq(SystemFile::getUploadId, uploadId).remove();
    }

    private SystemFile findByIdValidateExists(Long id) {
        return findByIdValidateExists(id, FILE_NOT_EXISTS);
    }
}
