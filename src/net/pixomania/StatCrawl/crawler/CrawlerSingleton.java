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

/**
 *
 * @author galaxyAbstractor
 */
public class CrawlerSingleton {
    private static Crawler crawler;

    private CrawlerSingleton() {

    }

    /**
     * Returns the Crawler object
     * @return the crawler object
     */
    public static synchronized Crawler getCrawler(){
        if(crawler == null) crawler = new Crawler();
        return crawler;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}
