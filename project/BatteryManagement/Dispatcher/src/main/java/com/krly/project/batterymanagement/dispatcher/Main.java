package com.krly.project.batterymanagement.dispatcher;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;

/**
 * 充电台服务器分派器
 * 按照协议的要求，充电台在上电后会首先连接此服务器，获取实际工作服务器的地址
 */
public class Main {

    public static void main(String[] args) throws Exception {
        CommandLineParser parser = new BasicParser();
        Options options = new Options();
        options.addOption("h", "host", true, "Local IP address");
        options.addOption("p", "port", true, "Local IP port");

        CommandLine commandLine = parser.parse(options, args);
        final String host = commandLine.getOptionValue("h", "127.0.0.1");
        final int port = Integer.parseInt(commandLine.getOptionValue("p", "34116"));

        //
        OnlineServerInfoManagement.getInstance().start();

        //
        DispatcherServer server = new DispatcherServer(host, port);
        server.start();
    }
}
