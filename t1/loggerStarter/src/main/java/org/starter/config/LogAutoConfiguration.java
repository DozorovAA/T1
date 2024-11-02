package org.starter.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.starter.LogProperties;

@Configuration
@EnableConfigurationProperties(LogProperties.class)
@ComponentScan(basePackages = "org.starter")
@EnableAspectJAutoProxy
public class LogAutoConfiguration {
    private final LogProperties logProperties;

    public LogAutoConfiguration(LogProperties authProperties) {
        this.logProperties = authProperties;
    }

    @Bean
    public LogConfig logConfig() {
        if(logProperties == null) {
            return new LogConfig(false, "INFO", false);
        }
        return new LogConfig(logProperties.getEnable(), logProperties.getLogLvl(), logProperties.getLogDuration());
    }

}