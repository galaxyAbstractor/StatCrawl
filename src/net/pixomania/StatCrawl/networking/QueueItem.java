package net.pixomania.StatCrawl.networking;

/**
 *
 * @author galaxyAbstractor
 */
public class QueueItem {
    public String data;
    public Operation operation;
    
    public QueueItem(String data, Operation operation){
        this.data = data;
        this.operation = operation;
    }
   
}
