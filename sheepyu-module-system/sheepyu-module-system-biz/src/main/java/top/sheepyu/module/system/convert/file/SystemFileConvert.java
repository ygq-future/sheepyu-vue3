package top.sheepyu.module.system.convert.file;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
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

    List<FilePartDto> convertDtoList(List<SystemFilePart> list);

    SystemFile convert(FileDto dto);

    FileDto convertDto(SystemFile file);

    SystemFileRespVo convert(SystemFile file);
}
