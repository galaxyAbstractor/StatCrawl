package net.pixomania.StatCrawl.db;

/**
 * This is so that there can only be one single database object all the time
 * @author galaxyAbstractor
 */
public class DbSingleton {
    private static Db db;
    
    private DbSingleton() {
        
    }
    
    /**
     * Returns the Database object
     * @return the database object
     */
    public static Db getDb(){
        if(db == null) db = new Db();
        return db;
    }
    
    /**
     * Reset the database if we change settings
     */
    public static void nullify(){
        db = null;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}
