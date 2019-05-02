package com.reporting.aggregation.dataserver.servers.groupby.requests;

public class GroupByRequest {
    int startRow;
    int endRow;
    RowGroupColumn[] rowGroupCols;
    String[] valueCols;
    String[] pivotCols;
    boolean pivotMode;
    String[] groupKeys;

    public GroupByRequest() {}

    public String getGroup() {
        if (groupKeys.length == 0)
            return "/";
        else {
            String groupName = "";
            for(String key : groupKeys) {
               groupName += key;
            }
            return groupName;
        }
    }
    /**
     * @return the startRow
     */
    public int getStartRow() {
        return startRow;
    }

    /**
     * @return the endRow
     */
    public int getEndRow() {
        return endRow;
    }

    /**
     * @return the rowGroupColumns
     */
    public RowGroupColumn[] getRowGroupCols() {
        return rowGroupCols;
    }

    /**
     * @return the valueCols
     */
    public String[] getValueCols() {
        return valueCols;
    }

    /**
     * @return the pivotCols
     */
    public String[] getPivotCols() {
        return pivotCols;
    }

    /**
     * @return the pivotMode
     */
    public boolean isPivotMode() {
        return pivotMode;
    }

    /**
     * @return the groupKeys
     */
    public String[] getGroupKeys() {
        return groupKeys;
    }    
}