package net.pixomania.StatCrawl.networking;

import java.io.Serializable;

/**
 * The packet which is sent between the Server and the Client(s)
 * @author galaxyAbstractor
 */
public class Packet implements Serializable {
    public Object data;
    public Type type;

    public Packet(Object data, Type type){
        this.data = data;
        this.type = type;
    }
}
