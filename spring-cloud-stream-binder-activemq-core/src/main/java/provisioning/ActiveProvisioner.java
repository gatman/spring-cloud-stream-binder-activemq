package provisioning;

import org.springframework.cloud.stream.binder.ExtendedConsumerProperties;
import org.springframework.cloud.stream.binder.ExtendedProducerProperties;
import org.springframework.cloud.stream.provisioning.ConsumerDestination;
import org.springframework.cloud.stream.provisioning.ProducerDestination;
import org.springframework.cloud.stream.provisioning.ProvisioningException;
import org.springframework.cloud.stream.provisioning.ProvisioningProvider;
import org.springframework.util.StringUtils;
import properties.ActiveConsumerProperties;
import properties.ActiveProducerProperties;


public class ActiveProvisioner implements
        ProvisioningProvider<ExtendedConsumerProperties<ActiveConsumerProperties>,
        ExtendedProducerProperties<ActiveProducerProperties>>{


    private static final class ActiveConsumerDestination implements ConsumerDestination{
        private String destination;

        ActiveConsumerDestination(String destination) {
            super();
            this.destination = destination;
        }

        @Override
        public String getName() {
            return this.destination;
        }
    }

    private static final class ActiveProducerDestination implements ProducerDestination{
        private String destination;

        ActiveProducerDestination(String destination) {
            super();
            this.destination = destination;
        }

        @Override
        public String getName() {
            return this.destination;
        }

        @Override
        public String getNameForPartition(int partition) {
            return null;
        }
    }
    @Override
    public ConsumerDestination provisionConsumerDestination(String name, String group,
                                                            ExtendedConsumerProperties<ActiveConsumerProperties> properties)
            throws ProvisioningException {
        String destination = StringUtils.isEmpty(properties.getExtension().getDestination()) ? name: properties.getExtension().getDestination();
        return new ActiveConsumerDestination(destination);
    }
    @Override
    public ProducerDestination provisionProducerDestination(String name,
                                                            ExtendedProducerProperties<ActiveProducerProperties> properties)
            throws ProvisioningException {
        String destination = StringUtils.isEmpty(properties.getExtension().getDestination()) ? name : properties.getExtension().getDestination();
        return new ActiveProducerDestination(destination);
    }

}


