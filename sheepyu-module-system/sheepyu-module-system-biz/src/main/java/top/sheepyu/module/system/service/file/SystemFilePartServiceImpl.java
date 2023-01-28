package top.sheepyu.module.system.service.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.framework.mybatisplus.core.query.ServiceImplX;
import top.sheepyu.module.system.api.file.FilePartDto;
import top.sheepyu.module.system.dao.file.SystemFilePart;
import top.sheepyu.module.system.dao.file.SystemFilePartMapper;

import java.util.List;
import java.util.Objects;

import static top.sheepyu.module.system.convert.file.SystemFileConvert.CONVERT;

/**
 * @author ygq
 * @date 2023-01-25 15:31
 **/
@Service
@Slf4j
@Validated
public class SystemFilePartServiceImpl extends ServiceImplX<SystemFilePartMapper, SystemFilePart> implements SystemFilePartService {

    @Override
    public boolean createFilePart(FilePartDto dto) {
        //根据上传id和索引找文件的部分
        SystemFilePart filePart = lambdaQuery().eq(SystemFilePart::getUploadId, dto.getUploadId())
                .eq(SystemFilePart::getPartIndex, dto.getPartIndex())
                .one();

        //如果为空说明这一部分没有上传过, 直接保存
        if (filePart == null) {
            save(CONVERT.convert(dto));
            return true;
        }

        //如果不为空,而且md5值还不相同,说明之前上传过这一个文件,但是失败了
        // 还修改了文件内容导致md5不一样, 然后还重新上传了, 这时候就更新一下md5
        //否则就是md5也相同, 直接跳过即可
        if (!Objects.equals(dto.getMd5(), filePart.getMd5())) {
            filePart.setMd5(dto.getMd5());
            updateById(filePart);
            return true;
        }
        return false;
    }

    @Override
    public void deletePartByUploadId(String uploadId) {
        lambdaUpdate().eq(SystemFilePart::getUploadId, uploadId).remove();
    }

    @Override
    public List<SystemFilePart> listByUploadId(String uploadId) {
        return lambdaQuery().eq(SystemFilePart::getUploadId, uploadId)
                .orderByAsc(SystemFilePart::getPartIndex).list();
    }
}
