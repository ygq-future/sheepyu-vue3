package top.sheepyu.module.system.controller.admin.file;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.sheepyu.framework.file.config.FileUploadFactory;
import top.sheepyu.module.common.common.Result;
import top.sheepyu.module.system.controller.admin.file.vo.SystemFileRespVo;
import top.sheepyu.module.system.convert.file.SystemFileConvert;
import top.sheepyu.module.system.dao.file.SystemFile;
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
    @Resource
    private FileUploadFactory fileUploadFactory;

    @GetMapping("/checkMd5/{md5}")
    @ApiOperation("检查是否已经有这个md5的文件")
    @PreAuthorize("@ss.hasPermission('system:file:create')")
    public Result<SystemFileRespVo> checkMd5(@PathVariable String md5) {
        SystemFile file = systemFileService.findFileByMd5(md5);
        return Result.success(SystemFileConvert.CONVERT.convert(file));
    }

    @DeleteMapping("/delete/{uploadId}")
    @ApiOperation("根据uploadId删除文件")
    @PreAuthorize("@ss.hasPermission('system:file:delete')")
    public Result<Boolean> delete(@PathVariable String uploadId) {
        return Result.success(fileUploadFactory.get().deleteFile(uploadId));
    }

    @PostMapping("/upload")
    @ApiOperation("文件上传")
    @PreAuthorize("@ss.hasPermission('system:file:create')")
    public Result<String> upload(@RequestParam MultipartFile file,
                                 @RequestParam String md5,
                                 @RequestParam(required = false) String remark) throws IOException {
        String url = fileUploadFactory.get().upload(file.getInputStream(), md5, file.getOriginalFilename(), remark);
        return Result.success(url);
    }

    @PostMapping("/preparePart")
    @ApiOperation("准备分片上传")
    @PreAuthorize("@ss.hasPermission('system:file:create')")
    public Result<String> preparePart(@RequestParam String md5,
                                      @RequestParam String filename,
                                      @RequestParam(required = false) String remark) {
        String uploadId = fileUploadFactory.get().preparePart(md5, filename, remark);
        return Result.success(uploadId);
    }

    @PostMapping("/uploadPart/{uploadId}")
    @ApiOperation("分片上传")
    @PreAuthorize("@ss.hasPermission('system:file:create')")
    public Result<String> uploadPart(@PathVariable String uploadId,
                                     @RequestParam MultipartFile file,
                                     @RequestParam Integer index) throws IOException {
        String md5 = fileUploadFactory.get().uploadPart(uploadId, file.getInputStream(), index);
        return Result.success(md5);
    }

    @PostMapping("/completePart/{uploadId}")
    @ApiOperation("完成分片上传")
    @PreAuthorize("@ss.hasPermission('system:file:create')")
    public Result<String> completePart(@PathVariable String uploadId) {
        String url = fileUploadFactory.get().completePart(uploadId);
        return Result.success(url);
    }
}
