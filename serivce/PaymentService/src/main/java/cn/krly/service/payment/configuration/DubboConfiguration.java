package cn.krly.service.payment.configuration;

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

    @Bean(name = "OrderServiceProviderConfig")
    public ProviderConfig getOrderServiceProviderConfig(ApplicationConfig applicationConfig, RegistryConfig registryConfig) {
        ProtocolConfig protocolConfig = getProtocolConfig("dubbo", 14625);
        return getProviderConfig(applicationConfig, registryConfig, protocolConfig);
    }

    @Bean(name = "ProductServiceProviderConfig")
    public ProviderConfig getProductServiceProviderConfig(ApplicationConfig applicationConfig, RegistryConfig registryConfig) {
        ProtocolConfig protocolConfig = getProtocolConfig("dubbo", 14635);
        return getProviderConfig(applicationConfig, registryConfig, protocolConfig);
    }

    @Bean(name = "AliPayServiceProviderConfig")
    public ProviderConfig getAliPayServiceProviderConfig(ApplicationConfig applicationConfig, RegistryConfig registryConfig) {
        ProtocolConfig protocolConfig = getProtocolConfig("dubbo", 14725);
        return getProviderConfig(applicationConfig, registryConfig, protocolConfig);
    }

    @Bean(name = "WechatPayServiceProviderConfig")
    public ProviderConfig getWechatPayServiceProviderConfig(ApplicationConfig applicationConfig, RegistryConfig registryConfig) {
        ProtocolConfig protocolConfig = getProtocolConfig("dubbo", 14825);
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
