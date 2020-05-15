package properties;
import org.springframework.cloud.stream.binder.BinderSpecificPropertiesProvider;

public class ActiveBindingProperties implements BinderSpecificPropertiesProvider{

    private ActiveConsumerProperties consumer = new ActiveConsumerProperties();

    private ActiveProducerProperties producer = new ActiveProducerProperties();

    public ActiveConsumerProperties getConsumer() {
        return consumer;
    }

    public ActiveProducerProperties getProducer() {
        return producer;
    }

}
