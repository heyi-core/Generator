package com.heyi.${project.dataBase}.baseplatform.image;

import cn.hutool.core.util.RandomUtil;
import com.aliyun.oss.OSSClient;
import com.heyi.${project.dataBase}.basecommon.BaseException;
import com.heyi.${project.dataBase}.platform.sysuser.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.heyi.${project.dataBase}.basecommon.BaseConstant.FAILED;

@Service
@PropertySource("classpath:oss.properties")
@Slf4j
public class ImageService {

    @Value("${r'${'}aliyun.oss.endpoint}")
    private String endpoint;
    @Value("${r'${'}aliyun.oss.accessKeyId}")
    private String accessKeyId;
    @Value("${r'${'}aliyun.oss.accessKeySecret}")
    private String accessKeySecret;
    @Value("${r'${'}aliyun.oss.bucketName}")
    private String bucketName;
    @Value("${r'${'}aliyun.oss.domain}")
    private String aliyundomain;

    /**
     * 上传文件
     *
     * @param inputStream 文件流
     * @return
     * @throws Exception
     */
    public String uploadoss(SysUser sysUser, InputStream inputStream, String suffix) {
        if (StringUtils.isBlank(inputStream.toString())) {
            throw new BaseException(FAILED, "上传文件不允许为空");
        }
        if(StringUtils.isBlank(suffix)){
            suffix = ".jpg";
        }
        String url = sysUser.getSysUserId() + "/" + RandomUtil.randomString(16)+ suffix ;
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        if (!ossClient.doesBucketExist(bucketName)) {
            ossClient.createBucket(bucketName);
        }
        ossClient.putObject(bucketName, url, inputStream);
        return aliyundomain + url;
    }
    /**
     * oss删除
     * @param filePath
     */
    public void deleteoss(String filePath) {
        filePath = filePath.replace(aliyundomain,"");
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        if (!ossClient.doesBucketExist(bucketName)) {
            ossClient.createBucket(bucketName);
        }
        boolean exist = ossClient.doesObjectExist(bucketName, filePath);
        if (!exist) {
            throw new BaseException(FAILED,"文件不存在");
        }
        ossClient.deleteObject(bucketName, filePath);
    }

    public Map<String, List<String>> uploadossMany(MultipartFile[] uploadfile, SysUser sysUser){
        List<String> success = new ArrayList<>();
        List<String> failed = new ArrayList<>();
        for (MultipartFile multipartFile : uploadfile) {
            String suffix = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf("."));
            try {
                String url = this.uploadoss(sysUser, multipartFile.getInputStream(),suffix);
                success.add(url);
            }catch (Exception e){
                failed.add(multipartFile.getOriginalFilename());
            }
        }
        LinkedHashMap<String, List<String>> resultMap = new LinkedHashMap<>();
        resultMap.put("success",success);
        resultMap.put("failed",failed);
        return resultMap;
    }
}
