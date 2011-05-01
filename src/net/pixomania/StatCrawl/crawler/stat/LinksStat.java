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
package net.pixomania.StatCrawl.crawler.stat;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import org.jsoup.nodes.Element;
import net.pixomania.StatCrawl.crawler.Stat;
import net.pixomania.StatCrawl.networking.DbStore;
import net.pixomania.StatCrawl.networking.Operation;
import net.pixomania.StatCrawl.networking.QueueItem;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 * @author galaxyAbstractor
 */
public class LinksStat extends Stat {
    
    private ArrayList<URL> pending = new ArrayList<URL>();
    
    public LinksStat(Document html){
        super(html); 
    }
    
    @Override
    public void parse() {
        Elements links = html.select("a[href]");
        for (Element link : links) {
                
            String linkURL = link.attr("abs:href");

            // Make sure we have an URL, otherwise we are stuck here forever
            if(linkURL.isEmpty()) continue;
            if(!linkURL.startsWith("http")) continue;

            URL url;
            try {
                url = new URL(linkURL);
            } catch(MalformedURLException e){
                System.out.println("Malformed URL");
                continue;
            }
            
            // Add the URL to the pending list
            pending.add(url); 
                 
        }
        save();
    }

    @Override
    public void save() {
        for(URL pUrl : pending){
           String url = pUrl.toString();
           QueueItem qi = new QueueItem();
           qi.data = url;
           qi.operation = Operation.PENDING;
           DbStore.add(qi);
        }
    }

}
