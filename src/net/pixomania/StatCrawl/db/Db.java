package net.pixomania.StatCrawl.db;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.sql.Statement;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.pixomania.StatCrawl.stats.City;
import net.pixomania.StatCrawl.stats.Region;
import net.pixomania.StatCrawl.stats.Country;
import net.pixomania.StatCrawl.stats.Location;

/**
 *
 * @author galaxyAbstractor
 */
public class Db {

    private Connection conn = null;
    private Connection countryConn = null;
    
    public Db(){
        // Establish a connection to the database
        try{
           String userName = "root";
           String password = "";
           String url = "jdbc:mysql://localhost/StatCrawl";
           String url2 = "jdbc:mysql://localhost/country";
           
           Class.forName ("com.mysql.jdbc.Driver").newInstance();
           conn = DriverManager.getConnection (url, userName, password);
           countryConn = DriverManager.getConnection (url2, userName, password);
           
           System.out.println ("Database connection established");
        } catch (Exception e)  {
           System.err.println ("Cannot connect to database server");
        }
    }
    
    /**
     * Add an URL to the pending list, as long as it doesn't already exist as crawled
     * @param url the URL to add
     */
    public void insertPending(String url){
        String md5 = getMD5Digest(url.getBytes());
        String query = "SELECT * FROM crawled WHERE md5 = '"+md5+"'";
        
        try {
            Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery(query);
            
            rs.last();
            int rowcount = rs.getRow();
            
            if(rowcount == 0){
                Statement insertStatement = conn.createStatement();
                query = "INSERT IGNORE INTO pending (md5, url) VALUES ('"+ md5 +"','"+ url +"')";
                
                insertStatement.executeUpdate(query);
                
            } else {
                //System.out.println("Already crawled");
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(Db.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Insert a URL to the crawled table
     * @param url the URL to insert
     */
    public void insertCrawled(String url){
        
        String md5 = getMD5Digest(url.getBytes());
        String query = "INSERT INTO crawled (md5, url) VALUES ('"+ md5 +"','"+ url +"')";
        
        try {        
            Statement insertStatement = conn.createStatement();
            insertStatement.executeUpdate(query); 
        } catch (SQLException ex) {
            Logger.getLogger(Db.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Remove an URL from the pending list
     * @param url the URL to remove
     */
    public void removePending(String url){
        String md5 = getMD5Digest(url.getBytes());
        String query = "DELETE FROM pending WHERE md5 = '"+md5+"'";
        
        try {        
            Statement deleteStatement = conn.createStatement();
            deleteStatement.executeUpdate(query); 
        } catch (SQLException ex) {
            Logger.getLogger(Db.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    /**
     * Get the first item on the list
     * @return the URL as a String
     */
    public String getFirstPending() {

        try {
            String query = "SELECT url FROM pending";
            PreparedStatement statement = conn.prepareStatement(query); 
            statement.setMaxRows(1); 
            ResultSet rs = statement.executeQuery();
            rs.next();
            return rs.getString("url");
        } catch (SQLException ex) {
            Logger.getLogger(Db.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * Inserts an IP to the ip table
     * @param ip the IP to insert
     */
    public void insertIP(String host){
        String query = "SELECT * FROM hosts WHERE host = '"+host+"'";
        
        try {
            
            Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery(query);
            
            rs.last();
            int rowcount = rs.getRow();
            
            if(rowcount == 0){
                String ip = InetAddress.getByName(host).getHostAddress();
                query = "SELECT * FROM ip WHERE ip = '"+ip+"'";

                try {
                    st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    rs = st.executeQuery(query);

                    rs.last();
                    rowcount = rs.getRow();

                    if(rowcount == 0){
                        Statement insertStatement = conn.createStatement();
                        query = "INSERT INTO ip (ip, count) VALUES ('"+ ip +"',1)";
                        insertLocation(ip);
                        insertStatement.executeUpdate(query);

                    } else {
                        Statement updateStatement = conn.createStatement();
                        query = "UPDATE ip SET count = count+1 WHERE ip = '"+ip+"'";

                        updateStatement.executeUpdate(query);
                    }


                } catch (SQLException ex) {
                    Logger.getLogger(Db.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(Db.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Db.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Inserts a host to the hosts table
     * @param host the host to insert
     */
    public void insertHost(String host){
        
        String query = "SELECT * FROM hosts WHERE host = '"+host+"'";
        
        try {
            Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery(query);
            
            rs.last();
            int rowcount = rs.getRow();
            
            if(rowcount == 0){
                Statement insertStatement = conn.createStatement();
                query = "INSERT INTO hosts (host, count) VALUES ('"+ host +"',1)";
                
                insertStatement.executeUpdate(query);
                
            } else {
                Statement updateStatement = conn.createStatement();
                query = "UPDATE hosts SET count = count+1 WHERE host = '"+host+"'";
                
                updateStatement.executeUpdate(query);
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(Db.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Inserts geographical info about one IP to the database
     * @param ip the IP to lookup and insert
     */
    public void insertLocation(String ip){
        // Get the index of the dots
        int first = ip.indexOf(".", 0);
        int second = ip.indexOf(".", first+1);
        int third = ip.indexOf(".", second+1);
        
        // Get all numbers
        long A = Long.parseLong(ip.substring(0, first));
        long B = Long.parseLong(ip.substring(first+1, second));
        long C = Long.parseLong(ip.substring(second+1, third));
        long D = Long.parseLong(ip.substring(third+1));
            
        // Calculate the IP start for the database search
        long ipStart = ((A*256+B)*256+C)*256 + D;
        
        String query = "SELECT * FROM `ip_group_city` where `ip_start` <= "+ipStart+" order by ip_start desc limit 1;";
        
        try {
            Statement st = countryConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery(query);
            
            rs.last();
            int rowcount = rs.getRow();
            rs.first();
            
            if(rowcount == 1){
              /* ####################
                 # Countries
                 #################### */
                query = "SELECT * FROM countries WHERE name = '"+rs.getString("country_name") +"'";
                st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet countries = st.executeQuery(query);

                countries.last();
                rowcount = countries.getRow();
                
                if(rowcount == 0){
                    Statement insertStatement = conn.createStatement();
                    query = "INSERT INTO countries (short, name, count) VALUES ('"+ rs.getString("country_code") +"','"+
                            rs.getString("country_name")+"',1)";
                
                    insertStatement.executeUpdate(query);
                } else {
                    Statement updateStatement = conn.createStatement();
                    query = "UPDATE countries SET count = count+1 WHERE name = '"+rs.getString("country_name")+"'";
                
                    updateStatement.executeUpdate(query);
                }
                
                /* ####################
                 # Regions
                 #################### */
                
                // The database doesn't include all regions over the world, so we have to set some to unknown
                String region = rs.getString("region_code");
                String regionName = rs.getString("region_name");
                if(region.isEmpty()) region = "0";
                if(regionName.isEmpty()) regionName = "unknown";
                
                query = "SELECT * FROM regions WHERE code = '"+region +"' AND short = '"+ rs.getString("country_code")+"'";
                st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet regions = st.executeQuery(query);

                regions.last();
                rowcount = regions.getRow();
                
                if(rowcount == 0){
                    Statement insertStatement = conn.createStatement();
                    query = "INSERT INTO regions (short, code, name, count) VALUES ('"+ rs.getString("country_code") +"','"+
                            region+"', '"+regionName+"',1)";
                
                    insertStatement.executeUpdate(query);
                } else {
                    Statement updateStatement = conn.createStatement();
                    query = "UPDATE regions SET count = count+1 WHERE code = '"+region+"' AND short = '"+ rs.getString("country_code")+"'";
                
                    updateStatement.executeUpdate(query);
                }
                
                /* ####################
                 # Cities
                 #################### */
                
                // The database doesn't include all cities over the world, so we have to set some to unknown
                String city = rs.getString("city");
                if(city.isEmpty()) city = "unknown";
                
                query = "SELECT * FROM cities WHERE name = '"+city +"' AND short = '"+region+
                        "' AND countrycode = '"+rs.getString("country_code")+"'";
                st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet cities = st.executeQuery(query);

                cities.last();
                rowcount = cities.getRow();
                
                if(rowcount == 0){
                    Statement insertStatement = conn.createStatement();
                    query = "INSERT INTO cities (countrycode, short, name, count) VALUES ('"+
                            rs.getString("country_code")+"','"+ region +"','"+city+"',1)";
                
                    insertStatement.executeUpdate(query);
                } else {
                    Statement updateStatement = conn.createStatement();
                    query = "UPDATE cities SET count = count+1 WHERE name = '"+city+"' AND short = '"+region+"'";
                
                    updateStatement.executeUpdate(query);
                }

            } else {
                System.out.println("Wrong ammount locations");
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(Db.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Generate an md5 hash
     * @param buffer Byte-array of a String
     * @return an md5 hash as a String
     */
    private String getMD5Digest(byte[] buffer) {
        String resultHash = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");

            byte[] result = new byte[md5.getDigestLength()];
            md5.reset();
            md5.update(buffer);
            result = md5.digest();

            StringBuilder buf = new StringBuilder(result.length * 2);

            for (int i = 0; i < result.length; i++) {
                int intVal = result[i] & 0xff;
                if (intVal < 0x10) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(intVal));
            }

            resultHash = buf.toString();
        } catch (NoSuchAlgorithmException e) {
        }
        return resultHash;
    }

    public ArrayList<Location> getCountryStats() {
        ArrayList<Location> countryList = new ArrayList<Location>();
        
        String countryQuery = "SELECT * FROM countries";
        ResultSet countries;
        ResultSet regions;
        ResultSet cities;
        try {
            Statement cst = conn.createStatement();
            countries = cst.executeQuery(countryQuery);
            
            while(countries.next()){
                String regionQuery = "SELECT * FROM regions WHERE short = '"+countries.getString("short")+"'";
                
                Statement rst = conn.createStatement();
                regions = rst.executeQuery(regionQuery);
                
                Country c = new Country(countries.getString("name"), 
                                        countries.getString("short"),
                                        countries.getInt("count"));
                
                while(regions.next()){
                    String cityQuery = "SELECT * FROM cities WHERE short = '"+regions.getString("code")+
                            "' AND countrycode = '"+countries.getString("short")+"'";
                    
                    Statement cist = conn.createStatement();
                    cities = cist.executeQuery(cityQuery);
                    
                    Region r = new Region(regions.getString("name"), 
                                          regions.getString("code"), 
                                          regions.getInt("count"));
                    while(cities.next()){
                        City city = new City(cities.getString("name"),
                                             cities.getInt("count"));
                        r.cities.add(city);
                    }
                    c.regions.add(r);
                }
                countryList.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Db.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return countryList;
    }
}
