import com.reporting.aggregation.dataserver.kafka.CalculationContextKafkaConsumer;
import com.reporting.aggregation.dataserver.kafka.ICalculationContextListener;
import com.reporting.mocks.model.CalculationContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PricingGroupDataServer implements ICalculationContextListener {
    @Autowired
    private CalculationContextKafkaConsumer calculationContextKafkaConsumer;

    private String pricingGroupName;

    public PricingGroupDataServer(String pricingGroupName) {
        this.pricingGroupName = pricingGroupName;

    }

    @Override
    public void update(CalculationContext calculationContext) {

    }
}