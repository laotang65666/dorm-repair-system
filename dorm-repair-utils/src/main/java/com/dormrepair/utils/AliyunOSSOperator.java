package com.dormrepair.utils;

import com.aliyun.sdk.service.oss2.OSSClient;
import com.aliyun.sdk.service.oss2.OSSClientBuilder;
import com.aliyun.sdk.service.oss2.credentials.CredentialsProvider;
import com.aliyun.sdk.service.oss2.credentials.EnvironmentVariableCredentialsProvider;
import com.aliyun.sdk.service.oss2.models.PutObjectRequest;
import com.aliyun.sdk.service.oss2.models.PutObjectResult;
import com.aliyun.sdk.service.oss2.transport.BinaryData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 阿里云 OSS 文件上传工具类（新版 SDK v2）。
 *
 * 职责：接收「文件字节数组 + 原始文件名」，上传到 OSS，返回可访问的 URL。
 * 被 UploadController 调用。
 *
 * 关键安全点：AccessKeyId / AccessKeySecret 通过环境变量读取
 *   OSS_ACCESS_KEY_ID / OSS_ACCESS_KEY_SECRET
 * 不写死在代码里，所以这个类可以放心提交到 git。
 */
@Slf4j
@Component
public class AliyunOSSOperator {

    @Autowired
    private AliyunOSSProperties aliyunOSSProperties;

    /**
     * 上传文件到 OSS
     * @param content          文件内容（字节数组）
     * @param originalFilename 原始文件名，用来取扩展名
     * @return 上传成功后的可访问 URL，如 https://xxx.oss-cn-beijing.aliyuncs.com/2026/10/xxxx.png
     */
    public String upload(byte[] content, String originalFilename) throws Exception {
        String endpoint = aliyunOSSProperties.getEndpoint();
        String bucketName = aliyunOSSProperties.getBucketName();
        String region = aliyunOSSProperties.getRegion();

        // ① 凭证：从环境变量读取（告诉 OSS「我是谁」）
        CredentialsProvider provider = new EnvironmentVariableCredentialsProvider();

        // ② 生成对象名：按年月分目录 + UUID 随机名 + 保留扩展名，避免重名冲突
        String dir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM"));
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String objectName = dir + "/" + UUID.randomUUID() + extension;

        // ③ 构建客户端：凭证 + 区域 + 域名
        OSSClientBuilder clientBuilder = OSSClient.newBuilder()
                .credentialsProvider(provider)
                .region(region)
                .endpoint(endpoint);

        // ④ 上传（try-with-resources：用完自动关闭连接）
        try (OSSClient client = clientBuilder.build()) {
            PutObjectRequest request = PutObjectRequest.newBuilder()
                    .bucket(bucketName)
                    .key(objectName)
                    .body(BinaryData.fromBytes(content))
                    .build();

            PutObjectResult result = client.putObject(request);
            if (result.statusCode() != 200) {
                throw new RuntimeException("OSS 上传失败，statusCode = " + result.statusCode());
            }
            log.info("OSS 上传成功：{}", objectName);
        }

        // ⑤ 拼接并返回访问 URL：https://<bucket>.<endpoint域名>/<objectName>
        return endpoint.split("//")[0] + "//"
                + bucketName + "." + endpoint.split("//")[1]
                + "/" + objectName;
    }
}
