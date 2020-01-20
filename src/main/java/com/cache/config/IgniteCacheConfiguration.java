package com.cache.config;

import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.spring.SpringCacheManager;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.communication.tcp.TcpCommunicationSpi;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.cache.Caching;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableCaching
public class IgniteCacheConfiguration {

    @Bean
    public CacheManager cacheManager() {
        SpringCacheManager cacheManager = new SpringCacheManager();
        cacheManager.setConfiguration(igniteConfiguration());
        return cacheManager;
    }

    @Bean(name = "igniteConfiguration")
    public IgniteConfiguration igniteConfiguration() {
        IgniteConfiguration igniteConfiguration = new IgniteConfiguration();
        igniteConfiguration.setGridName("testGrid");
        //igniteConfiguration.setClientMode(true);
        igniteConfiguration.setPeerClassLoadingEnabled(true);
        igniteConfiguration.setLocalHost("127.0.0.1");

        TcpDiscoverySpi tcpDiscoverySpi = new TcpDiscoverySpi();
        TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();
        ipFinder.setAddresses(Collections.singletonList("127.0.0.1:47500..47509"));
        tcpDiscoverySpi.setIpFinder(ipFinder);
        tcpDiscoverySpi.setLocalPort(47500);
        // Changing local port range. This is an optional action.
        tcpDiscoverySpi.setLocalPortRange(9);
        //tcpDiscoverySpi.setLocalAddress("localhost");
        igniteConfiguration.setDiscoverySpi(tcpDiscoverySpi);

        TcpCommunicationSpi communicationSpi = new TcpCommunicationSpi();
        communicationSpi.setLocalAddress("localhost");
        communicationSpi.setLocalPort(48100);
        communicationSpi.setSlowClientQueueLimit(1000);
        igniteConfiguration.setCommunicationSpi(communicationSpi);


        igniteConfiguration.setCacheConfiguration(cacheConfiguration());

        return igniteConfiguration;

    }

    @Bean(name = "cacheConfiguration")
    public CacheConfiguration[] cacheConfiguration() {
        List<CacheConfiguration> cacheConfigurations = new ArrayList<>();
            CacheConfiguration cacheConfiguration = new CacheConfiguration();
            cacheConfiguration.setAtomicityMode(CacheAtomicityMode.ATOMIC);
            cacheConfiguration.setCacheMode(CacheMode.REPLICATED);
            cacheConfiguration.setName("employee");
            cacheConfiguration.setWriteThrough(false);
            cacheConfiguration.setReadThrough(false);
            cacheConfiguration.setWriteBehindEnabled(false);
            cacheConfiguration.setBackups(1);
            cacheConfiguration.setStatisticsEnabled(true);

            CacheConfiguration cacheConfiguration1 = new CacheConfiguration();
            cacheConfiguration1.setAtomicityMode(CacheAtomicityMode.ATOMIC);
            cacheConfiguration1.setCacheMode(CacheMode.REPLICATED);
            cacheConfiguration1.setName("student");
            cacheConfiguration1.setWriteThrough(false);
            cacheConfiguration1.setReadThrough(false);
            cacheConfiguration1.setWriteBehindEnabled(false);
            cacheConfiguration1.setBackups(1);
            cacheConfiguration1.setStatisticsEnabled(true);

            cacheConfigurations.add(cacheConfiguration);
            cacheConfigurations.add(cacheConfiguration1);

        return cacheConfigurations.toArray(new CacheConfiguration[cacheConfigurations.size()]);
    }
}
