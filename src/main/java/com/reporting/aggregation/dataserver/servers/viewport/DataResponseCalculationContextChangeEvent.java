package com.reporting.aggregation.dataserver.servers.viewport;

import com.reporting.mocks.model.CalculationContext;

public class DataResponseCalculationContextChangeEvent extends DataResponse {
    protected CalculationContext calculationContext;

    public DataResponseCalculationContextChangeEvent(CalculationContext calculationContext) {
        super(EDataResponseType.CalculationContextChangeEvent);
        this.calculationContext = calculationContext;
    }

    public CalculationContext getCalculationContext() {
        return calculationContext;
    }    
}