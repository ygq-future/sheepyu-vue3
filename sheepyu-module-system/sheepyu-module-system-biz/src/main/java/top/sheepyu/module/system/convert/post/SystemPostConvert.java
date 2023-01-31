package top.sheepyu.module.system.convert.post;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.system.controller.admin.post.vo.SystemPostCreateVo;
import top.sheepyu.module.system.controller.admin.post.vo.SystemPostRespVo;
import top.sheepyu.module.system.controller.admin.post.vo.SystemPostUpdateVo;
import top.sheepyu.module.system.dao.post.SystemPost;

import java.util.List;

/**
 * @author ygq
 * @date 2023-01-29 18:01
 **/
@Mapper
public interface SystemPostConvert {
    SystemPostConvert CONVERT = Mappers.getMapper(SystemPostConvert.class);

    SystemPost convert(SystemPostCreateVo createVo);

    SystemPost convert(SystemPostUpdateVo updateVo);

    SystemPostRespVo convert(SystemPost systemConfig);

    List<SystemPostRespVo> convertList(List<SystemPost> list);

    PageResult<SystemPostRespVo> convertPage(PageResult<SystemPost> pageResult);
}
