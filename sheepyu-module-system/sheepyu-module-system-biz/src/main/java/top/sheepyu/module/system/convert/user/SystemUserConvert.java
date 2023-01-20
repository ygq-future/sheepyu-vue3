package top.sheepyu.module.system.convert.user;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.system.controller.admin.user.vo.SystemUserCreateVo;
import top.sheepyu.module.system.controller.admin.user.vo.SystemUserExcelVo;
import top.sheepyu.module.system.controller.admin.user.vo.SystemUserRespVo;
import top.sheepyu.module.system.controller.admin.user.vo.SystemUserUpdateVo;
import top.sheepyu.module.system.dao.user.SystemUser;

import java.util.List;

/**
 * @author ygq
 * @date 2023-01-18 14:41
 **/
@Mapper
public interface SystemUserConvert {
    SystemUserConvert CONVERT = Mappers.getMapper(SystemUserConvert.class);

    SystemUser convert(SystemUserCreateVo createVo);

    SystemUser convert(SystemUserUpdateVo updateVo);

    SystemUserRespVo convert(SystemUser systemUser);

    List<SystemUserExcelVo> convertExcel(List<SystemUser> list);

    PageResult<SystemUserRespVo> convertPage(PageResult<SystemUser> pageResult);
}
