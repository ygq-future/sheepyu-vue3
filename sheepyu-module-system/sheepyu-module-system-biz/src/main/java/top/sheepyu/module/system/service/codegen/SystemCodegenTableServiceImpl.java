package top.sheepyu.module.system.service.codegen;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.framework.mybatisplus.core.query.ServiceImplX;
import top.sheepyu.module.system.dao.codegen.SystemCodegenTable;
import top.sheepyu.module.system.dao.codegen.SystemCodegenTableMapper;

/**
 * @author ygq
 * @date 2023-03-01 21:13
 **/
@Service
@Validated
@Slf4j
public class SystemCodegenTableServiceImpl extends ServiceImplX<SystemCodegenTableMapper, SystemCodegenTable> implements SystemCodegenTableService {

}
