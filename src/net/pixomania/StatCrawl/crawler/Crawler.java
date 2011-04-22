package net.pixomania.StatCrawl.crawler;


import com.esotericsoftware.kryonet.Client;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.pixomania.StatCrawl.client.ClientSingleton;
import net.pixomania.StatCrawl.client.ClientView;
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

    private int doneParsing;
    private int sitesToCrawl;
    
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
    public synchronized void setPending(LinkedList<String> pending){
        this.pending = pending;
        this.sitesToCrawl = pending.size();
        this.notify();
    }

    public int getSitesToCrawl(){
        return sitesToCrawl;
    }

    public int getDoneParsing(){
        return doneParsing;
    }

    /**
     * Notifies that a parser thread is done with its operations
     */
    public void doneParsing(){
        doneParsing++;
    }

    @Override
    public void run() {
        crawl();
    }

    private synchronized void crawl(){
        // Get the first ten URLs from the database
        Packet fetch = new Packet();
        fetch.type = Type.FETCH;
        // Send it to the Server
        client.sendTCP(fetch);

        // Crawl and parse for links until there is no more pending URL's
        while(run){
            System.out.println("run");

            // If the pending list is empty, there may still be parsers that
            // parses. Wait 5 seconds, then start from the beginning of the loop
            if(pending.isEmpty() && (doneParsing == 0)){
                try {
                    this.wait();
                } catch (InterruptedException ex) {
                    run = false;
                    break;
                }
            }

            while(!pending.isEmpty()){
                System.out.println(pending.size() + " more to crawl");

                // Get the first element in the queue
                String url = pending.poll();

                // we have to make sure it's HTML output, not an image, before we parse it as HTML
                URL url2 = null;
                try {
                    url2 = new URL(url);
                    URLConnection connection = url2.openConnection();
                    // See if we can connect
                    try {
                        connection.connect();
                    } catch (IOException ex) {
                        System.out.println("Timeout");
                        String host = url2.getHost();
                        try {
                            QueueItem qi = new QueueItem();
                            qi.data = host + " " + InetAddress.getByName(host).getHostAddress() + " timeout";
                            qi.operation = Operation.ERROR;
                            DbStore.add(qi);
                            // Remove one from the counter
                            sitesToCrawl--;
                        } catch (UnknownHostException ex1) {
                            // Remove one from the counter
                            sitesToCrawl--;
                            QueueItem qi = new QueueItem();
                            qi.data = host + " unknownIP timeout";
                            qi.operation = Operation.ERROR;
                            DbStore.add(qi);
                        }
                        continue;
                    }
                    // Get the contenttype
                    String contenttype = connection.getContentType();
                    if((!contenttype.contains("text/html")) || (contenttype == null)){
                        System.out.println("NOT HTML");
                        // Remove one from the counter
                        sitesToCrawl--;
                        continue;
                    }
                }  catch (IOException ex) {
                    System.out.println("Malformed URL");
                    String host = url2.getHost();
                    try {
                        QueueItem qi = new QueueItem();
                        qi.data = host + " " + InetAddress.getByName(host).getHostAddress() + " other/malformed";
                        qi.operation = Operation.ERROR;
                        DbStore.add(qi);
                        // Remove one from the counter
                        sitesToCrawl--;
                    } catch (UnknownHostException ex1) {
                        // Remove one from the counter
                        sitesToCrawl--;
                        QueueItem qi = new QueueItem();
                        qi.data = host + " unknownIP other/malformed";
                        qi.operation = Operation.ERROR;
                        DbStore.add(qi);
                    }
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


            if(doneParsing == sitesToCrawl){
                // Send the stored queries to the server. Then reset the local queue.

                Packet storedQueries = new Packet();
                System.out.println("Created new packet");
                storedQueries.data = DbStore.getList();
                System.out.println("Got list");
                storedQueries.type = Type.QUERIES;
                System.out.println("Put type QUERIES");
                client.sendTCP(storedQueries); // DISCONNECT???
                System.out.println("Sent package");

                DbStore.reset();
                System.out.println("Reseted DbStore");

                // Reset some values
                doneParsing = 0;
                sitesToCrawl = 0;
                ClientView.clearRows();
                System.out.println("Cleared rows");

                fetch = new Packet();
                System.out.println("Created fetch packet");
                fetch.type = Type.FETCH;
                // Request more links
                client.sendTCP(fetch);
                System.out.println("Sent fetch packet");
            } else {
                try {
                    this.sleep(3000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        System.out.println("Crawler has stopped");
    }
}