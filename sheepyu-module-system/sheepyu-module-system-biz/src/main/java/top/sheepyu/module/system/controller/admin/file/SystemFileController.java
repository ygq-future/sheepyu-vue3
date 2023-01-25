package top.sheepyu.module.system.controller.admin.file;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.sheepyu.framework.security.core.annotations.Permit;
import top.sheepyu.module.common.common.Result;
import top.sheepyu.module.system.service.file.SystemFileService;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author ygq
 * @date 2023-01-25 17:52
 **/
@RestController
@RequestMapping("/system/file")
@Api(tags = "管理端 - 系统文件")
public class SystemFileController {
    @Resource
    private SystemFileService systemFileService;

    @Permit
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(@RequestParam MultipartFile file,
                                 @RequestParam(required = false) String remark) throws IOException {
        String url = systemFileService.uploadFile(file, remark);
        return Result.success(url);
    }
}
