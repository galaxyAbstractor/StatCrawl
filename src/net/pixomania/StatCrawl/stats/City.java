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
public class City extends Location {

    public City(String name, int count){
        this.name = name;
        this.count = count;
    }
    
    @Override
    public ArrayList<Location> getList() {
        throw new UnsupportedOperationException("Should not be called");
    }

}
