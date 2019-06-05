package com.reporting.aggregation.dataserver.servers.riskreporting.ignite;

import com.reporting.mocks.model.RiskResult;
import com.reporting.mocks.model.risks.Risk;

public interface IRiskResultListener {
    void update(RiskResult<? extends Risk> riskResult);
}