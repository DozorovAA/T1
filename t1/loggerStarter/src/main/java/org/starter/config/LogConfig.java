package org.starter.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class LogConfig {
    private Boolean enableLog;
    private String logLevel;
    private Boolean logDuration;

    public LogConfig(Boolean enableLog, String logLevel, Boolean logDuration) {
        this.enableLog = enableLog;
        this.logLevel = logLevel;
        this.logDuration = logDuration;
    }

}
