package net.pixomania.StatCrawl.crawler.stat;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import org.jsoup.nodes.Element;
import net.pixomania.StatCrawl.crawler.Stat;
import net.pixomania.StatCrawl.db.DbQueue;
import net.pixomania.StatCrawl.db.Md5;
import net.pixomania.StatCrawl.db.Operation;
import net.pixomania.StatCrawl.db.QueueItem;
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
           DbQueue.addQuery(new QueueItem(url, Operation.PENDING));
        }
    }

}
