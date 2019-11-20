package com.micro.msaframework.registry;

public interface ServiceRegistry {

    /**
     * 注册服务信息
     * @param serviceName   服务名称
     * @param serviceAddredd    服务地址
     */
    void register(String serviceName, String serviceAddredd);
}
