package net.pixomania.StatCrawl.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.pixomania.StatCrawl.db.Db;
import net.pixomania.StatCrawl.db.DbSingleton;
import net.pixomania.StatCrawl.networking.Packet;
import net.pixomania.StatCrawl.networking.Type;
/**
 *
 * @author galaxyAbstractor
 */
public class DbServer {
    private Server server;
    private Db db = DbSingleton.getDb();
    
    public void start(){
        try {
            server = new Server();
            
            int port = ServerView.getPort();
            if(port == -1) {
                System.out.println("  Illegal port");
            } else {
                Kryo kryo = server.getKryo();

                // Register the classes we are sending
                kryo.register(Packet.class);
                
                server.start();
                server.bind(ServerView.getPort(), ServerView.getPort());

                server.addListener(new Listener() {
                    @Override
                    public void received (Connection connection, Object object) {
                       if (object instanceof Packet) {
                          Packet request = (Packet)object;

                          // What should we respond to the clients?
                          switch(request.type){
                              case FETCH:
                                  // Client wants new links, send him some! Otherwise unhappy customer :'(
                                  Packet p = new Packet(db.getFirstTenPending(),Type.TOCRAWL);
                                  connection.sendTCP(p);
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
    }
}
