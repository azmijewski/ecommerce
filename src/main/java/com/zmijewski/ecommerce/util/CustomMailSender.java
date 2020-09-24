package com.zmijewski.ecommerce.util;

import com.zmijewski.ecommerce.dto.EmailDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
@Log4j2
public class CustomMailSender {
    private final JavaMailSender mailSender;

    public CustomMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    public void sendMail(EmailDTO emailDTO){
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        try {
            mimeMessageHelper.setTo(emailDTO.getSendTo());
            mimeMessageHelper.setText(emailDTO.getContent(), true);
            mimeMessageHelper.setSubject(emailDTO.getSubject());
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Could not send email", e);
        }
    }
}
