package com.dormrepair.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 阿里云 OSS 配置属性类。
 * 自动读取 application.yml 里 aliyun.oss.* 开头的配置：
 *   aliyun.oss.endpoint   → endpoint
 *   aliyun.oss.bucketName → bucketName
 *   aliyun.oss.region     → region
 *
 * 注意：这里只有「地址类」配置（非机密）；AccessKey 走环境变量，见 AliyunOSSOperator。
 */
@Data
@Component
@ConfigurationProperties(prefix = "aliyun.oss")
public class AliyunOSSProperties {
    private String endpoint;    // 访问域名，如 https://oss-cn-beijing.aliyuncs.com
    private String bucketName;  // 存储桶名称
    private String region;      // 地域，如 cn-beijing
}
