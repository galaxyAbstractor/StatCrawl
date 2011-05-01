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
        // Create a new client if it hasn't been created yet
        if(client == null) client = new Client(60000,60000);
        return client;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}
