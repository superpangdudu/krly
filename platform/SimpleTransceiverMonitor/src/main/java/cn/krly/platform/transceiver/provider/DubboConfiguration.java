package cn.krly.platform.transceiver.provider;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ProviderConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DubboConfiguration {
    public static String GROUP = "";
    public static String HOST = "";
    public static int PORT = 0;

    @Bean(name = "DefaultProviderConfig")
    public ProviderConfig getProviderConfig(ApplicationConfig applicationConfig,
                                            RegistryConfig registryConfig,
                                            ProtocolConfig protocolConfig) {
        ProviderConfig providerConfig = new ProviderConfig();

        providerConfig.setGroup(GROUP);
        protocolConfig.setHost(HOST);
        protocolConfig.setPort(PORT);

        providerConfig.setApplication(applicationConfig);
        providerConfig.setRegistry(registryConfig);
        providerConfig.setProtocol(protocolConfig);

        return providerConfig;
    }
}
