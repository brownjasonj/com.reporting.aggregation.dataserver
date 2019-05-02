package com.reporting.aggregation.dataserver.servers.groupby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GroupDescriptor {
    private String name;
    private int levels;
    private String[] levelKeyNames;

    public GroupDescriptor(String name, int levels) {
        this.name = name;
        this.levels = levels;
        levelKeyNames = new String[levels];
    }

    public GroupDescriptor(String name, String[] levelKeyNames) {
        this(name, levelKeyNames.length);
        for(int i = 0; i < levels; i++)
            this.levelKeyNames[i] = levelKeyNames[i];
    }

    public String getName() {
        return name;
    }

    public int getLevels() {
        return levels;
    }

    public String[] getKeyNames() {
        return levelKeyNames;
    }

    public HashMap<String,String> getKvpsFromPath(String path) {
        String[] pathValues = path.split("/");
        HashMap<String,String> kvps = new HashMap<>();
        int max = (pathValues.length > levels) ? levels : pathValues.length;
        for (int i = 0; i < max; i++) {
            kvps.put(levelKeyNames[i], pathValues[i]);
        }
        return kvps;
    }

    public List<String> getGroupsFromPath(String path) {
        String[] pathValues = path.split("/");
        ArrayList<String> groups = new ArrayList<>();
        int max = (pathValues.length > levels) ? levels : pathValues.length;
        String currentPath = "";
        for(int i = 0; i < max; i++) {
            currentPath += "/" + pathValues[i];
            groups.add(currentPath);
        }
        return groups;
    }
}