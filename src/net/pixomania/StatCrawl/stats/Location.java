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
 * Different locations datatype, like Countries, Regions and Cities
 * @author galaxyAbstractor
 */
public abstract class Location {
    public String name;
    public int count;
    
    public abstract ArrayList<Location> getList();
}
