package cn.krly.utility.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;


public class CuratorUtils {
    public static CuratorFramework newCuratorFramework(String zkAddress) {
        CuratorFramework curatorFramework =
                CuratorFrameworkFactory.newClient(zkAddress, new ExponentialBackoffRetry(1000, 10));
        curatorFramework.start();

        return curatorFramework;
    }

    public static void createPathIfNotExists(CuratorFramework curatorFramework,
                                     String path,
                                     boolean isPersistent) throws Exception {
        Stat stat = curatorFramework.checkExists().forPath(path);
        if (stat != null)
            return;

        CreateMode mode = CreateMode.PERSISTENT;
        if (isPersistent == false)
            mode = CreateMode.EPHEMERAL;

        curatorFramework.create()
                .creatingParentsIfNeeded()
                .withMode(mode)
                .forPath(path);
    }
}
