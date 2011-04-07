package net.pixomania.StatCrawl.crawler;


import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.pixomania.StatCrawl.db.Db;
import net.pixomania.StatCrawl.db.DbQueue;
import net.pixomania.StatCrawl.db.DbSingleton;
import net.pixomania.StatCrawl.db.Operation;
import net.pixomania.StatCrawl.db.QueueItem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;



/**
 * The Crawler thread. This fetches 10 links from the pending table and
 * starts a parser for each one of them.
 * @author galaxyAbstractor
 */
public class Crawler extends Thread {
    
    private boolean run = true;
    
    /**
     * Indicates to the crawler that it needs to stop
     */
    public void stopCrawler(){
        run = false;
    }
    
    /**
     * Resets the run boolean back to true so we can restart the crawler
     */
    public void resetCrawler(){
        run = true;
    }
    
    @Override
    public void run() {
        Db db = DbSingleton.getDb();
        Queue<String> pending = new LinkedList<String>();
        // Crawl and parse for links until there is no more pending URL's
        while(run){
            System.out.println("run");
            // Get the first ten URLs from the database
            pending = db.getFirstTenPending();
            
            // If the pending list is empty, there may still be parsers that 
            // parses. Wait 5 seconds, then start from the beginning of the loop
            if(pending.isEmpty()){
                try {
                    this.sleep(5000);
                } catch (InterruptedException ex) {
                    run = false;
                }
                continue;
            }
            
            while(!pending.isEmpty()){
                System.out.println(pending.size() + " more to crawl");
                
                // Get the first element in the queue
                String url = pending.poll();
                
                // Remove the first URL from the pending list
                DbQueue.addQuery(new QueueItem(url, Operation.REMOVE));

                // we have to make sure it's HTML output, not an image, before we parse it as HTML
                URL url2;
                try {
                    url2 = new URL(url);
                    if(!url2.openConnection().getContentType().contains("text/html")){
                        System.out.println("NOT HTML");
                        continue;
                    }
                } catch (IOException ex) {
                    System.out.println("Malformed URL");
                    continue;
                }

                // Connect and get the links from the HTML
                Document html;
                try {
                    html = Jsoup.connect(url).userAgent("StatCrawl").get();
                    
                    System.out.println("Started parser");
                    Parser parser = new Parser(html, url);
                    parser.execute();
                } catch (IOException ex) {
                    Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            
            // Wait for the DbQueue to reach zero before doing next batch of
            // Crawls
            while(DbQueue.getSize() != 0){
                try {
                    this.sleep(5000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        System.out.println("Crawler has stopped");
        DbQueue.stopQueue();
        
    }
    
    

}
