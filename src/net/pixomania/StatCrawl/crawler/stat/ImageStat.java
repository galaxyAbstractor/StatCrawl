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
package net.pixomania.statcrawl.crawler.stat;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class ImageStat extends Stat {
    
    private ArrayList<String> pending = new ArrayList<String>();
    
    public ImageStat(Document html){
        super(html); 
    }
    
    @Override
    public void parse() {
        Elements images = html.select("img[src]");
        for (Element image : images) {
              
            String imageURL = image.attr("abs:src");
            
            // Make sure we have an URL, otherwise we are stuck here forever
            if(imageURL.isEmpty()) continue;
            if(!imageURL.startsWith("http")) continue;

            URL url;
            String stat = "";
            try {
                url = new URL(imageURL);
                // Open up a connection
                URLConnection conn = url.openConnection();
                conn.connect();
                
                // Determine the size
                int size = conn.getContentLength();
                String type = imageURL.substring(imageURL.lastIndexOf(".")+1, imageURL.length());
                // Fix issues with differentally cased extensions and jpeg/jpg
                type = type.toLowerCase();
                if(type.equals("jpeg")) type = "jpg";
                
                // If filesize couldn't be determined, set it to 0
                if(size > 0){
                    stat += size+" ";
                } else {
                    stat += "0 ";
                }
                
                // If type is too long, something is wrong (http://example.com/img/3dft4)
                if(type.length() < 5){
                    stat += type;
                } else {
                    stat += "unknown";
                }
                
            } catch (IOException ex) {
                Logger.getLogger(ImageStat.class.getName()).log(Level.SEVERE, null, ex);
                continue;
            } 
            
            // Add the URL to the pending list
            pending.add(stat); 
                 
        }
        save();
    }

    @Override
    public void save() {
        for(String stat : pending){
            QueueItem qi =  new QueueItem();
            qi.data = stat;
            qi.operation = Operation.IMAGE;
           DbStore.add(qi);
        }
    }
}
