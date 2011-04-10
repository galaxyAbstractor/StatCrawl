package net.pixomania.StatCrawl.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.pixomania.StatCrawl.db.DbQueue;
import net.pixomania.StatCrawl.db.QueueItem;
/**
 *
 * @author galaxyAbstractor
 */
public class DbServer {
    private Server server;
    
    public void start(){
        try {
            server = new Server();
            server.start();
            server.bind(1337, 1337);
            
            server.addListener(new Listener() {
                @Override
                public void received (Connection connection, Object object) {
                   if (object instanceof QueueItem) {
                      QueueItem request = (QueueItem)object;
                      DbQueue.addQuery(request);   
                   }
                }
                @Override
                public void connected (Connection connection) {
                    
                }
            });
            
            Kryo kryo = server.getKryo();
            
            kryo.register(QueueItem.class);
        } catch (IOException ex) {
            Logger.getLogger(DbServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void stop(){
        server.stop();
    }
}
