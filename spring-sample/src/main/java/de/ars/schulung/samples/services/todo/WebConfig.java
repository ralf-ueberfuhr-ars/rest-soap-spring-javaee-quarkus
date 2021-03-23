package de.ars.schulung.samples.services.todo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.*;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {

            // read from environment variable (different in local, test and prod stage)
            @Value("${CORS_ALLOWED_ORIGINS:*}")
            String allowedOrigins;
            @Value("${CORS_ALLOW_CREDENTIALS:false}")
            boolean allowCredentials;

            @Override
            public void addCorsMappings(final CorsRegistry registry) {
                registry.addMapping("/**") //
                        .exposedHeaders(LOCATION, LINK) //
                        // allow all HTTP request methods
                        .allowedMethods(stream(RequestMethod.values()).map(Enum::name).toArray(String[]::new)) //
                        // allow the commonly used headers
                        .allowedHeaders(ORIGIN, CONTENT_TYPE, CONTENT_LANGUAGE, ACCEPT, ACCEPT_LANGUAGE, IF_MATCH, IF_NONE_MATCH) //
                        // this is stage specific
                        .allowedOrigins(allowedOrigins.split(","))
                        .allowCredentials(allowCredentials);
            }
        };
    }
}
