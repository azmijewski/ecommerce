package com.zmijewski.ecommerce.quartz.job;

import com.zmijewski.ecommerce.service.CartService;
import lombok.extern.log4j.Log4j2;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class DeleteCartJob implements Job {

    @Autowired
    private CartService cartService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        cartService.deleteOldCarts();
    }
}
