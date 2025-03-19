package org.g2.starter.core.thread.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 线程池配置
 *
 * @author wuwenxi 2021-07-16
 */
@ConfigurationProperties(prefix = "g2.thread.scheduled")
public class ScheduledPoolProperties {

    /**
     * 是否启用
     */
    private boolean enable;
    /**
     * 线程池核心线程数
     */
    private int corePoolSize;
    /**
     * 线程名前缀
     */
    private String prefixName;
    /**
     * 是否后台线程
     */
    private boolean daemon;

    public ScheduledPoolProperties() {
        this.enable = false;
        this.corePoolSize = 15;
        this.prefixName = "schedule-task-";
        this.daemon = true;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public String getPrefixName() {
        return prefixName;
    }

    public void setPrefixName(String prefixName) {
        this.prefixName = prefixName;
    }

    public boolean isDaemon() {
        return daemon;
    }

    public void setDaemon(boolean daemon) {
        this.daemon = daemon;
    }
}
