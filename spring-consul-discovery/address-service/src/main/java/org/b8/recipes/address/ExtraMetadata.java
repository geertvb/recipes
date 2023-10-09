package org.b8.recipes.address;

import com.ecwid.consul.v1.ConsulClient;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties
@ConditionalOnClass({ConsulDiscoveryProperties.class, ConsulClient.class})
public class ExtraMetadata {

    @Autowired(required = false)
    protected ConsulDiscoveryProperties properties;

    @PostConstruct
    public void init() {
        if (properties != null) {
            properties.getMetadata().put("extra_value", "Hello Address!");
        }
    }

}
