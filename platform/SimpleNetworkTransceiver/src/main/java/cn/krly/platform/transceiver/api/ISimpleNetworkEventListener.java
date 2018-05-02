package cn.krly.platform.transceiver.api;


public interface ISimpleNetworkEventListener {
    void onConnected(int token, String host, int port);
    void onDisconnected(int token, String host, int port);
    void onRead(int token, byte[] message, String host, int port);
}
