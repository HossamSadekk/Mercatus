package com.sideproject.mercatus.service;

import com.sideproject.mercatus.exceptions.MailFailureException;
import com.sideproject.mercatus.model.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Value("${email.from}")
    private String fromAddress;

    @Value("${app.frontend.url}")
    private String url;

    @Autowired
    private JavaMailSender javaMailSender;

    private SimpleMailMessage makeMailMessage() {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromAddress);
        return simpleMailMessage;
    }

    public void sendVerificationEmail(VerificationToken verificationToken) throws MailFailureException {
        SimpleMailMessage message = makeMailMessage();
        message.setTo(verificationToken.getLocalUser().getEmail());
        message.setSubject("Verify your email to activate your account");
        message.setText("please follow the link below to verify your email to activate your account.\n" + url + "/auth/verify?token=" + verificationToken.getToken());
        try {
            javaMailSender.send(message);
        } catch (MailException e) {
            throw new MailFailureException();
        }
    }
}