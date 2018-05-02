package cn.krly.platform.transceiver;


import static java.lang.Thread.sleep;

public class Main {

    static void main(String[] args) throws Exception {
        String host = "127.0.0.1";
        int port = 14127;

        final SimpleNetworkTransceiver transceiver =
                new SimpleNetworkTransceiver(host, port, null);

        transceiver.start();

        //
        Thread worker = new Thread(() -> {
            while (true) {
                try {
                    sleep(10000);
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
        });

        worker.start();
        worker.join();
    }
}
