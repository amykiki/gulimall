package daily.boot.gulimall.thirdparty.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PolicyConditions;
import com.aliyun.oss.model.PutObjectRequest;
import com.google.gson.JsonObject;
import daily.boot.common.util.DateTimeFormatterUtil;
import daily.boot.gulimall.thirdparty.entity.OssProps;
import daily.boot.gulimall.thirdparty.service.AliOssService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Service
@DependsOn("ossProps")
@Slf4j
public class AliOssServiceImpl implements AliOssService, InitializingBean {
    private OSS ossClient;
    @Override
    public void afterPropertiesSet() throws Exception {
        ossClient = new OSSClientBuilder()
                .build(
                        OssProps.ENDPOINT,
                        OssProps.ACCESS_KEY_ID,
                        OssProps.ACCESS_KEY_SECRET
                );
    }
    
    @Override
    public String uplode(MultipartFile file) {
        // TODO: 2020/10/21 文件有效性，上传大小限制检查
        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();
        InputStream inputStream =null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return putFile(inputStream, contentType, fileName);
    }
    
    @Override
    public Map<String, String> policySign() {
        try {
            //设置过期时间
            long expireEndTime = System.currentTimeMillis() + OssProps.EXPIRE_TIME * 1000;
            Date expiration = new Date(expireEndTime);
            //设置上传文件目录，以日期分割
            String dir = DateTimeFormatterUtil.formatToDateStr(LocalDateTime.now()) + "/";
            
            //创建post规则
            PolicyConditions policyConds = new PolicyConditions();
            // PostObject请求最大可支持的文件大小为5 GB，即CONTENT_LENGTH_RANGE为5*1024*1024*1024。
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);
            //编码上传策略
            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
            String encodedPolcy = BinaryUtil.toBase64String(binaryData);
            
            //获取直传签名
            String postSignature = ossClient.calculatePostSignature(postPolicy);
            
            //构造返回数据
            Map<String, String> respMap = new LinkedHashMap<>();
            respMap.put("accessid", OssProps.ACCESS_KEY_ID);
            respMap.put("policy", encodedPolcy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", OssProps.HOST);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
            
            //构造callback数据
            JsonObject jsonCallback = new JsonObject();
            jsonCallback.addProperty("callbackUrl", OssProps.CALLBACK_URL);
            jsonCallback.addProperty("callbackBody", "filename=${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}");
            jsonCallback.addProperty("callbackBodyType", "application/x-www-form-urlencoded");
            String base64CallackBody = BinaryUtil.toBase64String(jsonCallback.toString().getBytes(StandardCharsets.UTF_8));
            respMap.put("callback", base64CallackBody);
            
            //构造自定义callback-var
            //Map<String, String> varMap = new HashMap<>();
            //varMap.put("x:id", "1234");
            //Gson json = new Gson();
            //String varJson = BinaryUtil.toBase64String(json.toJson(varMap).getBytes(StandardCharsets.UTF_8));
            respMap.put("x:uid", "123456");
    
    
            return respMap;
    
        } catch (Exception e) {
            // TODO: 2020/10/22 全局异常处理
            log.error(e.getMessage(), e);
        }
        
        return null;
    }
    
    @Override
    // TODO: 2020/10/22 回调需要有公网域名，现在无法测试 
    public Map<String, String> callback(String autorizationInput, String pubKeyUrl, String callbackBody) {
        VerifyOSSCallbackRequest(autorizationInput, pubKeyUrl, callbackBody);
        Map<String, String> result = new HashMap<>();
        result.put("Status", "OK");
        return result;
    }
    
    private String putFile(InputStream input, String contentType, String fileName) {
        //生成文件上传名
        String uploadName = uploadFileName(fileName);
        ObjectMetadata meta = new ObjectMetadata();
        //设置上传内容类型
        meta.setContentType(contentType);
        //被下载时网页的缓存行为
        meta.setCacheControl("no-cache");
        //创建上传请求
        PutObjectRequest request = new PutObjectRequest(OssProps.BUCKET_NAME, uploadName, input, meta);
        ossClient.putObject(request);
        return OssProps.HOST + "/" + uploadName;
    }
    
    private String uploadFileName(String fileName) {
        //上传文件夹目录
        String uploadDir = DateTimeFormatterUtil.formatToDateStr(LocalDateTime.now());
        StringBuilder sb = new StringBuilder(uploadDir);
        sb.append("/");
        int index = fileName.lastIndexOf(".");
        //文件名不包含后缀
        String filePrefix = fileName.substring(0, index);
        //文件后缀，包含.
        String fileSuffix = fileName.substring(fileName.lastIndexOf("."));
        sb.append(filePrefix);
        sb.append("_");
        sb.append(UUID.randomUUID());
        sb.append(fileSuffix);
        return sb.toString();
    }
    
    private boolean VerifyOSSCallbackRequest(String autorizationInput, String pubKeyUrl, String callbackBody) {
        byte[] pubKey = BinaryUtil.fromBase64String(pubKeyUrl);
        String pubKeyAddr = new String(pubKey);
        if (!pubKeyAddr.startsWith("http://gosspublic.alicdn.com/")
                && !pubKeyAddr.startsWith("https://gosspublic.alicdn.com/")) {
            log.error("pub key addr must be oss addrss");
            return false;
        }
        return true;
    }
}
