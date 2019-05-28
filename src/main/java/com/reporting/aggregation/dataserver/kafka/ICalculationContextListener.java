package com.reporting.aggregation.dataserver.kafka;

import com.reporting.mocks.model.CalculationContext;

public interface ICalculationContextListener {
    void update(CalculationContext calculationContext);
}