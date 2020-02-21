package com.ecommerce.bookstore.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

/**
 * A {@link Component} that is used for mailing service.
 */
@Configuration
public class EmailConfiguration {

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private String port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    /**
     * Configure a {@link JavaMailSender} instance using spring mail properties.
     * 
     * @return JavaMailSender
     */
    @Bean
    public JavaMailSender getMailSender() {
        JavaMailSenderImpl mailSenderImpl = new JavaMailSenderImpl();
        mailSenderImpl.setHost(getHost());
        mailSenderImpl.setPort(Integer.parseInt(getPort()));
        mailSenderImpl.setUsername(getUsername());
        mailSenderImpl.setPassword(getPassword());
        return mailSenderImpl;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "EmailConfiguration [host=" + host + ", password=" + password + ", port=" + port + ", username="
                + username + "]";
    }
}