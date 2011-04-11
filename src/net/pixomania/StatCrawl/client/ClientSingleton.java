package net.pixomania.StatCrawl.client;

import com.esotericsoftware.kryonet.Client;

/**
 *
 * @author galaxyAbstractor
 */
public class ClientSingleton {
    private static Client client;

    private ClientSingleton() {

    }

    /**
     * Returns the Client object
     * @return the Client object
     */
    public static Client getClient(){
        if(client == null) client = new Client();
        return client;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}
