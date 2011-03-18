package net.pixomania.StatCrawl.crawler;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.SwingWorker;
import net.pixomania.StatCrawl.db.Db;
import net.pixomania.StatCrawl.db.DbSingleton;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;



/**
 *
 * @author vigge
 */
public class Crawler extends SwingWorker<Void, Void> {
    
    @Override
    protected Void doInBackground() throws Exception {
        Db db = DbSingleton.getDb();
        Queue<String> pending = new LinkedList<String>();
        // Crawl and parse for links until there is no more pending URL's
        do {
            System.out.println("run");
            // Get the first URL in the pending Set
            pending = db.getFirstTenPending();
            
            if(pending.isEmpty()){
                Thread.sleep(5000);
                continue;
            }
            
            while(!pending.isEmpty()){
                String url = pending.poll();
                // Remove the first URL from the pending list
                db.removePending(url);

                // we have to make sure it's HTML output, not an image, before we parse it as HTML
                URL url2;
                try {
                    url2 = new URL(url);
                    if(!url2.openConnection().getContentType().contains("text/html")){
                        System.out.println("NOT HTML");
                        continue;
                    }
                } catch(MalformedURLException e){
                    System.out.println("Malformed URL");
                    continue;
                }

                // Connect and get the links from the HTML

                Document doc = Jsoup.connect(url).userAgent("StatCrawl").get();
                Elements links = doc.select("a[href]");

                System.out.println("Started parser");
                Parser parser = new Parser(links, url);
                parser.execute();
                
            }
            
        } while((CrawlView.toggled != false));
        return null;
    }
    
    

}
