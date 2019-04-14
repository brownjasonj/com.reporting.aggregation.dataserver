package com.reporting.aggregation.dataserver.datamodel;

public class DataResponse {
    protected EDataResponseType dataResponseType;

    protected DataResponse(EDataResponseType dataResponseType) {
        this.dataResponseType = dataResponseType;
    }

    /**
     * @return the dataResponseType
     */
    public EDataResponseType getDataResponseType() {
        return dataResponseType;
    }
}