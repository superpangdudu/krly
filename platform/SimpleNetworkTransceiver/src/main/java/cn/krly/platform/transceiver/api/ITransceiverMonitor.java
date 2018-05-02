package cn.krly.platform.transceiver.api;

/**
 * Created by Administrator on 2018/4/26.
 */
public interface ITransceiverMonitor {
    void onChannelConnected(int token, String host, int port);
    void onChannelClosed(int token, String host, int port);
    void onChannelRead(int token, byte[] message, String host, int port);
}
