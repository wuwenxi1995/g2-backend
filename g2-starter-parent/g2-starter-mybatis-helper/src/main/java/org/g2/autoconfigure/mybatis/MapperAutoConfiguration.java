package org.g2.autoconfigure.mybatis;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author wenxi.wu@hand-chian.com 2020-12-03
 */
@Configuration
public class MapperAutoConfiguration {

    @Bean
    @Primary
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer scanner = new MapperScannerConfigurer();
        scanner.setBasePackage("*.**.mapper");
        return scanner;
    }
}
