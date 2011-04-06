package net.pixomania.StatCrawl.db;

import java.util.LinkedList;
import java.util.Queue;

/**
 * As the name suggests, a database queue. Data and operations are added to the
 * Queue using the datatype QueueItem, which holds the data to insert and the
 * operation to perform.
 * @author galaxyAbstractor
 */
public class DbQueue extends Thread {
    private static Queue<QueueItem> pendingQueries = new LinkedList<QueueItem>();
    private Db db = DbSingleton.getDb();
    
    /**
     * Adds an operation to the queue.
     * @param item a QueueItem (data, operation) to add to the queue
     */
    public static void addQuery(QueueItem item){
        pendingQueries.add(item);
    }
    
    /**
     * Indicates to the DbQueue to stop when it's done with all operations.
     */
    public static void stopQueue(){
        run = false;
    }
    
    /**
     * Returns the run bool to the true state, so that the queue can be started
     * again
     */
    public void resetQueue(){
        run = true;
    }
    
    private static boolean run = true;
    
    @Override
    public void run(){
        while (true) {
            // if the queue has pending items, we check which operation they
            // should perform and perform it. Otherwise we sleep for 2 seconds.
            if(!pendingQueries.isEmpty()) {
                QueueItem item = pendingQueries.poll();
                System.out.println(pendingQueries.size() + " items left in queue. Operation performed: " + item.operation);
                
                switch(item.operation){
                    case CRAWLED:
                        db.insertCrawled(item.data);
                        break;
                    case HOST:
                        db.insertHost(item.data);
                        break;
                    case IP:
                        db.insertIP(item.data);
                        break;
                    case PENDING:
                        db.insertPending(item.data);
                        break;
                    case REMOVE:
                        db.removePending(item.data);
                        break;
                }
                 
             } else {
                if(run){
                    try {
                        this.sleep(2000);
                    } catch (InterruptedException ex) {
                        run = false;
                    }
                } else {
                    // Queue has been stopped. Break
                    System.out.println("DBQUEUE STOPPED! "+ pendingQueries.size() + " left in pending db queue");
                    break;
                }
             }
        }
    }   
}
