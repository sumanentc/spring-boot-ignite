package com.cache.config;

import com.cache.entity.Organization;
import com.cache.entity.Person;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.communication.tcp.TcpCommunicationSpi;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.apache.ignite.springdata22.repository.config.EnableIgniteRepositories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableIgniteRepositories(value = "com.cache.*")
public class IgniteCacheConfiguration {

    @Bean
    public Ignite igniteInstance() {
        return Ignition.start(igniteConfiguration());
    }

    @Bean(name = "igniteConfiguration")
    public IgniteConfiguration igniteConfiguration() {
        IgniteConfiguration igniteConfiguration = new IgniteConfiguration();
        igniteConfiguration.setIgniteInstanceName("testIgniteInstance");
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
            cacheConfiguration.setStatisticsEnabled(true);

            CacheConfiguration cacheConfiguration1 = new CacheConfiguration();
            cacheConfiguration1.setAtomicityMode(CacheAtomicityMode.ATOMIC);
            cacheConfiguration1.setCacheMode(CacheMode.REPLICATED);
            cacheConfiguration1.setName("student");
            cacheConfiguration1.setStatisticsEnabled(true);

            // Defining and creating a new cache to be used by Ignite Spring Data
            // repository.
            CacheConfiguration<Long,Person> ccfg = new CacheConfiguration("PersonCache");
            // Setting SQL schema for the cache.
            ccfg.setIndexedTypes(Long.class, Person.class);

            CacheConfiguration<Long, Organization> orgCacheCfg = new CacheConfiguration<>("ORG_CACHE");
            orgCacheCfg.setCacheMode(CacheMode.REPLICATED); // Default.
            orgCacheCfg.setIndexedTypes(Long.class, Organization.class);

            cacheConfigurations.add(ccfg);
            cacheConfigurations.add(orgCacheCfg);
            cacheConfigurations.add(cacheConfiguration);
            cacheConfigurations.add(cacheConfiguration1);

        return cacheConfigurations.toArray(new CacheConfiguration[cacheConfigurations.size()]);
    }
}
