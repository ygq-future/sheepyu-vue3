package top.sheepyu.module.system.controller.admin.codegen;

import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.sheepyu.module.common.common.Result;
import top.sheepyu.module.system.controller.admin.codegen.vo.TableInfoRespVo;
import top.sheepyu.module.system.service.codegen.SystemCodegenBiz;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ygq
 * @date 2023-03-03 19:46
 **/
@RestController
@RequestMapping("/system/codegen")
public class SystemCodegenController {
    @Resource
    private SystemCodegenBiz systemCodegenBiz;

    @GetMapping("/table-list")
    @ApiOperation("修改系统配置")
    @PreAuthorize("@ss.hasPermission('system:codegen:query')")
    public Result<List<TableInfoRespVo>> tableList() {
        return Result.success(systemCodegenBiz.tableList());
    }
}
