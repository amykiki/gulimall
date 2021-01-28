package daily.boot.gulimall.seckill.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * 定时任务
 *  1. @EnableScheduling 开启定时任务
 *  2. @Scheduled 开启一个定时任务的方法注解
 *
 * 异步任务
 *  1. @EnableAsync: 开启异步任务
 *  2. @Async: 给希望异步执行的方法标注
 */
@EnableAsync
@EnableScheduling
@Configuration
public class ScheduledConfig {
}
