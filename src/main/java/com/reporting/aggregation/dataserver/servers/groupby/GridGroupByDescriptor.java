package com.reporting.aggregation.dataserver.servers.groupby;

import java.util.ArrayList;
import java.util.List;

public class GridGroupByDescriptor {
    private String name;
    private String[] groupColumns;
    private String[] valueColumns;

    public GridGroupByDescriptor(String name, String[] groupColuns, String[] nonGroupColumns) {
        this.name = name;
        this.groupColumns = groupColuns;
        this.valueColumns = nonGroupColumns;
    }

    public String getName() {
        return name;
    }

    public int getColumnCount() {
        return groupColumns.length + valueColumns.length;
    }

    public String[] getGroupColumns(){
        return groupColumns;
    }

    public String[] getValueColumns() {
        return valueColumns;
    }

    public List<String> getGroupsFromPath(String path) {
        String[] pathValues = path.split("/");
        ArrayList<String> groups = new ArrayList<>();
        int max = (pathValues.length > groupColumns.length) ? groupColumns.length : pathValues.length;
        String currentPath = "";
        for(int i = 1; i <= max; i++) {
            currentPath += "/" + pathValues[i];
            groups.add(currentPath);
        }
        return groups;
    }
}