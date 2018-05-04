package cn.krly.service.notification.configuration;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ProviderConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.rpc.Exporter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(Exporter.class)
public class DubboConfiguration {

    @Bean(name = "MailServiceProviderConfig")
    public ProviderConfig getMailServiceProviderConfig(ApplicationConfig applicationConfig, RegistryConfig registryConfig) {
        ProtocolConfig protocolConfig = getProtocolConfig("dubbo", 15625);
        return getProviderConfig(applicationConfig, registryConfig, protocolConfig);
    }

    @Bean(name = "SMSServiceProviderConfig")
    public ProviderConfig getSMSServiceProviderConfig(ApplicationConfig applicationConfig, RegistryConfig registryConfig) {
        ProtocolConfig protocolConfig = getProtocolConfig("dubbo", 15635);
        return getProviderConfig(applicationConfig, registryConfig, protocolConfig);
    }

    @Bean(name = "NotificationServiceProviderConfig")
    public ProviderConfig getNotificationServiceProviderConfig(ApplicationConfig applicationConfig, RegistryConfig registryConfig) {
        ProtocolConfig protocolConfig = getProtocolConfig("dubbo", 15725);
        return getProviderConfig(applicationConfig, registryConfig, protocolConfig);
    }

    //===================================================================================
    private ProtocolConfig getProtocolConfig(String protocol, int port) {
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName(protocol);
        protocolConfig.setPort(port);
        //protocolConfig.setThreads(200);

        return protocolConfig;
    }

    private ProviderConfig getProviderConfig(ApplicationConfig applicationConfig,
                                             RegistryConfig registryConfig,
                                             ProtocolConfig protocolConfig) {
        ProviderConfig providerConfig = new ProviderConfig();
        providerConfig.setTimeout(5000);
        providerConfig.setRetries(0);
        providerConfig.setDelay(-1);
        providerConfig.setApplication(applicationConfig);
        providerConfig.setRegistry(registryConfig);
        providerConfig.setProtocol(protocolConfig);

        return providerConfig;
    }
}
