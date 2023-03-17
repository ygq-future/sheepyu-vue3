package top.sheepyu.module.system.convert.file;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.system.api.file.FileDto;
import top.sheepyu.module.system.api.file.FilePartDto;
import top.sheepyu.module.system.controller.admin.file.vo.SystemFileRespVo;
import top.sheepyu.module.system.dao.file.SystemFile;
import top.sheepyu.module.system.dao.file.SystemFilePart;

import java.util.List;

/**
 * @author ygq
 * @date 2023-01-25 15:59
 **/
@Mapper
public interface SystemFileConvert {
    SystemFileConvert CONVERT = Mappers.getMapper(SystemFileConvert.class);

    SystemFilePart convert(FilePartDto dto);

    SystemFile convert(FileDto dto);

    SystemFileRespVo convert(SystemFile file);

    FileDto convertDto(SystemFile file);

    List<FilePartDto> convertDtoList(List<SystemFilePart> list);

    PageResult<SystemFileRespVo> convertPage(PageResult<SystemFile> pageResult);
}
