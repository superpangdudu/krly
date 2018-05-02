package cn.krly.platform.transceiver;

import cn.krly.platform.transceiver.comsumer.DubboConfiguration;
import cn.krly.platform.transceiver.comsumer.TransceiverMonitorProxy;
import org.apache.commons.cli.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class SimpleTransceiverClusterApplication {

	public static void main(String[] args) throws Exception {
		CommandLineParser parser = new BasicParser();
		Options options = new Options();
		options.addOption("g", "group", true, "Working group");
		options.addOption("h", "host", true, "Local IP address");
		options.addOption("p", "port", true, "Local IP port");
		//options.addOption("zk", "zookeeper", true, "ZooKeeper address");

		CommandLine commandLine = parser.parse(options, args);
		final String clusterGroup = commandLine.getOptionValue("g", "default");
		final String host = commandLine.getOptionValue("h", "127.0.0.1");
		final int port = Integer.parseInt(commandLine.getOptionValue("p", "24114"));
		//final String zkAddress = commandLine.getOptionValue("zk", "127.0.0.1:2181");

		//
		DubboConfiguration.GROUP = clusterGroup;

		//
		ConfigurableApplicationContext ctx =
				SpringApplication.run(SimpleTransceiverClusterApplication.class, args);

		//
		TransceiverMonitorProxy proxy = ctx.getBean(TransceiverMonitorProxy.class);

		SimpleNetworkTransceiver transceiver = new SimpleNetworkTransceiver(host, port, proxy);
		transceiver.start();

		/*
		//
		new Thread(() -> {
			try {
				Thread.sleep(500); // FIXME waiting to complete binding

				CuratorFramework curatorFramework = CuratorUtils.newCuratorFramework(zkAddress);
				String rootPath = "/transceiver/" + clusterGroup;

				CuratorUtils.createPathIfNotExists(curatorFramework, rootPath, true);
				CuratorUtils.createPathIfNotExists(curatorFramework,
						rootPath + "/" + host + ":" + port,
						false);

			} catch (Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}).start();
		*/
	}
}
