package top.sheepyu.module.system.controller.app.file;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.sheepyu.module.common.common.Result;
import top.sheepyu.module.system.service.file.SystemFileService;

import javax.annotation.Resource;
import java.io.IOException;

import static top.sheepyu.module.common.common.Result.success;

/**
 * @author ygq
 * @date 2023-05-20 20:52
 **/
@RestController
@RequestMapping("/system/file")
@Api(tags = "用户端 - 文件上传")
public class AppSystemFileController {
    @Resource
    private SystemFileService systemFileService;

    @PostMapping("/simple-upload")
    @ApiOperation("简单文件上传(不会保存到数据库)")
    public Result<String> simpleUpload(@RequestParam MultipartFile file) throws IOException {
        String url = systemFileService.simpleUpload(file);
        return success(url);
    }
}
