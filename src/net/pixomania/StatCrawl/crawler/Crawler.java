package net.pixomania.StatCrawl.crawler;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.SwingWorker;
import net.pixomania.StatCrawl.db.Db;
import net.pixomania.StatCrawl.db.DbSingleton;
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
        Db db = DbSingleton.getDb();
        ArrayList<URL> pending = new ArrayList<URL>();
        // Crawl and parse for links until there is no more pending URL's
        do {
            System.out.println("run");
            // Get the first URL in the pending Set
            String url = db.getFirstPending();
            
            if(url == null){
                Thread.sleep(5000);
                System.out.println("Slept 5 seconds");
                continue;
            }
            
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
            
//            Main.linksAddedToPending = 0;
//            Main.linksFound = 0;
//            Main.linksTotal = 0;
        } while((Main.toggled != false));
        return null;
    }
    
    

}
