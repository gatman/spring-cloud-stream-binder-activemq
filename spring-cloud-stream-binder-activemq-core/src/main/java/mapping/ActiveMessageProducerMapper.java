package mapping;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.jms.JmsProperties.AcknowledgeMode;
import org.springframework.context.Lifecycle;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.support.DefaultMessageBuilderFactory;
import org.springframework.integration.support.MessageBuilderFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.SimpleMessageConverter;
import org.springframework.messaging.MessageChannel;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageListener;


public class ActiveMessageProducerMapper implements MessageProducer, MessageListener, InitializingBean, Lifecycle {

    private DefaultMessageListenerContainer container;
    private MessageChannel outputChannel;
    private MessageBuilderFactory messageBuilderFactory = new DefaultMessageBuilderFactory();
    private MessageConverter messageConverter = new SimpleMessageConverter();
    private JmsTemplate jmsTemplate;
    private Destination destination;

    public DefaultMessageListenerContainer getContainer() {
        return container;
    }
    public void setContainer(DefaultMessageListenerContainer container) {
        this.container = container;
    }
    public MessageBuilderFactory getMessageBuilderFactory() {
        return messageBuilderFactory;
    }
    public void setMessageBuilderFactory(MessageBuilderFactory messageBuilderFactory) {
        this.messageBuilderFactory = messageBuilderFactory;
    }
    public MessageConverter getMessageConverter() {
        return messageConverter;
    }
    public void setMessageConverter(MessageConverter messageConverter) {
        this.messageConverter = messageConverter;
    }
    public JmsTemplate getJmsTemplate() {
        return jmsTemplate;
    }
    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }
    public Destination getDestination() {
        return destination;
    }
    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    @Override
    public void start() {
        container.start();
    }

    @Override
    public void stop() {
        container.stop();
    }

    @Override
    public boolean isRunning() {
        return container.isRunning();
    }


    @Override
    public void setOutputChannel(MessageChannel outputChannel) {
        this.outputChannel = outputChannel;
    }


    @Override
    public MessageChannel getOutputChannel() {
        return outputChannel;
    }

    @Override
    public void onMessage(Message message) {
        try {
            Object payload = this.messageConverter.fromMessage(message);
            org.springframework.messaging.Message<?> requestMessage = messageBuilderFactory.withPayload(payload).build();
            outputChannel.send(requestMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void afterPropertiesSet() {
        onInit();
        this.container.afterPropertiesSet();
    }
    private void onInit() {
        DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
        container.setMessageListener(this);
        container.setDestination(destination);
        ConnectionFactory producerConnection = jmsTemplate.getConnectionFactory();
        container.setConnectionFactory(producerConnection);
        container.setSessionAcknowledgeMode(AcknowledgeMode.AUTO.getMode());
        container.setSessionTransacted(false);
        this.container = container;
    }

}
