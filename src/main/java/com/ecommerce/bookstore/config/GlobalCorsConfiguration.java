package com.ecommerce.bookstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * A Global Cross Origins configuration to define which domains can access this
 * applications api.
 */
@Configuration
public class GlobalCorsConfiguration {

    private final ApplicationPropertiesConfiguration applicationPropertiesConfiguration;

    public GlobalCorsConfiguration(ApplicationPropertiesConfiguration applicationPropertiesConfiguration) {
        this.applicationPropertiesConfiguration = applicationPropertiesConfiguration;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            // Makes sure the APIs are accessible only from the allowed origins.
            public void addCorsMappings(CorsRegistry registry) {
                // Note: (important)
                // registry.addMapping("/**") will not not work for this app api because the
                // root mapping is requestMapping("/api") see (AccountResourceController class).
                // Thus, use
                // registry.addMapping("/api/**")
                registry.addMapping("/api/**")
                        .allowedOrigins(applicationPropertiesConfiguration.getCors().getAllowedOrigins().toArray(
                                new String[applicationPropertiesConfiguration.getCors().getAllowedOrigins().size()]));
            }
        };
    }

}