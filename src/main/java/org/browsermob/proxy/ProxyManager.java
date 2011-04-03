package org.browsermob.proxy;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.inject.Provider;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
public class ProxyManager {
    private AtomicInteger portCounter = new AtomicInteger(9090);
    private Provider<ProxyServer> proxyServerProvider;
    private Map<Integer, ProxyServer> proxies = new ConcurrentHashMap<Integer, ProxyServer>();

    @Inject
    public ProxyManager(Provider<ProxyServer> proxyServerProvider) {
        this.proxyServerProvider = proxyServerProvider;
    }

    public int create() throws Exception {
        int port = portCounter.incrementAndGet();
        ProxyServer proxy = proxyServerProvider.get();
        proxy.setPort(port);
        proxy.start();
        proxies.put(port, proxy);

        return port;
    }

    public void delete(int port) throws Exception {
        ProxyServer proxy = proxies.remove(port);
        proxy.stop();
    }
}