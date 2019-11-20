package com.micro.msaframework.registry;

import com.micro.msaframework.registry.registryimpl.ServiceRegistryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "registry")
public class RegistryConfig {
    private Logger logger = LoggerFactory.getLogger(RegistryConfig.class);
    private String servers;

    @Bean
    public ServiceRegistry serviceRegistry() {
        return new ServiceRegistryImpl(servers);
    }


    public void setServers(String servers) {
        this.servers = servers;
        logger.info("servers: {}", servers);
    }
}
