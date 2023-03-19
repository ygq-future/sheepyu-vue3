package top.sheepyu.module.system.service.file;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import top.sheepyu.framework.file.config.FileUploadFactory;
import top.sheepyu.framework.file.core.oss.FileUpload;
import top.sheepyu.framework.mybatisplus.core.query.ServiceImplX;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.common.util.MyStrUtil;
import top.sheepyu.module.system.api.file.FileDto;
import top.sheepyu.module.system.controller.admin.file.vo.SystemFileQueryVo;
import top.sheepyu.module.system.controller.admin.file.vo.SystemFileStatisticsVo;
import top.sheepyu.module.system.dao.file.SystemFile;
import top.sheepyu.module.system.dao.file.SystemFileMapper;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static top.sheepyu.framework.file.core.enums.FileUploadCompleteEnum.COMPLETE;
import static top.sheepyu.framework.file.core.enums.FileUploadCompleteEnum.INCOMPLETE;
import static top.sheepyu.framework.security.util.SecurityFrameworkUtil.getLoginUserUsername;
import static top.sheepyu.module.common.exception.CommonException.exception;
import static top.sheepyu.module.common.util.FileUtil.*;
import static top.sheepyu.module.common.util.MyStrUtil.getDatePath;
import static top.sheepyu.module.common.util.MyStrUtil.getUUID;
import static top.sheepyu.module.system.constants.ErrorCodeConstants.FILE_EXISTS;
import static top.sheepyu.module.system.constants.ErrorCodeConstants.FILE_NOT_EXISTS;
import static top.sheepyu.module.system.convert.file.SystemFileConvert.CONVERT;

/**
 * @author ygq
 * @date 2023-01-25 16:14
 **/
@Service
@Slf4j
@Validated
public class SystemFileServiceImpl extends ServiceImplX<SystemFileMapper, SystemFile> implements SystemFileService {
    @Resource
    private FileUploadFactory fileUploadFactory;

    @Override
    public void updateFileByUploadId(FileDto dto) {
        SystemFile file = CONVERT.convert(dto);
        update(file, buildQuery().eq(SystemFile::getUploadId, file.getUploadId()));
    }

    @Override
    public SystemFile findFileByUploadId(String uploadId) {
        SystemFile file = lambdaQuery().eq(SystemFile::getUploadId, uploadId).one();
        if (file == null) {
            throw exception(FILE_NOT_EXISTS);
        }
        return file;
    }

    @Override
    public SystemFile findFileByMd5(String md5) {
        return lambdaQuery().eq(SystemFile::getMd5, md5).one();
    }

    @Override
    public PageResult<SystemFile> page(SystemFileQueryVo queryVo) {
        return page(queryVo, buildQuery()
                .and(StrUtil.isNotBlank(queryVo.getKeyword()), q -> q
                        .eq(SystemFile::getId, queryVo.getKeyword()).or()
                        .like(SystemFile::getFilename, queryVo.getKeyword()))
                .eqIfPresent(SystemFile::getComplete, queryVo.getComplete())
                .betweenIfPresent(SystemFile::getSize, queryVo.getSizes())
                .betweenIfPresent(SystemFile::getCreateTime, queryVo.getCreateTimes())
                .orderByDesc(SystemFile::getCreateTime));
    }

    @Transactional
    @Override
    public void delete(String ids) {
        List<Long> idList = MyStrUtil.splitToLong(ids, ',');
        List<SystemFile> files = listByIds(idList);

        //删除数据库数据
        batchDelete(ids, SystemFile::getId);

        //删除本地或者云端文件
        List<String> paths = files.stream().map(SystemFile::getPath).collect(Collectors.toList());
        fileUploadFactory.get().deleteFile(paths);
    }

    @Override
    public String upload(MultipartFile file, String md5, String remark) throws IOException {
        SystemFile systemFile = findFileByMd5(md5);
        if (systemFile != null) {
            return systemFile.getUrl();
        }

        FileUpload upload = fileUploadFactory.get();
        String filename = file.getOriginalFilename();
        String path = getDatePath() + getUUID() + getSuffix(filename);
        String domain = upload.buildDomain();
        //封装基本信息
        systemFile = new SystemFile()
                .setComplete(COMPLETE.getCode())
                .setUploadId(getUUID())
                .setSize(getSize(file.getInputStream()))
                .setDomain(domain)
                .setFilename(filename)
                .setMimeType(getMimeType(filename))
                .setPath(path)
                .setUrl(domain + path)
                .setRemark(remark)
                .setCreator(getLoginUserUsername())
                .setCreateTime(new Date());

        //上传文件
        md5 = upload.upload(file.getInputStream(), path, systemFile.getSize());
        save(systemFile.setMd5(md5));
        return systemFile.getUrl();
    }

    @Override
    public String preparePart(String md5, String filename, String remark) {
        SystemFile systemFile = findFileByMd5(md5);
        if (systemFile != null) {
            checkFileComplete(systemFile);
            return systemFile.getUploadId();
        }

        FileUpload upload = fileUploadFactory.get();
        String path = getDatePath() + getUUID() + getSuffix(filename);
        String domain = upload.buildDomain();
        //封装基本信息
        systemFile = new SystemFile()
                .setComplete(INCOMPLETE.getCode())
                .setMd5(md5)
                .setFilename(filename)
                .setMimeType(getMimeType(filename))
                .setDomain(domain)
                .setPath(path)
                .setUrl(domain + path)
                .setUploadId(upload.buildUploadId(path))
                .setRemark(remark)
                .setCreator(getLoginUserUsername())
                .setCreateTime(new Date());

        save(systemFile);
        return systemFile.getUploadId();
    }

