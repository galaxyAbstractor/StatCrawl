package net.pixomania.StatCrawl.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.pixomania.StatCrawl.db.Db;
import net.pixomania.StatCrawl.db.DbQueue;
import net.pixomania.StatCrawl.db.DbSingleton;
import net.pixomania.StatCrawl.networking.Operation;
import net.pixomania.StatCrawl.networking.Packet;
import net.pixomania.StatCrawl.networking.QueueItem;
import net.pixomania.StatCrawl.networking.Type;
/**
 *
 * @author galaxyAbstractor
 */
public class DbServer {
    private Server server;
    private Db db = DbSingleton.getDb();
    private DbQueue dbQueue = new DbQueue();
    
    public void start(){
        try {
            dbQueue.resetQueue();
            dbQueue.start();
            server = new Server(30000,30000);
            
            int port = ServerView.getPort();
            if(port == -1) {
                System.out.println("  Illegal port");
            } else {
                Kryo kryo = server.getKryo();

                // Register the classes we are sending
                kryo.register(Packet.class);
                kryo.register(net.pixomania.StatCrawl.networking.Type.class);
                kryo.register(QueueItem.class);
                kryo.register(ArrayList.class);
                kryo.register(LinkedList.class);
                kryo.register(Operation.class);
                
                server.start();
                server.bind(ServerView.getPort());
                
                server.addListener(new Listener() {
                    @Override
                    public void received (Connection connection, Object object) {
                       if (object instanceof Packet) {
                          Packet request = (Packet)object;
                          Packet p;
                          // What should we respond to the clients?
                          switch(request.type){
                              case FETCH:
                                  // Client wants new links, send him some! Otherwise unhappy customer :'(
                                  p = new Packet();
                                  p.data = db.getFirstTenPending();
                                  p.type = Type.TOCRAWL;
                                  connection.sendTCP(p);
                                  break;
                              case QUERIES:
                                  System.out.println("Got Queries");
                                  dbQueue.addAll((Collection<QueueItem>) request.data);
                                  
                                  break;
                          }
                       }
                    }
                    @Override
                    public void connected (Connection connection) {

                    }
                });

                
            }
        } catch (IOException ex) {
            Logger.getLogger(DbServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void stop(){
        server.stop();
        dbQueue.stopQueue();
    }
}
