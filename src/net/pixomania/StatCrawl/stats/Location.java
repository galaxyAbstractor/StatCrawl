package net.pixomania.StatCrawl.stats;

import java.util.ArrayList;

/**
 * Different locations datatype, like Countries, Regions and Cities
 * @author galaxyAbstractor
 */
public abstract class Location {
    public String name;
    public int count;
    
    public abstract ArrayList<Location> getList();
}
