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
 * A city
 * @author galaxyAbstractor
 */
public class City extends Location {

    /**
     * Set the name and count of the City
     * @param name The City name
     * @param count The count
     */
    public City(String name, int count){
        this.name = name;
        this.count = count;
    }
    /**
     * This should not be called as a city cannot have nodes
     * @return 
     */
    @Override
    public ArrayList<Location> getList() {
        throw new UnsupportedOperationException("Should not be called");
    }

}