    @Override
    public String uploadPart(MultipartFile part, String uploadId, Integer index) throws IOException {
        SystemFile systemFile = findFileByUploadId(uploadId);
        checkFileComplete(systemFile);

        String path = systemFile.getPath();
        InputStream in = part.getInputStream();
        long size = getSize(in);
        String md5 = fileUploadFactory.get().uploadPart(in, uploadId, path, size, index);
        //更新一下文件的index
        systemFile.setPartIndex(index);
        updateById(systemFile);
        return md5;
    }

    @Override
    public String completePart(String uploadId) {
        SystemFile systemFile = findFileByUploadId(uploadId);
        checkFileComplete(systemFile);
        long size = fileUploadFactory.get().completePart(uploadId, systemFile.getPath());
        systemFile.setSize(size).setComplete(COMPLETE.getCode());
        updateById(systemFile);
        return systemFile.getUrl();
    }

    @Override
    public SystemFileStatisticsVo statistics() {
        SystemFileStatisticsVo vo = new SystemFileStatisticsVo();
        Date now = new Date();
        DateTime lastDay = DateUtil.offsetDay(now, -1);
        long total = count();
        //获取今日上传数量
        long todayIncrement = count(buildQuery()
                .between(SystemFile::getCreateTime, DateUtil.beginOfDay(now), DateUtil.endOfDay(now)));
        //获取昨天上传数量
        long lastDayIncrement = count(buildQuery()
                .between(SystemFile::getCreateTime, DateUtil.beginOfDay(lastDay), DateUtil.endOfDay(lastDay)));
        int todayPercent = (int) ((todayIncrement - lastDayIncrement) / (lastDayIncrement == 0 ? 1 : lastDayIncrement)) * 100;
        vo.setTotal(total);
        vo.setTodayIncrement(Long.valueOf(todayIncrement).intValue());
        vo.setTodayPercent(todayPercent);
        //获取上周开始和结束
        Date beginWeek = DateUtil.beginOfWeek(DateUtil.lastWeek());
        Date endWeek = DateUtil.endOfWeek(DateUtil.lastWeek());
        //查询上周的数据, 只查createTime和mimeType就行
        List<SystemFile> fileList = list(buildQuery()
                .select(SystemFile::getMimeType, SystemFile::getCreateTime)
                .between(SystemFile::getCreateTime, beginWeek, endWeek));
        //按照日期分组排序
        TreeMap<Date, List<SystemFile>> fileByDateMap = fileList.stream()
                .peek(e -> e.setCreateTime(DateUtil.beginOfDay(e.getCreateTime())))
                .collect(Collectors.groupingBy(SystemFile::getCreateTime, TreeMap::new, Collectors.toList()));
        //补全头一天和最后一天
        if (CollUtil.isEmpty(fileByDateMap)) {
            //说明头和尾都没有
            fileByDateMap.put(beginWeek, Collections.emptyList());
            fileByDateMap.put(endWeek, Collections.emptyList());
        } else {
            //说明开头的不是这个星期的头一天, 所以补全一下
            if (!DateUtil.isSameDay(fileByDateMap.firstKey(), beginWeek)) {
                fileByDateMap.put(beginWeek, Collections.emptyList());
            }
            //说明结尾的不是这个星期的结尾一天, 所以补全一下
            if (!DateUtil.isSameDay(fileByDateMap.lastKey(), endWeek)) {
                fileByDateMap.put(endWeek, Collections.emptyList());
            }
        }
        //封装数据
        List<List<Integer>> weekIncrement = new ArrayList<>();
        AtomicReference<Date> lastCreateTime = new AtomicReference<>();
        fileByDateMap.forEach((date, files) -> {
            //判断是否和前一天是连续的天
            if (lastCreateTime.get() != null) {
                while (!DateUtil.isSameDay(DateUtil.offsetDay(lastCreateTime.get(), 1), date)) {
                    weekIncrement.add(Arrays.asList(0, 0, 0, 0));
                    lastCreateTime.set(DateUtil.offsetDay(lastCreateTime.get(), 1));
                }
            }
            //根据mime_type判断文件类型并计数
            int fileCount = 0;
            int imgCount = 0;
            int mediaCount = 0;
            int compressCount = 0;
            for (SystemFile file : files) {
                if (DOCUMENT_TYPE.contains(file.getMimeType())) {
                    fileCount++;
                }
                if (IMAGE_TYPE.contains(file.getMimeType())) {
                    imgCount++;
                }
                if (MEDIA_TYPE.contains(file.getMimeType())) {
                    mediaCount++;
                }
                if (COMPRESS_TYPE.contains(file.getMimeType())) {
                    compressCount++;
                }
            }
            weekIncrement.add(Arrays.asList(fileCount, imgCount, mediaCount, compressCount));
            lastCreateTime.set(date);
        });
        vo.setWeekIncrement(weekIncrement);
        return vo;
    }

    /**
     * 检查文件是否已经完成上传
     *
     * @param file 文件
     */
    private void checkFileComplete(SystemFile file) {
        if (COMPLETE.getCode() == file.getComplete()) {
            throw exception(FILE_EXISTS);
        }
    }
}
