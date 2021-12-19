package com.happysnaker.utils;



import com.happysnaker.exception.TxCosUploadException;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

/**
 * 文件上传控制器
 * @author happysnakers
 */
@Component
public class TxCosUtils {

    @Value("${spring.tengxunyun.cos.secretId}")
    private String secretId;
    @Value("${spring.tengxunyun.cos.secretKey}")
    private String secretKey;
    @Value("${spring.tengxunyun.cos.bucket}")
    private String bucket;
    @Value("${spring.tengxunyun.cos.bucketName}")
    private String bucketName;
    @Value("${spring.tengxunyun.cos.url}")
    private String url;
    @Value("${spring.tengxunyun.cos.prefix}")
    private String prefix;

    /**
     * 上传道腾讯云服务器
     *
     * @return
     */
    @NonNull
    public String uploadFile(MultipartFile file, String fileName) throws TxCosUploadException {
        String oldFileName = file.getOriginalFilename();
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DATE);
        // 1 初始化用户身份信息(secretId, secretKey)
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        ClientConfig clientConfig = new ClientConfig(new Region(bucket));
        // 3 生成cos客户端
        COSClient cosclient = new COSClient(cred, clientConfig);
        // bucket的命名规则为{name}-{appid} ，此处填写的存储桶名称必须为此格式
        String bucketName = this.bucketName;

        // 简单文件上传, 最大支持 5 GB, 适用于小文件上传, 建议 20 M 以下的文件使用该接口
        // 大文件上传请参照 API 文档高级 API 上传
        File localFile = null;
        try {
            localFile = File.createTempFile("temp", null);
            file.transferTo(localFile);
            // 指定要上传到 COS 上的路径
            String key = "/" + this.prefix + fileName;
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
            PutObjectResult putObjectResult = cosclient.putObject(putObjectRequest);
            return url + putObjectRequest.getKey();
        } catch (IOException e) {
            throw new TxCosUploadException("腾讯云COS上传文件时失败，错误信息: " + e.getMessage());
        } finally {
            // 关闭客户端(关闭后台线程)
            cosclient.shutdown();
        }
    }

}
