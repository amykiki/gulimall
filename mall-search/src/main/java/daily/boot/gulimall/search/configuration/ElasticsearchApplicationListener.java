package daily.boot.gulimall.search.configuration;

import daily.boot.gulimall.search.service.BaseESService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author zhangboqing
 * @date 2019/12/10
 */

@Component
@Slf4j
public class ElasticsearchApplicationListener implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {
    private ApplicationContext applicationContext;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        String[] beanNames = applicationContext.getBeanNamesForType(BaseESService.class);
        if (beanNames.length > 0) {
            for (int i = 0; i < beanNames.length; i++) {
                String beanName = beanNames[i];
                if (beanName.contains("daily.boot.gulimall.search.service.impl.BaseESServiceImpl")) {
                    continue;
                }
                BaseESService bean = applicationContext.getBean(beanName, BaseESService.class);
                bean.createIndex();
            }
            
        }
    
    }
}
