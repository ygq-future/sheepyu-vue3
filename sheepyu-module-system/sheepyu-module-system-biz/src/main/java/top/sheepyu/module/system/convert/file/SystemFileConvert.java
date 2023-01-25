package top.sheepyu.module.system.convert.file;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import top.sheepyu.module.system.dao.file.SystemFile;
import top.sheepyu.module.system.dao.file.SystemFilePart;
import top.sheepyu.module.system.dto.FileDto;
import top.sheepyu.module.system.dto.FilePartDto;

import java.util.List;

/**
 * @author ygq
 * @date 2023-01-25 15:59
 **/
@Mapper
public interface SystemFileConvert {
    SystemFileConvert CONVERT = Mappers.getMapper(SystemFileConvert.class);

    SystemFilePart convert(FilePartDto dto);

    List<FilePartDto> convertList(List<SystemFilePart> list);

    SystemFile convert(FileDto dto);
}
