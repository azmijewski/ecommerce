package com.zmijewski.ecommerce.listeners;

import com.zmijewski.ecommerce.dto.AuditDTO;
import com.zmijewski.ecommerce.dto.EmailDTO;
import com.zmijewski.ecommerce.service.AuditLogService;
import com.zmijewski.ecommerce.util.CustomMailSender;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitListeners {

    private final CustomMailSender mailSender;
    private final AuditLogService auditLogService;

    public RabbitListeners(CustomMailSender mailSender,
                           AuditLogService auditLogService) {
        this.mailSender = mailSender;
        this.auditLogService = auditLogService;
    }

    @RabbitListener(queues = "emailQueue")
    public void emailSender(EmailDTO emailDTO) {
        mailSender.sendMail(emailDTO);
    }

    @RabbitListener(queues = "auditQueue")
    public void addAuditLog(AuditDTO auditDTO) {
        auditLogService.createInfoAuditLog(auditDTO.getMessage());
    }
}
