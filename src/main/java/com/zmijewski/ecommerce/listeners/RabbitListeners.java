package com.zmijewski.ecommerce.listeners;

import com.zmijewski.ecommerce.dto.EmailDTO;
import com.zmijewski.ecommerce.util.CustomMailSender;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitListeners {

    private final CustomMailSender mailSender;

    public RabbitListeners(CustomMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @RabbitListener(queues = "emailQueue")
    public void emailSender(EmailDTO emailDTO) {
        mailSender.sendMail(emailDTO);
    }
}
