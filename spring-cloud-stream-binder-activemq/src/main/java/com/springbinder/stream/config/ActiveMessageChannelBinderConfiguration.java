package com.springbinder.stream.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import com.springbinder.properties.ActiveBinderConfigurationProperties;
import com.springbinder.properties.ActiveExtendedBindingProperties;
import com.springbinder.provisioning.ActiveProvisioner;
import com.springbinder.stream.ActiveMessageChannelBinder;

@Configuration
@Import({ PropertyPlaceholderAutoConfiguration.class })
@EnableConfigurationProperties({ActiveExtendedBindingProperties.class
        , ActiveBinderConfigurationProperties.class})
public class ActiveMessageChannelBinderConfiguration {

    private final ActiveBinderConfigurationProperties activeBinderConfigurationProperties;

    @Autowired
    public ActiveMessageChannelBinderConfiguration(ActiveBinderConfigurationProperties activeBinderConfigurationProperties) {
        this.activeBinderConfigurationProperties = activeBinderConfigurationProperties;
    }

    @Bean
    public ActiveMessageChannelBinder activeMQMessageChannelBinder(ActiveExtendedBindingProperties activeExtendedBindingProperties){
        ActiveMessageChannelBinder activeMessageChannelBinder = new ActiveMessageChannelBinder(new ActiveProvisioner(), activeBinderConfigurationProperties);
        activeMessageChannelBinder.setExtendedProperties(activeExtendedBindingProperties);
        return activeMessageChannelBinder;
    }



}
