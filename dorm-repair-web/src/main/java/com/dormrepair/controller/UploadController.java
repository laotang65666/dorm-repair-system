package com.dormrepair.controller;

import com.dormrepair.pojo.Result;
import com.dormrepair.utils.AliyunOSSOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传接口。
 * 前端用 multipart/form-data 上传文件，字段名 file，返回 OSS 访问 URL。
 */
@Slf4j
@RestController
public class UploadController {

    @Autowired
    private AliyunOSSOperator aliyunOSSOperator;

    /** POST /upload，返回文件在 OSS 上的访问地址 */
    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            return Result.error("请选择要上传的文件");
        }
        log.info("文件上传：{}", file.getOriginalFilename());
        String url = aliyunOSSOperator.upload(file.getBytes(), file.getOriginalFilename());
        log.info("上传成功，URL：{}", url);
        return Result.success(url);
    }
}
