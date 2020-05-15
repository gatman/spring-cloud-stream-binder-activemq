package stream;

import org.springframework.beans.BeanUtils;
import org.springframework.cloud.stream.binder.*;
import org.springframework.cloud.stream.provisioning.ConsumerDestination;
import org.springframework.cloud.stream.provisioning.ProducerDestination;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.jms.JmsSendingMessageHandler;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import mapping.ActiveMessageProducerMapper;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import javax.jms.*;
import java.util.Map;
import properties.*;
import provisioning.ActiveProvisioner;


public class ActiveMessageChannelBinder extends AbstractMessageChannelBinder<ExtendedConsumerProperties<ActiveConsumerProperties>,
        ExtendedProducerProperties<ActiveProducerProperties>, ActiveProvisioner>
        implements ExtendedPropertiesBinder<MessageChannel, ActiveConsumerProperties, ActiveProducerProperties> {

    private static final String TYPE_TOPIC="topic";

    private ActiveExtendedBindingProperties extendedProperties;

    private ActiveBinderConfigurationProperties activeBinderConfigurationProperties;

    @Override
    public Map<String, ?> getBindings() {
        return null;
    }
    @Override
    public String getDefaultsPrefix() {
        return "";
    }
    @Override
    public Class<? extends BinderSpecificPropertiesProvider> getExtendedPropertiesEntryClass() {
        return null;
    }


    public ActiveMessageChannelBinder(ActiveProvisioner provisioningProvider,
                                        ActiveBinderConfigurationProperties activeBinderConfigurationProperties) {
        super(new String[0], provisioningProvider);
        this.activeBinderConfigurationProperties = activeBinderConfigurationProperties;
    }

    public void setExtendedProperties(ActiveExtendedBindingProperties extendedProperties) {
        this.extendedProperties = extendedProperties;
    }

    @Override
    public ActiveConsumerProperties getExtendedConsumerProperties(String channelName) {
        ActiveConsumerProperties consumer = new ActiveConsumerProperties();
        BeanUtils.copyProperties(this.extendedProperties.getExtendedConsumerProperties(channelName),consumer);
        return consumer;
    }

    @Override
    public ActiveProducerProperties getExtendedProducerProperties(String channelName) {
        ActiveProducerProperties producerProperties = new ActiveProducerProperties();
        BeanUtils.copyProperties(this.extendedProperties.getExtendedProducerProperties(channelName),producerProperties);
        return producerProperties;
    }

    @Override
    protected MessageHandler createProducerMessageHandler(ProducerDestination destination,
                                                          ExtendedProducerProperties<ActiveProducerProperties> producerProperties, MessageChannel errorChannel) {

        JmsTemplate jmsTemplate = new JmsTemplate(getConnectionFactory());
        if (producerProperties.getExtension().isTransaction()){
            jmsTemplate.setSessionTransacted(true);
        }else {
            jmsTemplate.setSessionTransacted(false);
        }
        JmsSendingMessageHandler jmsSendingMessageHandler = new JmsSendingMessageHandler(jmsTemplate);

        if (TYPE_TOPIC.equals(producerProperties.getExtension().getType())){
            jmsTemplate.setDefaultDestination(new ActiveMQTopic(destination.getName()));
            jmsSendingMessageHandler.setDestination(new ActiveMQTopic(destination.getName()));
        }else {
            jmsTemplate.setDefaultDestination(new ActiveMQQueue(destination.getName()));
            jmsSendingMessageHandler.setDestination(new ActiveMQQueue(destination.getName()));
        }
        jmsSendingMessageHandler.setBeanFactory(getApplicationContext());
        return jmsSendingMessageHandler;
    }

    @Override
    protected MessageProducer createConsumerEndpoint(ConsumerDestination destination, String group,
                                                     ExtendedConsumerProperties<ActiveConsumerProperties> properties){
        ActiveMessageProducerMapper producer = new ActiveMessageProducerMapper();

        JmsTemplate jmsTemplate = new JmsTemplate(getConnectionFactory());
        if (TYPE_TOPIC.equals(properties.getExtension().getType())){
            jmsTemplate.setDefaultDestination(new ActiveMQTopic(destination.getName()));
        }else {
            jmsTemplate.setDefaultDestination(new ActiveMQQueue(destination.getName()));
        }

        jmsTemplate.setDeliveryMode(DeliveryMode.PERSISTENT);
        producer.setJmsTemplate(jmsTemplate);
        if (TYPE_TOPIC.equals(properties.getExtension().getType())){
            producer.setDestination(new ActiveMQTopic(destination.getName()));
        }else {
            producer.setDestination(new ActiveMQQueue(destination.getName()));
        }
        return producer;
    }

    private ActiveMQConnectionFactory getConnectionFactory(){
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL(activeBinderConfigurationProperties.getHost());
        activeMQConnectionFactory.setUserName(activeBinderConfigurationProperties.getUser());
        activeMQConnectionFactory.setPassword(activeBinderConfigurationProperties.getPassword());
        return activeMQConnectionFactory;
    }
}
