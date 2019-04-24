package com.reporting.aggregation.dataserver.servers.groupby;

public class RowGroupColumn {
    String id;
    String displayName;
    String field;

    public RowGroupColumn() {}

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @return the field
     */
    public String getField() {
        return field;
    }   
}