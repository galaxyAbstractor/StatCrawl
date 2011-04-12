package net.pixomania.StatCrawl.networking;

/**
 *
 * @author galaxyAbstractor
 */
public class Packet {
    public Object data;
    public Type type;

    public Packet(Object data, Type type){
        this.data = data;
        this.type = type;
    }
}
