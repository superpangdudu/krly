package cn.krly.platform.transceiver.comsumer;

import cn.krly.platform.transceiver.api.ISimpleNetworkEventListener;
import cn.krly.platform.transceiver.api.ITransceiverMonitor;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;

@Component
public class TransceiverMonitorProxy implements ISimpleNetworkEventListener {
    
    @Reference(consumer = "DefaultConsumerConfig")
    private ITransceiverMonitor transceiverMonitor;

    @Override
    public void onConnected(int token, String host, int port) {
        //transceiverMonitor.onChannelConnected(token, host, port);
    }

    @Override
    public void onDisconnected(int token, String host, int port) {
        transceiverMonitor.onChannelClosed(token, host, port);
    }

    @Override
    public void onRead(int token, byte[] message, String host, int port) {
        //transceiverMonitor.onChannelRead(token, message, host, port);
        System.out.println(new String(message));
    }
}
