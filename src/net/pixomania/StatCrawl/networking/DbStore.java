/*******************************************************************
* StatCrawl
*
* Copyright (c) 2011, http://pixomania.net
*
* Licensed under the BSD License
* Redistributions of files must retain the above copyright notice.
*
* Please see LICENSE.txt for more info.
*
* @copyright Copyright 2011, pixomania, http://pixomania.net
* @license BSD license (http://www.opensource.org/licenses/bsd-license.php)
********************************************************************/
package net.pixomania.StatCrawl.networking;

import com.esotericsoftware.kryonet.Client;
import java.util.ArrayList;
import net.pixomania.StatCrawl.client.ClientSingleton;

/**
 * This stores all local queries in a local list, which is sent to the server when the current crawlbatch is down
 * This is to keep the network calls down to a minimum
 * @author galaxyAbstractor
 */
public class DbStore {
    private static ArrayList<QueueItem> queries = new ArrayList<QueueItem>();
    private static Client client = ClientSingleton.getClient();

    public static void add(QueueItem item){
        queries.add(item);
    }

    /**
     * Returns the list
     * @return the list of Collection<QueueItem>
     */
    public static ArrayList<QueueItem> getList(){
        return queries;
    }


    /**
     * Clear the list
     */
    public static void reset(){
        queries.clear();
    }

    

}
