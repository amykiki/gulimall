package daily.boot.gulimall.thirdparty.util;

import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.alibaba.cloudapi.sdk.signature.HMacSHA256SignerFactory;
import com.alibaba.cloudapi.sdk.util.HttpCommonUtil;
import daily.boot.gulimall.common.httpclient.HttpClientReq;
import daily.boot.gulimall.common.httpclient.HttpConstant;

import java.text.SimpleDateFormat;
import java.util.*;

public class AliRequestSign {
    public static void addSignatureHeader(HttpClientReq request, AliRequestConfig config) {
        Date current = config.getCurrentDate() == null ? new Date() : config.getCurrentDate();
        //设置请求头中的时间戳
        if (null == request.getFirstHeaderValue(HttpConstant.HTTP_HEADER_DATE)) {
            request.addHeader(HttpConstant.HTTP_HEADER_DATE, getHttpDateHeaderValue(current));
        }
    
        //设置请求头中的时间戳，以timeIntervalSince1970的形式
        request.addHeader(SdkConstant.CLOUDAPI_X_CA_TIMESTAMP, String.valueOf(current.getTime()));
    
        //请求放重放Nonce,15分钟内保持唯一,建议使用UUID
        if(config.isGenerateNonce()){
            if(null == request.getFirstHeaderValue(SdkConstant.CLOUDAPI_X_CA_NONCE)) {
                request.addHeader(SdkConstant.CLOUDAPI_X_CA_NONCE, UUID.randomUUID().toString());
            }
        }
    
    
        //设置请求头中的UserAgent
        request.addHeader(HttpConstant.HTTP_HEADER_USER_AGENT, SdkConstant.CLOUDAPI_USER_AGENT);
    
        //设置请求头中的主机地址
        request.addHeader(HttpConstant.HTTP_HEADER_HOST , request.getHost());
    
        //设置请求头中的Api绑定的的AppKey
        request.addHeader(SdkConstant.CLOUDAPI_X_CA_KEY, config.getAppKey());
    
        //设置签名版本号
        request.addHeader(SdkConstant.CLOUDAPI_X_CA_VERSION , SdkConstant.CLOUDAPI_CA_VERSION_VALUE);
    
        //设置请求数据类型
        if(null == request.getFirstHeaderValue(HttpConstant.HTTP_HEADER_CONTENT_TYPE)) {
            request.addHeader(HttpConstant.HTTP_HEADER_CONTENT_TYPE, request.getHttpMethod().getRequestContentType());
        }
        //设置应答数据类型
        if(null == request.getFirstHeaderValue(HttpConstant.HTTP_HEADER_ACCEPT)){
            request.addHeader(HttpConstant.HTTP_HEADER_ACCEPT , request.getHttpMethod().getAcceptContentType());
        }
        
        if (config.isNeedSignature() && !HttpCommonUtil.isEmpty(config.getSignatureMethod())) {
            request.addHeader(SdkConstant.CLOUDAPI_X_CA_SIGNATURE_METHOD, config.getSignatureMethod());
        }
        request.addHeader(SdkConstant.CLOUDAPI_X_CA_SIGNATURE_METHOD, HMacSHA256SignerFactory.METHOD);
    
        /**
         *  如果formParams不为空
         *  将Form中的内容拼接成字符串后使用UTF8编码序列化成Byte数组后加入到Request中去
         */
        if(null != request.getBody() && config.isGenerateContentMd5() && request.getBody().length >0 && null == request.getFirstHeaderValue(HttpConstant.HTTP_HEADER_CONTENT_MD5)){
            request.addHeader(HttpConstant.HTTP_HEADER_CONTENT_MD5 , AliSignUtil.base64AndMD5(request.getBody()));
        }
    
        /**
         *  将Request中的httpMethod、headers、path、queryParam、formParam合成一个字符串用hmacSha256算法双向加密进行签名
         *  签名内容放到Http头中，用作服务器校验
         */
        String signature = AliSignUtil.sign(request, config);
        request.addHeader(SdkConstant.CLOUDAPI_X_CA_SIGNATURE, signature);
    
        /**
         *  凑齐所有HTTP头之后，将头中的数据全部放入Request对象中
         *  Http头编码方式：先将字符串进行UTF-8编码，然后使用Iso-8859-1解码生成字符串
         */
        for(String key : request.getHeaders().keySet()){
            List<String> values = request.getHeaders().get(key);
            if(null != values && values.size() > 0){
                for(int i = 0 ; i < values.size() ; i++){
                    byte[] temp = values.get(i).getBytes(SdkConstant.CLOUDAPI_ENCODING);
                    values.set(i , new String(temp , SdkConstant.CLOUDAPI_HEADER_ENCODING));
                }
            }
            request.getHeaders().put(key , values);
        }
    }
    
    private static String getHttpDateHeaderValue(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(date);
    }
}
