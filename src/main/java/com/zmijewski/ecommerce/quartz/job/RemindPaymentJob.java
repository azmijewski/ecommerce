package com.zmijewski.ecommerce.quartz.job;

import com.zmijewski.ecommerce.service.OrderService;
import com.zmijewski.ecommerce.util.CustomMailSender;
import com.zmijewski.ecommerce.util.EmailTemplateCreator;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RemindPaymentJob implements Job {
    @Autowired
    private OrderService orderService;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        orderService.sendRemindPaymentNotifications();
    }
}
