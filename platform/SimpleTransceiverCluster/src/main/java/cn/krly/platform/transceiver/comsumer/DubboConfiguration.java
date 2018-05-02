package cn.krly.platform.transceiver.comsumer;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class DubboConfiguration {
    public static String GROUP = "";

    @Bean(name = "DefaultConsumerConfig")
    public ConsumerConfig getConsumerConfig(ApplicationConfig applicationConfig, RegistryConfig registryConfig) {
        ConsumerConfig consumerConfig = new ConsumerConfig();
        consumerConfig.setGroup(GROUP);
        consumerConfig.setTimeout(3000);
        consumerConfig.setRetries(0);
        consumerConfig.setCheck(true);

        consumerConfig.setApplication(applicationConfig);
        consumerConfig.setRegistry(registryConfig);

        return consumerConfig;
    }
}
