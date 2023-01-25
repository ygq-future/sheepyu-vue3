package top.sheepyu.framework.job.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author ygq
 * @date 2023-01-16 16:38
 **/
@Configuration
@EnableScheduling
public class SheepyuQuartzAutoConfiguration {
    /*@Bean
    @QuartzDataSource
    public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource) {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setDataSource(dataSource);
        return schedulerFactoryBean;
    }*/

    @Bean
    public SchedulerManager schedulerManager() {
        return new SchedulerManager();
    }
}
