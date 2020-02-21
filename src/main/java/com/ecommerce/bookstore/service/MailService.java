package com.ecommerce.bookstore.service;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import javax.mail.internet.MimeMessage;

import com.ecommerce.bookstore.domain.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

/**
 * Service for sending emails.
 * <p>
 * We use the {@link Async} annotation to send emails asynchronously.
 * <p>
 * Source: <a href="https://www.jhipster.tech/">Jhipster</a> generated code.
 */
@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private final String SENDER = "bookstore@localhost";

    private final String USER = "user";

    private final String baseUrl = "http://localhost:8082";

    private final String BASE_URL = "baseUrl";

    private final JavaMailSender javaMailSender;

    private final SpringTemplateEngine templateEngine;

    public MailService(JavaMailSender javaMailSender, SpringTemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;

    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send email to '{}' with subject '{}' and content={}", to, subject, content);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());

            message.setTo(to);
            message.setFrom(SENDER);
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.info("Email has been sent to '{}'}", to);
            System.out.println(to);
            System.out.println(subject);
            System.out.println(content);
        } catch (Exception e) {
            log.warn("Email could not be sent to user '{}'", to, e);
        }
    }

    @Async
    public void sendEmailTemplate(User user, String templateName, String subject) {
        Locale locale = Locale.forLanguageTag(Locale.ENGLISH.getLanguage());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, baseUrl);
        String content = templateEngine.process(templateName, context);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendActivationEmail(User user) {
        log.debug("Sending activation email to '{}'", user.getEmail());
        String subject = "Bookstore account activation is required";
        sendEmailTemplate(user, "mail/activationEmail", subject);
    }
}