package com.fisi.sd.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages="com.fisi.sd")
@Import(value = {RepositoryConfiguration.class})
public class ApplicationConfiguration {

}
