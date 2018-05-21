package com.krly.project.batterymanagement.batteryserver;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;

/**
 * Created by Administrator on 2018/5/17.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        CommandLineParser parser = new BasicParser();
        Options options = new Options();
        options.addOption("h", "host", true, "Local IP address");
        options.addOption("p", "port", true, "Local IP port");

        CommandLine commandLine = parser.parse(options, args);
        final String host = commandLine.getOptionValue("h", "127.0.0.1");
        final int port = Integer.parseInt(commandLine.getOptionValue("p", "32321"));

        //
        MyProducer myProducer = MyProducer.getInstance();
        myProducer.start();

        //
        LocalCommandServer localCommandServer = new LocalCommandServer(host, port);
        localCommandServer.start();

        //
        BatteryServer batteryServer = new BatteryServer(host, port);
        batteryServer.addListener(ChannelContextHolder.getInstance());
        batteryServer.addListener(localCommandServer);

        IBatteryService batteryService = BatteryServiceImpl.getInstance();
        batteryServer.setBatteryService(batteryService);

        batteryServer.start();
    }
}
