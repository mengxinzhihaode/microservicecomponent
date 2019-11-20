package com.micro.msaframework.registry.config;

public class ZookeeperConfig {
    private int timeout;
    private String path;

    public ZookeeperConfig(int timeout, String path) {
        this.timeout = timeout;
        this.path = path;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
