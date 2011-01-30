package net.pixomania.StatCrawl.crawler;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.SwingWorker;
import net.pixomania.StatCrawl.db.Db;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



/**
 *
 * @author vigge
 */
public class Crawler extends SwingWorker<Void, Void> {
    
    @Override
    protected Void doInBackground() throws Exception {
        Db db = new Db();
        ArrayList<URL> pending = new ArrayList<URL>();
        // Crawl and parse for links until there is no more pending URL's
        do {
            // Get the first URL in the pending Set
            String url = db.getFirstPending();
            
            // we have to make sure it's HTML output, not an image, before we parse it as HTML
            URL url2;
            try {
                url2 = new URL(url);
                if(!url2.openConnection().getContentType().contains("text/html")){
                    System.out.println("NOT HTML");
                    db.removePending(url);
                    continue;
                }
            } catch(MalformedURLException e){
                System.out.println("Malformed URL");
                continue;
            }
                
            Main.setCurrentlyCrawling(url);
            // Connect and get the links from the HTML
            
            Document doc = Jsoup.connect(url).userAgent("StatCrawl").get();
            Elements links = doc.select("a[href]");

            for (Element link : links) {
                
                String linkURL = link.attr("abs:href");

                // Make sure we have an URL, otherwise we are stuck here forever
                if(linkURL.isEmpty()) continue;
                if(!linkURL.startsWith("http")) continue;
                
                URL url1;
                try {
                    url1 = new URL(linkURL);
                } catch(MalformedURLException e){
                    System.out.println("Malformed URL");
                    continue;
                }
                
                
                // Appends the URL to the output. This needs changing
                Main.appendToOutputArea(url1.toString());
                // Add the URL to the pending list
                pending.add(url1); 
                
                 
                // Update the status
                // Main.updateStatus();
                // Main.linksTotal++;
            }
            
            for(URL pUrl : pending){
               db.insertPending(pUrl.toString());                 
            }
            
            pending.clear();
            
            // This URL is crawled and does not need to be crawled again
            db.insertCrawled(url); 
            // Remove the first URL from the pending list
            db.removePending(url);
               
            db.insertIP(new URL(url).getHost());
            
            db.insertHost(new URL(url).getHost());
            
//            Main.linksAddedToPending = 0;
//            Main.linksFound = 0;
//            Main.linksTotal = 0;
        } while((db.getFirstPending() != null) && (Main.toggled != false));
        return null;
    }
    
    

}
