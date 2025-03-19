package org.g2.starter.zk.lock.listener;

import org.apache.curator.framework.CuratorFramework;
import org.g2.starter.core.helper.ApplicationContextHelper;
import org.springframework.boot.CommandLineRunner;

/**
 * @author wuwenxi 2021-06-21
 */
public class CuratorStartListener implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        CuratorFramework curatorFramework = ApplicationContextHelper.getApplicationContext().getBean(CuratorFramework.class);
        curatorFramework.start();
    }
}
