/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.pixomania.StatCrawl.db;

/**
 *
 * @author galaxyAbstractor
 */
public class DbSingleton {
    private static Db db;
    
    private DbSingleton() {
        
    }
    
    public static synchronized Db getDb(){
        if(db == null) db = new Db();
        return db;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}
