/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.pixomania.StatCrawl.stats;

import java.util.ArrayList;

/**
 *
 * @author galaxyAbstractor
 */
public class Country extends Location {
    public String shortCode;
    
    public ArrayList<Location> regions = new ArrayList<Location>();
    
    public Country(String name, String shortCode, int count){
        this.name = name;
        this.shortCode = shortCode;
        this.count = count;
    }
    
    public ArrayList<Location> getList() {
        return regions;
    }
}
