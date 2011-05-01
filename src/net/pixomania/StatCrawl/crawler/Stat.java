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
package net.pixomania.StatCrawl.crawler;

import org.jsoup.nodes.Document;

/**
 * The abstract for each stat to use
 * @author galaxyAbstractor
 */
public abstract class Stat {

    protected Document html;
    
    public Stat(Document html) {
        this.html = html;
    }
    
    public abstract void parse();
    public abstract void save();
    
}
