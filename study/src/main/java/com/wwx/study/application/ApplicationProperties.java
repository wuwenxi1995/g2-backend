package com.wwx.study.application;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * @author wuwenxi 2022-05-11
 */
@Data
@ConfigurationProperties(prefix = "study")
public class ApplicationProperties {

    private Duration timeout;
}
