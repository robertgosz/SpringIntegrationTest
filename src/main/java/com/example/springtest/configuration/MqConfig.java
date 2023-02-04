package com.example.springtest.configuration;

import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.mq.spring.boot.MQConfigurationProperties;
import com.ibm.mq.spring.boot.MQConnectionFactoryCustomizer;
import com.ibm.mq.spring.boot.MQConnectionFactoryFactory;
import com.ibm.msg.client.wmq.common.CommonConstants;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;

import javax.jms.JMSException;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "mq.config")
public class MqConfig extends MQConfigurationProperties {

    protected int sessionCacheSize;
    protected int clientReconnectionTimeout;
    protected int pollingInterval;

    @Bean
    public CachingConnectionFactory mqCachingConnectionFactory(MQConnectionFactory mqConnectionFactory) {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setSessionCacheSize(sessionCacheSize);
        cachingConnectionFactory.setReconnectOnException(true);
        cachingConnectionFactory.setTargetConnectionFactory(mqConnectionFactory);
        return cachingConnectionFactory;
    }

    @Bean
    public MQConnectionFactory mqConnectionFactory(ObjectProvider<List<MQConnectionFactoryCustomizer>> factoryCustomizers) throws JMSException {
        MQConnectionFactory mqConnectionFactoryFactory = new MQConnectionFactoryFactory(this,
                factoryCustomizers.getIfAvailable()).createConnectionFactory(MQConnectionFactory.class);
        mqConnectionFactoryFactory.setConnectionNameList(this.getConnName());
        mqConnectionFactoryFactory.setTransportType(CommonConstants.WMQ_CM_CLIENT);
        mqConnectionFactoryFactory.setClientReconnectTimeout(clientReconnectionTimeout);
        mqConnectionFactoryFactory.setPollingInterval(pollingInterval);
        return mqConnectionFactoryFactory;
    }

    public int getSessionCacheSize() {
        return sessionCacheSize;
    }

    public void setSessionCacheSize(int sessionCacheSize) {
        this.sessionCacheSize = sessionCacheSize;
    }

    public int getClientReconnectionTimeout() {
        return clientReconnectionTimeout;
    }

    public void setClientReconnectionTimeout(int clientReconnectionTimeout) {
        this.clientReconnectionTimeout = clientReconnectionTimeout;
    }

    public int getPollingInterval() {
        return pollingInterval;
    }

    public void setPollingInterval(int pollingInterval) {
        this.pollingInterval = pollingInterval;
    }

}
