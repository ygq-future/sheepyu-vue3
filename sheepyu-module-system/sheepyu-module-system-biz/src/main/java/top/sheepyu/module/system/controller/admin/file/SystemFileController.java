package top.sheepyu.module.system.controller.admin.file;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.common.common.Result;
import top.sheepyu.module.system.controller.admin.file.vo.SystemFileQueryVo;
import top.sheepyu.module.system.controller.admin.file.vo.SystemFileRespVo;
import top.sheepyu.module.system.controller.admin.file.vo.SystemFileStatisticsVo;
import top.sheepyu.module.system.dao.file.SystemFile;
import top.sheepyu.module.system.service.file.SystemFileService;

import javax.annotation.Resource;
import java.io.IOException;

import static top.sheepyu.module.common.common.Result.success;
import static top.sheepyu.module.system.convert.file.SystemFileConvert.CONVERT;

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

    @GetMapping("/checkMd5/{md5}")
    @ApiOperation("检查是否已经有这个md5的文件")
    @PreAuthorize("@ss.hasPermission('system:file:create')")
    public Result<SystemFileRespVo> checkMd5(@PathVariable String md5) {
        SystemFile file = systemFileService.findFileByMd5(md5);
        return success(CONVERT.convert(file));
    }

    @DeleteMapping("/{ids}")
    @ApiOperation("批量删除文件")
    @PreAuthorize("@ss.hasPermission('system:file:delete')")
    public Result<Boolean> delete(@PathVariable String ids) {
        systemFileService.delete(ids);
        return success(true);
    }

    @PostMapping("/upload")
    @ApiOperation("文件上传")
    @PreAuthorize("@ss.hasPermission('system:file:create')")
    public Result<String> upload(@RequestParam MultipartFile file,
                                 @RequestParam String md5,
                                 @RequestParam(required = false) String remark) throws IOException {
        String url = systemFileService.upload(file, md5, remark);
        return success(url);
    }

    @PostMapping("/preparePart")
    @ApiOperation("准备分片上传")
    @PreAuthorize("@ss.hasPermission('system:file:create')")
    public Result<String> preparePart(@RequestParam String md5,
                                      @RequestParam String filename,
                                      @RequestParam(required = false) String remark) {
        String uploadId = systemFileService.preparePart(md5, filename, remark);
        return success(uploadId);
    }

    @PostMapping("/uploadPart/{uploadId}")
    @ApiOperation("分片上传")
    @PreAuthorize("@ss.hasPermission('system:file:create')")
    public Result<String> uploadPart(@PathVariable String uploadId,
                                     @RequestParam MultipartFile part,
                                     @RequestParam Integer index) throws IOException {
        String md5 = systemFileService.uploadPart(part, uploadId, index);
        return success(md5);
    }

    @PostMapping("/completePart/{uploadId}")
    @ApiOperation("完成分片上传")
    @PreAuthorize("@ss.hasPermission('system:file:create')")
    public Result<String> completePart(@PathVariable String uploadId) {
        String url = systemFileService.completePart(uploadId);
        return success(url);
    }

    @GetMapping("/page")
    @ApiOperation("获取文件分页")
    @PreAuthorize("@ss.hasPermission('system:file:query')")
    public Result<PageResult<SystemFileRespVo>> page(SystemFileQueryVo queryVo) {
        return success(CONVERT.convertPage(systemFileService.page(queryVo)));
    }

    @GetMapping("/statistics")
    @ApiOperation("文件统计")
//    @PreAuthorize("@ss.hasPermission('dashboard')")
    public Result<SystemFileStatisticsVo> statistics() {
        return success(systemFileService.statistics());
    }
}
