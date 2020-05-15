package properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.stream.binder.AbstractExtendedBindingProperties;
import org.springframework.cloud.stream.binder.BinderSpecificPropertiesProvider;
import java.util.Map;


@ConfigurationProperties("spring.cloud.stream.active")
public class ActiveExtendedBindingProperties extends AbstractExtendedBindingProperties<ActiveConsumerProperties, ActiveProducerProperties, ActiveBindingProperties> {


    @Override
    public Map<String, ActiveBindingProperties> getBindings() {
        return this.doGetBindings();
    }

    @Override
    public String getDefaultsPrefix() {
        return "";
    }

    @Override
    public Class<? extends BinderSpecificPropertiesProvider> getExtendedPropertiesEntryClass() {
        return ActiveBindingProperties.class;
    }
}
