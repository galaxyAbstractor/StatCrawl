/*******************************************************************
* StatCrawl
*
* Copyright (c) 2011, http://pixomania.net
*
* Licensed under the BSD License
* Redistributions of files must retain the above copyright notice.
*
* Please see LICENSE.txt for more info.
*
* @copyright Copyright 2011, pixomania, http://pixomania.net
* @license BSD license (http://www.opensource.org/licenses/bsd-license.php)
********************************************************************/
package net.pixomania.StatCrawl.stats;

import java.util.ArrayList;

/**
 * A Country
 * @author galaxyAbstractor
 */
public class Country extends Location {
    public String shortCode;
    
    public ArrayList<Location> regions = new ArrayList<Location>();
    /**
     * Set the name, shortcode and count of the country
     * @param name the name of the country
     * @param shortCode the shortcode of the country
     * @param count the count
     */
    public Country(String name, String shortCode, int count){
        this.name = name;
        this.shortCode = shortCode;
        this.count = count;
    }
    
    /**
     * Get the regions of this country
     * @return a list with Regions
     */
    @Override
    public ArrayList<Location> getList() {
        return regions;
    }
}
