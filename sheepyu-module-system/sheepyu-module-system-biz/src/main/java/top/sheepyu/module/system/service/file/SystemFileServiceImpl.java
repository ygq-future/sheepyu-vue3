package top.sheepyu.module.system.service.file;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import top.sheepyu.framework.file.config.FileUploadFactory;
import top.sheepyu.framework.file.core.oss.FileUpload;
import top.sheepyu.framework.mybatisplus.core.query.ServiceImplX;
import top.sheepyu.module.system.api.file.FileDto;
import top.sheepyu.module.system.dao.file.SystemFile;
import top.sheepyu.module.system.dao.file.SystemFileMapper;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;

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
    @Resource
    private FileUploadFactory fileUploadFactory;

    @Override
    public FileDto createFile(FileDto dto) {
        //根据md5查询文件是否存在
        SystemFile file = findFileByMd5(dto.getMd5());

        //如果文件为空, 说明文件已经上传过了或者压根没有这个文件,那么本次就算新的文件, 保存即可
        if (file == null) {
            file = CONVERT.convert(dto);
            save(file);
        }

        return CONVERT.convertDto(file);
    }

    @Override
    public void updateFile(FileDto dto) {
        SystemFile file = findByIdValidateExists(dto.getId());
        BeanUtil.copyProperties(dto, file);
        updateById(file);
    }

    @Override
    public String uploadFile(MultipartFile file, String md5, String remark) throws IOException {
        FileUpload fileUpload = fileUploadFactory.get();
        return fileUpload.upload(file.getInputStream(), md5, file.getOriginalFilename(), remark);
    }

    @Override
    public FileDto findFile(Long fileId) {
        return CONVERT.convertDto(findByIdValidateExists(fileId));
    }

    @Override
    public Long preparePart(String md5, String filename, String remark) {
        FileUpload fileUpload = fileUploadFactory.get();
        return fileUpload.preparePart(md5, filename, remark);
    }

    @Override
    public String uploadPart(Long fileId, InputStream inputStream, Integer index) {
        FileUpload fileUpload = fileUploadFactory.get();
        return fileUpload.uploadPart(fileId, inputStream, index);
    }

    @Override
    public String completePart(Long fileId) {
        FileUpload fileUpload = fileUploadFactory.get();
        return fileUpload.completePart(fileId);
    }

    @Override
    public SystemFile findFileByMd5(String md5) {
        return lambdaQuery().eq(SystemFile::getMd5, md5).one();
    }

    private SystemFile findByIdValidateExists(Long id) {
        return findByIdValidateExists(id, FILE_NOT_EXISTS);
    }
}
