package cn.krly.platform.transceiver;

import cn.krly.platform.transceiver.provider.DubboConfiguration;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SimpleTransceiverMonitorApplication {

	public static void main(String[] args) throws Exception {
		CommandLineParser parser = new BasicParser();
		Options options = new Options();
		options.addOption("g", "group", true, "Working group");
		options.addOption("h", "host", true, "Local IP address");
		options.addOption("p", "port", true, "Local IP port");

		CommandLine commandLine = parser.parse(options, args);
		final String clusterGroup = commandLine.getOptionValue("g", "default");
		final String host = commandLine.getOptionValue("h", "127.0.0.1");
		final int port = Integer.parseInt(commandLine.getOptionValue("p", "14116"));

        DubboConfiguration.GROUP = clusterGroup;
        DubboConfiguration.HOST = host;
        DubboConfiguration.PORT = port;

		//
		ConfigurableApplicationContext ctx =
				SpringApplication.run(SimpleTransceiverMonitorApplication.class, args);
	}
}
