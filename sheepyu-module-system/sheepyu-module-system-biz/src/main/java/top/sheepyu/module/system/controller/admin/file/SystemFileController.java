package top.sheepyu.module.system.controller.admin.file;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.sheepyu.module.common.common.Result;
import top.sheepyu.module.system.controller.admin.file.vo.SystemFileRespVo;
import top.sheepyu.module.system.convert.file.SystemFileConvert;
import top.sheepyu.module.system.dao.file.SystemFile;
import top.sheepyu.module.system.service.file.SystemFilePartService;
import top.sheepyu.module.system.service.file.SystemFileService;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Objects;

import static top.sheepyu.module.common.enums.status.StatusEnum.FALSE;

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
    private SystemFilePartService systemFilePartService;

    @GetMapping("/checkMd5/{md5}")
    @ApiOperation("检查是否已经有这个md5的文件")
    @PreAuthorize("@ss.hasPermission('system:file:create')")
    public Result<SystemFileRespVo> checkMd5(@PathVariable String md5) {
        SystemFile file = systemFileService.findFileByMd5(md5);
        SystemFileRespVo resp = SystemFileConvert.CONVERT.convert(file);
        if (file != null && Objects.equals(file.getComplete(), FALSE.getCode())) {
            Integer index = systemFilePartService.findIndexByFileId(file.getId());
            resp.setIndex(index);
        }
        return Result.success(resp);
    }

    @PostMapping("/upload")
    @ApiOperation("文件上传")
    @PreAuthorize("@ss.hasPermission('system:file:create')")
    public Result<String> upload(@RequestParam MultipartFile file,
                                 @RequestParam String md5,
                                 @RequestParam(required = false) String remark) throws IOException {
        String url = systemFileService.uploadFile(file, md5, remark);
        return Result.success(url);
    }

    @PostMapping("/preparePart")
    @ApiOperation("准备分片上传")
    @PreAuthorize("@ss.hasPermission('system:file:create')")
    public Result<Long> preparePart(@RequestParam String md5,
                                    @RequestParam String filename,
                                    @RequestParam(required = false) String remark) {
        Long fileId = systemFileService.preparePart(md5, filename, remark);
        return Result.success(fileId);
    }

    @PostMapping("/uploadPart/{fileId}")
    @ApiOperation("分片上传")
    @PreAuthorize("@ss.hasPermission('system:file:create')")
    public Result<String> uploadPart(@PathVariable Long fileId,
                                     @RequestParam MultipartFile file,
                                     @RequestParam Integer index) throws IOException {
        String md5 = systemFileService.uploadPart(fileId, file.getInputStream(), index);
        return Result.success(md5);
    }

    @PostMapping("/completePart/{fileId}")
    @ApiOperation("完成分片上传")
    @PreAuthorize("@ss.hasPermission('system:file:create')")
    public Result<String> completePart(@PathVariable Long fileId) {
        String url = systemFileService.completePart(fileId);
        return Result.success(url);
    }
}
