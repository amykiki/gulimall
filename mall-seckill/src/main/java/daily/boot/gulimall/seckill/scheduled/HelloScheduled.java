package daily.boot.gulimall.seckill.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class HelloScheduled {
    private AtomicInteger count = new AtomicInteger(0);
    
    
    /**
     * 1. 在Spring中表达式是6位组成，不允许第七位的年份
     * 2. 在周几的位置，1-7代表周一到周日
     * 3. 定时任务不该阻塞，默认是阻塞的
     * 1). 让业务以异步的方式，自己提交到线程池
     * ComplteableFuture.runAsync(() -> {}, execute);
     * <p>
     * 2). 支持定时任务线程池，设置 TaskSchedulingProperties
     * spring.task.scheduling.pool.size: 5
     * 注： 该设置在有些版本的springboot上不生效
     * <p>
     * 3). 让定时任务异步执行
     * 异步任务
     * <p>
     * 结论： 使用异步任务 + 定时任务来完成定时任务不阻塞的功能
     */
    @Async
    //@Scheduled(cron = "* * * * * ?")
    public void hello() {
        log.info("Hello...{}", count.getAndIncrement());
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
