package com.zmijewski.ecommerce.configuration;

import lombok.extern.log4j.Log4j2;
import org.quartz.Trigger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

@Configuration
@Log4j2
public class QuartzConfiguration {
    private final ApplicationContext applicationContext;

    public QuartzConfiguration(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        AutoWiringSpringBeanJobFactory jobFactory = new AutoWiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(Trigger ... triggers) {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setConfigLocation(new ClassPathResource("quartz.properties"));
        schedulerFactoryBean.setJobFactory(springBeanJobFactory());
        schedulerFactoryBean.setTriggers(triggers);
        schedulerFactoryBean.setWaitForJobsToCompleteOnShutdown(true);
        return schedulerFactoryBean;
    }
}
