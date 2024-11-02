package org.starter;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "log")
public class LogProperties {
    private Boolean enable = false;
    private String  logLvl = "INFO";
    private Boolean logDuration = false;

}
