package daily.boot.gulimall.product.service.impl;

import daily.boot.common.exception.BusinessException;
import daily.boot.gulimall.product.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class TestServiceImpl implements TestService {
    @Autowired
    private RedisTemplate redisTemplate;
    
    @Override
    @Cacheable(value = "smsCode", key = "'phone-' + #phone")
    public String getSmsCode(String phone) {
        String smsCode = smsCode();
        log.debug("generate SMSCode:{} for {}", smsCode, phone);
        return smsCode;
    }
    
    @Override
    @CacheEvict(value = "smsCode", key = "'phone-' + #phone", beforeInvocation = false)
    public String verifySmsCode(String phone, String code) {
        String smsCode = (String) redisTemplate.opsForValue().get("smsCode::phone-" + phone);
        if (StringUtils.isBlank(smsCode) || !smsCode.equals(code)) {
            throw new BusinessException("验证码不正确", "999999");
        }
        return "verified";
    }
    
    @Override
    @CacheEvict(value = "smsCode", allEntries = true)
    public void clearAllSmsCodes() {
    }
    
    private String smsCode() {
        String code = IntStream.range(0, 6).mapToObj(i -> String.valueOf(ThreadLocalRandom.current().nextInt(10)))
                                  .collect(Collectors.joining());
        return code;
    }
}
