package com.micro.msaframework.registry.registryimpl;

import com.micro.msaframework.registry.ServiceRegistry;
import com.micro.msaframework.registry.config.ZookeeperConfig;
import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

public class ServiceRegistryImpl implements ServiceRegistry, Watcher {

    private ZooKeeper zk;

    private Logger logger = LoggerFactory.getLogger(ServiceRegistryImpl.class);

    private CountDownLatch latch = new CountDownLatch(1);

    private ZookeeperConfig zookeeperConfig = new ZookeeperConfig(50000, "/registry");

    public ServiceRegistryImpl(String zkServers) {
        try {
            zk = new ZooKeeper(zkServers, zookeeperConfig.getTimeout(), this);
            latch.await();
            logger.info("connected to zookeeper");
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("throws exception");
        }
    }

    @Override
    public void register(String serviceName, String serviceAddress) {
        try {
            logger.info("begin register...");
            //创建根节点
            String registryPath = zookeeperConfig.getPath();
            if(zk.exists(registryPath, false) == null ) {
                zk.create(registryPath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                logger.info("create registry node: {}", registryPath);
            }

            //创建服务节点
            String servicePath = registryPath + "/" +serviceName;
            if(zk.exists(servicePath, false) == null) {
                zk.create(servicePath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                logger.info("create service node: {}", servicePath);
            }

            //创建地址节点
            String address = servicePath + "/address-";
            String addressNode = zk.create(address, serviceAddress.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            logger.info("create address node: {} => {}", addressNode, serviceAddress);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("register successful...");
    }


    @Override
    public void process(WatchedEvent watchedEvent) {

        if(watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            latch.countDown();
        }

    }
}
