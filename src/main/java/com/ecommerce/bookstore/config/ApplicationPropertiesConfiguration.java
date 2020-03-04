package com.ecommerce.bookstore.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Properties specific to this application.
 * <p>
 * Properties are configured in the {@code application-[prod, test].yml} files
 * with the prefix app.
 */
@Configuration
@ConfigurationProperties(prefix = "app")
public class ApplicationPropertiesConfiguration {
    private final JsonWebToken jwt = new JsonWebToken();
    private final Mail mail = new Mail();
    private final CORS cors = new CORS();

    @Bean
    public ApplicationPropertiesConfiguration properties() {
        return new ApplicationPropertiesConfiguration();
    }

    public static class JsonWebToken {
        private final Token token = new Token();

        public static class Token {
            private String secret;
            private Long expiration;

            public String getSecret() {
                return secret;
            }

            public void setSecret(String secret) {
                this.secret = secret;
            }

            public Long getExpiration() {
                return expiration;
            }

            public void setExpiration(Long expiration) {
                this.expiration = expiration;
            }
        }

        public Token getToken() {
            return token;
        }

    }

    public static class Mail {
        private String from;
        private String baseUrl;

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

    }

    public static class CORS {
        private List<String> allowedOrigins = new ArrayList<>();

        public List<String> getAllowedOrigins() {
            return allowedOrigins;
        }

        public void setAllowedOrigins(List<String> allowedOrigins) {
            this.allowedOrigins = allowedOrigins;
        }
    }

    public JsonWebToken getJwt() {
        return jwt;
    }

    public Mail getMail() {
        return mail;
    }

    public CORS getCors() {
        return cors;
    }
}