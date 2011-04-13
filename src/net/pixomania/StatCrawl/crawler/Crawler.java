package net.pixomania.StatCrawl.crawler;


import com.esotericsoftware.kryonet.Client;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.pixomania.StatCrawl.client.ClientSingleton;
import net.pixomania.StatCrawl.db.Db;
import net.pixomania.StatCrawl.db.DbQueue;
import net.pixomania.StatCrawl.db.DbSingleton;
import net.pixomania.StatCrawl.networking.DbStore;
import net.pixomania.StatCrawl.networking.Operation;
import net.pixomania.StatCrawl.networking.Packet;
import net.pixomania.StatCrawl.networking.QueueItem;
import net.pixomania.StatCrawl.networking.Type;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;



/**
 * The Crawler thread. This fetches 10 links from the pending table and
 * starts a parser for each one of them.
 * @author galaxyAbstractor
 */
public class Crawler extends Thread {
    
    private boolean run = true;
    private Client client = ClientSingleton.getClient();
    private Queue<String> pending = new LinkedList<String>();
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

    /**
     * Fills the pending queue with the fetched items
     * @param pending
     */
    public void setPending(LinkedList<String> pending){
        this.pending = pending;
    }
    
    @Override
    public void run() {
        Db db = DbSingleton.getDb();

        // Get the first ten URLs from the database
        Packet fetch = new Packet("null", Type.FETCH);
        // Send it to the Server
        client.sendTCP(fetch);

        // Crawl and parse for links until there is no more pending URL's
        while(run){
            System.out.println("run");

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
                DbStore.add(new QueueItem(url, Operation.REMOVE));

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

            // Send the stored queries to the server. Then reset the local queue.
            Packet storedQueries = new Packet(DbStore.getList(), Type.QUERIES);
            client.sendTCP(storedQueries);
            DbStore.reset();

            // Request more links
            client.sendTCP(fetch);
        }
        System.out.println("Crawler has stopped");
    }
}