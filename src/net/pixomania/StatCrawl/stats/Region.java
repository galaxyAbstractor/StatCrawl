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
 *
 * @author galaxyAbstractor
 */
public class Region extends Location {
    public String shortCode;
    
    public ArrayList<Location> cities = new ArrayList<Location>();
    /**
     * Set the name, shortcode and count of the region
     * @param name the name of the region
     * @param shortCode the shortcode of the region
     * @param count the count
     */
    public Region(String name, String shortCode, int count){
        this.name = name;
        this.shortCode = shortCode;
        this.count = count;
    }
    
    /**
     * Get the list of cities under this region
     * @return a list with City objects
     */
    @Override
    public ArrayList<Location> getList() {
        return cities;
    }
}
