package com.zmijewski.ecommerce.quartz;

import com.zmijewski.ecommerce.quartz.job.DeleteCartJob;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

import java.util.Date;

@Configuration
public class JobSubmit {

    private static final String EVERY_5_MIN = "0 0/5 * 1/1 * ? *";

    @Bean(name = "deleteCartJobDetail")
    public JobDetailFactoryBean deleteCartJobDetail() {
        JobDetailFactoryBean detailFactoryBean = new JobDetailFactoryBean();
        detailFactoryBean.setJobClass(DeleteCartJob.class);
        detailFactoryBean.setDurability(true);
        detailFactoryBean.setDescription("Deleting old carts job");
        return detailFactoryBean;
    }
    @Bean(name = "deleteCartTrigger")
    public CronTriggerFactoryBean deleteCartJobTrigger(@Qualifier("deleteCartJobDetail") JobDetail jobDetail) {
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        cronTriggerFactoryBean.setJobDetail(jobDetail);
        cronTriggerFactoryBean.setCronExpression(EVERY_5_MIN);
        cronTriggerFactoryBean.setStartTime(new Date());
        cronTriggerFactoryBean.setStartDelay(0L);
        cronTriggerFactoryBean.setName("Delete cart trigger");
        cronTriggerFactoryBean.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
        return cronTriggerFactoryBean;
    }
}
