package net.pixomania.StatCrawl.networking;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This stores all local queries in a local list, which is sent to the server when the current crawlbatch is down
 * This is to keep the network calls down to a minimum
 * @author galaxyAbstractor
 */
public class DbStore {
    private static Collection<QueueItem> queries = new ArrayList<QueueItem>();

    public static void add(QueueItem item){
        queries.add(item);
    }

    /**
     * Returns the list
     * @return the list of Collection<QueueItem>
     */
    public static Collection<QueueItem> getList(){
        return queries;
    }

    /**
     * Clear the list
     */
    public static void reset(){
        queries.clear();
    }
}
