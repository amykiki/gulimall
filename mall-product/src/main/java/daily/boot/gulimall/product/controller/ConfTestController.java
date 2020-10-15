package daily.boot.gulimall.product.controller;

import daily.boot.gulimall.common.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Api(tags = "nacos配置中心测试")
@RequestMapping("product/nacos-conf")
@RefreshScope
public class ConfTestController {
    @Value("${env}")
    private String env;
    @Value("${mall.product.name}")
    private String appName;
    @Value("${mall.product.appid}")
    private String appId;
    @Value("${mall.product.timeout}")
    private String timeout;
    @Value("${mall.product.data.password}")
    private String dataPassword;
    @Value("${mall.product.mq.topic}")
    private String mqName;
    @Value("${mall.product.rpc.server}")
    private String rpcServer;
    
    @GetMapping
    @ApiOperation(value = "获取配置中心配置")
    public R getConfs() {
        Map<String, String> map = new HashMap<>();
        map.put("env", env);
        map.put("appName", appName);
        map.put("appId", appId);
        map.put("timeout", timeout);
        map.put("dataPassword", dataPassword);
        map.put("mqName", mqName);
        map.put("rpcServer", rpcServer);
        return R.ok().put("data", map);
    }
    
}
