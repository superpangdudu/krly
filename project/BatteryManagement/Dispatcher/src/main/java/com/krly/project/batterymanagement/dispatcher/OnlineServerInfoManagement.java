package com.krly.project.batterymanagement.dispatcher;

/**
 * 工作服务器管理器
 * 目前的实现仅仅作为演示使用
 * 后期考虑采用ZooKeeper做注册中心
 */
public class OnlineServerInfoManagement {
//    private static class InstanceHolder {
//        public static OnlineServerInfoManagement instance = new OnlineServerInfoManagement();
//    }
//
//    public static OnlineServerInfoManagement getInstance() {
//        return InstanceHolder.instance;
//    }

    private static volatile OnlineServerInfoManagement INSTANCE = null;

    private OnlineServerInfoManagement() {
    }

    public static OnlineServerInfoManagement getInstance() {
        if (INSTANCE == null) {
            synchronized (OnlineServerInfoManagement.class) {
                if (INSTANCE == null)
                    INSTANCE = new OnlineServerInfoManagement();
            }
        }

        return INSTANCE;
    }

    //===================================================================================
    static final class OnlineServerInfo {
        private String host;
        private int port;

        private OnlineServerInfo(String host, int port) {
            this.host = host;
            this.port = port;
        }

        String getHost() {
            return host;
        }

        int getPort() {
            return port;
        }
    }

    //===================================================================================
    void start() {
        // TODO 连接ZooKeeper，读取数据，监听节点
    }

    OnlineServerInfo getOnlineServerInfo() {
        // TODO 暂时返回一个固定地址，后期加入服务器选取算法
        OnlineServerInfo info = new OnlineServerInfo("127.0.0.1", 32321);
        return info;
    }
}
