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
public abstract class Location {
    public String name;
    public int count;
    
    public abstract ArrayList<Location> getList();
}
