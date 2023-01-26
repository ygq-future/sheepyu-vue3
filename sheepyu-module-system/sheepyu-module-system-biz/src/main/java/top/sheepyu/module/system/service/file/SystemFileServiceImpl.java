package top.sheepyu.module.system.service.file;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import top.sheepyu.framework.file.config.FileUploadFactory;
import top.sheepyu.framework.file.core.oss.FileUpload;
import top.sheepyu.framework.mybatisplus.core.query.ServiceImplX;
import top.sheepyu.module.system.dao.file.SystemFile;
import top.sheepyu.module.system.dao.file.SystemFileMapper;
import top.sheepyu.module.system.dto.FileDto;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Objects;

import static top.sheepyu.framework.web.util.WebFrameworkUtil.getLoginUserUsername;
import static top.sheepyu.module.common.enums.status.StatusEnum.TRUE;
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
    public Long createFile(FileDto dto) {
        //根据登录用户和文件名查找上传的文件
        SystemFile file = lambdaQuery().eq(SystemFile::getFilename, dto.getFilename())
                .eq(SystemFile::getCreator, getLoginUserUsername()).one();

        //如果文件为空或者已经上传过了,那么本次就算新的文件, 保存即可
        //否则就是文件不为空且上传失败, 直接返回上一次上传失败的文件id即可
        if (file == null || Objects.equals(file.getComplete(), TRUE.getCode())) {
            file = CONVERT.convert(dto);
            save(file);
        }

        return file.getId();
    }

    @Override
    public void updateFile(FileDto dto) {
        SystemFile file = findByIdValidateExists(dto.getId());
        BeanUtil.copyProperties(dto, file);
        updateById(file);
    }

    @Override
    public String uploadFile(MultipartFile file, String remark) throws IOException {
        FileUpload fileUpload = fileUploadFactory.get();
        return fileUpload.upload(file.getInputStream(), file.getOriginalFilename(), remark);
    }

    @Override
    public FileDto findFile(Long fileId) {
        return CONVERT.convert(findByIdValidateExists(fileId));
    }

    private SystemFile findByIdValidateExists(Long id) {
        return findByIdValidateExists(id, FILE_NOT_EXISTS);
    }
}
