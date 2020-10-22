package daily.boot.gulimall.thirdparty.entity;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component("ossProps")
@ConfigurationProperties(prefix = "oss")
public class OssProps implements InitializingBean {
    private String accessKeyId;
    private String accessKeySecret;
    private String endpoint;
    /**
     * 阿里云 oss 文件根目录
     */
    private String bucketName;
    /**
     * web访问链接
     */
    private String host;
    private Integer expireTime; //签名过期时间，单位为秒
    private String callbackUrl; //回调url
    
    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;
    public static String ENDPOINT;
    public static String BUCKET_NAME;
    public static String HOST;
    public static Integer EXPIRE_TIME;
    public static String CALLBACK_URL;
    @Override
    public void afterPropertiesSet() throws Exception {
        ACCESS_KEY_ID = this.accessKeyId;
        ACCESS_KEY_SECRET = this.accessKeySecret;
        ENDPOINT = this.endpoint;
        BUCKET_NAME = this.bucketName;
        HOST = this.host;
        EXPIRE_TIME = this.expireTime;
        CALLBACK_URL = this.callbackUrl;
    }
}
