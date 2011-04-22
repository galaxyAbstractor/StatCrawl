package net.pixomania.StatCrawl.db;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
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
        try {
           // Change this to your installation
           String userName = "root";
           String password = "";
           String url = "jdbc:mysql://localhost/StatCrawl";
           String url2 = "jdbc:mysql://localhost/country";
           
           // Create a new mySQL connection and connect to them
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
    public synchronized void insertPending(String url){
        // MD5 is used for unique identification of the URLs
        String md5 = Md5.getMD5Digest(url.getBytes());
        
        // We have to see if the URL already been crawled
        String query = "(SELECT md5 FROM crawled WHERE md5 = '"+md5+"') UNION (SELECT md5 FROM crawling WHERE md5 = '"+md5+"')";
           
        try {
            // Creates and executes the above given query
            Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery(query);
            
            // Go to the last row and try to get it
            rs.last();
            int rowcount = rs.getRow();
            
            if(rowcount == 0){
                // There was no rows matching the select, so we the URL hasn't been crawled yet
                Statement insertStatement = conn.createStatement();
                query = "INSERT IGNORE INTO pending (md5, url) VALUES ('"+ md5 +"','"+ url +"')";
                
                insertStatement.executeUpdate(query);
                
            } else {
                // URL already exists
                //System.out.println("Already crawled");
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(Db.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Inserts links into the crawling table and removes them from the pending table
     * @param url
     */
    public void insertCrawling(String url){
        try {
            // MD5 is used for unique identification of the URLs
            String md5 = Md5.getMD5Digest(url.getBytes());
            String query = "INSERT IGNORE INTO crawling (md5, url) VALUES ('" + md5 + "','" + url + "')";

            // Creates and executes the above given query
            Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            st.executeUpdate(query);

            System.out.println("inserted into crawling");
            // Now remove it from pending
            query = "DELETE FROM pending WHERE md5 = '"+md5+"'";

            Statement deleteStatement = conn.createStatement();
            deleteStatement.executeUpdate(query);

        } catch (SQLException ex) {
            System.out.println("Error");
            ex.printStackTrace();
        }
    }
    
    /**
     * Insert a URL to the crawled table
     * @param url the URL to insert
     */
    public synchronized void insertCrawled(String url){
        // Get the URLs unique id and insert it to the table
        String md5 = Md5.getMD5Digest(url.getBytes());
        String query = "INSERT IGNORE INTO crawled (md5, url) VALUES ('"+ md5 +"','"+ url +"')";
        
        try {        
            Statement insertStatement = conn.createStatement();
            insertStatement.executeUpdate(query);

            // Now remove it from crawling
            query = "DELETE FROM crawling WHERE md5 = '"+md5+"'";

            Statement deleteStatement = conn.createStatement();
            deleteStatement.executeUpdate(query);
        } catch (SQLException ex) {
            Logger.getLogger(Db.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Remove an URL from the pending list
     * @param url the URL to remove
     */
    public synchronized void removePending(String url){
        // The URL has ben crawled, so we find it's ID and remove it
        String md5 = Md5.getMD5Digest(url.getBytes());
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
    public LinkedList<String> getFirstTenPending() {
        LinkedList<String> list = new LinkedList<String>();
        try {
            String query = "SELECT url FROM pending";
            PreparedStatement statement = conn.prepareStatement(query); 
            
            // Fetch max 10 rows from the table
            statement.setMaxRows(10); 
            ResultSet rs = statement.executeQuery();
            // Add them to the list we'll return
            while(rs.next()){
                insertCrawling(rs.getString("url"));
                list.add(rs.getString("url"));
            }

            return list;
        } catch (SQLException ex) {
            return null;
        }
        
    }
    
    /**
     * Inserts an IP to the ip table
     * @param ip the IP to insert
     */
    public void insertIP(String host){
        // Check to see if the IP of this host is already in the table
        String query = "SELECT host FROM hosts WHERE host = '"+host+"'";
        
        try {
            
            Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery(query);
            
            rs.last();
            int rowcount = rs.getRow();
            
            if(rowcount == 0){
                // The host was not found, see if the IP already exists
                String ip = InetAddress.getByName(host).getHostAddress();
                query = "SELECT ip FROM ip WHERE ip = '"+ip+"'";

                
                st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rs = st.executeQuery(query);

                rs.last();
                rowcount = rs.getRow();

                if(rowcount == 0){
                    // Insert IP
                    Statement insertStatement = conn.createStatement();
                    query = "INSERT INTO ip (ip, count) VALUES ('"+ ip +"',1)";
                    insertLocation(ip);
                    insertStatement.executeUpdate(query);

                } else {
                    // Update IP
                    Statement updateStatement = conn.createStatement();
                    query = "UPDATE ip SET count = count+1 WHERE ip = '"+ip+"'";

                    updateStatement.executeUpdate(query);
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
        // Check if the host already exists
        String query = "SELECT host FROM hosts WHERE host = '"+host+"'";
        
        try {
            Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery(query);
            
            rs.last();
            int rowcount = rs.getRow();
            
            if(rowcount == 0){
                // Insert host
                Statement insertStatement = conn.createStatement();
                query = "INSERT INTO hosts (host, count) VALUES ('"+ host +"',1)";
                
                insertStatement.executeUpdate(query);
                
            } else {
                // Update Host
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
        
        // Query to get the country
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
                
                // Check if the country already has a row
                query = "SELECT name FROM countries WHERE name = '"+rs.getString("country_name") +"'";
                st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet countries = st.executeQuery(query);

                countries.last();
                rowcount = countries.getRow();
                
                if(rowcount == 0){
                    // It hasn't, create the row
                    Statement insertStatement = conn.createStatement();
                    query = "INSERT INTO countries (short, name, count) VALUES ('"+ rs.getString("country_code") +"','"+
                            rs.getString("country_name")+"',1)";
                
                    insertStatement.executeUpdate(query);
                } else {
                    // It has, update the row
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
                
                // We have to see if the region already exists
                query = "SELECT code FROM regions WHERE code = '"+region +"' AND short = '"+ rs.getString("country_code")+"'";
                st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet regions = st.executeQuery(query);

                regions.last();
                rowcount = regions.getRow();
                
                if(rowcount == 0){
                    // It doesn't, insert it
                    Statement insertStatement = conn.createStatement();
                    query = "INSERT INTO regions (short, code, name, count) VALUES ('"+ rs.getString("country_code") +"','"+
                            region+"', '"+regionName+"',1)";
                
                    insertStatement.executeUpdate(query);
                } else {
                    // It does, update it
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
                
                // Check if the city already exists
                query = "SELECT name FROM cities WHERE name = '"+city +"' AND short = '"+region+
                        "' AND countrycode = '"+rs.getString("country_code")+"'";
                st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet cities = st.executeQuery(query);

                cities.last();
                rowcount = cities.getRow();
                
                if(rowcount == 0){
                    // It doesn't, insert it
                    Statement insertStatement = conn.createStatement();
                    query = "INSERT INTO cities (countrycode, short, name, count) VALUES ('"+
                            rs.getString("country_code")+"','"+ region +"','"+city+"',1)";
                
                    insertStatement.executeUpdate(query);
                } else {
                    // It does, update it
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
     * Inserts the image stat
     * @param stat a string with the stats divided by spaces
     */
    public void insertImage(String stat){
        Scanner sc = new Scanner(stat);
        
        String filesize = sc.next();
        String extension = sc.next();
        sc.close();
        
        
        String query = "SELECT extension FROM images WHERE extension = '"+extension+"'";
           
        try {
            // Creates and executes the above given query
            Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery(query);
            
            // Go to the last row and try to get it
            rs.last();
            int rowcount = rs.getRow();
            
            if(rowcount == 0){
                // There was no rows matching the select, so we the URL hasn't been crawled yet
                Statement insertStatement = conn.createStatement();
                query = "INSERT IGNORE INTO images (count, size, extension) VALUES (1,'"+ filesize +"','"+extension+"')";
                
                insertStatement.executeUpdate(query);
                
            } else {
                // It does, update it
                Statement updateStatement = conn.createStatement();
                query = "UPDATE images SET count = count+1, size = size+"+filesize+" WHERE extension = '"+extension+"'";

                updateStatement.executeUpdate(query);
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(Db.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Inserts the HTTP error code into the errors table
     * @param error data separated by spaces (host ip code)
     */
    public void insertError(String error){
        Scanner sc = new Scanner(error);

        String host = sc.next();
        String ip = sc.next();
        String code = sc.next();
        sc.close();

        String query = "SELECT code FROM errors WHERE code = '"+code+"'";

        try {
            // Creates and executes the above given query
            Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery(query);

            // Go to the last row and try to get it
            rs.last();
            int rowcount = rs.getRow();

            if(rowcount == 0){
                // There was no rows matching the select, so we the URL hasn't been crawled yet
                Statement insertStatement = conn.createStatement();
                query = "INSERT IGNORE INTO errors (count, code) VALUES (1,'"+ code +"')";

                insertStatement.executeUpdate(query);

            } else {
                // It does, update it
                Statement updateStatement = conn.createStatement();
                query = "UPDATE errors SET count = count+1 WHERE code = '"+code+"'";

                updateStatement.executeUpdate(query);
            }


        } catch (SQLException ex) {
            Logger.getLogger(Db.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    /**
     * Creates the structure with the data for the TreeTable stat view.
     * @return An ArrayList filled with Location objects
     */
    public ArrayList<Location> getCountryStats() {
        ArrayList<Location> countryList = new ArrayList<Location>();
        
        // Select everything in the countries table
        String countryQuery = "SELECT * FROM countries";
        ResultSet countries;
        ResultSet regions;
        ResultSet cities;
        try {
            Statement cst = conn.createStatement();
            countries = cst.executeQuery(countryQuery);
            
            // While there are countries
            while(countries.next()){
                // Get all of this countrys regions
                String regionQuery = "SELECT * FROM regions WHERE short = '"+countries.getString("short")+"'";
                
                Statement rst = conn.createStatement();
                regions = rst.executeQuery(regionQuery);
                
                // Create a country and add its info
                Country c = new Country(countries.getString("name"), 
                                        countries.getString("short"),
                                        countries.getInt("count"));
                // While there are regions under this country
                while(regions.next()){
                    // Get all the cities in this region
                    String cityQuery = "SELECT * FROM cities WHERE short = '"+regions.getString("code")+
                            "' AND countrycode = '"+countries.getString("short")+"'";
                    
                    Statement cist = conn.createStatement();
                    cities = cist.executeQuery(cityQuery);
                    
                    // Create a new region and add its data
                    Region r = new Region(regions.getString("name"), 
                                          regions.getString("code"), 
                                          regions.getInt("count"));
                    // While there are cities in this region
                    while(cities.next()){
                        // Create a city and add its data
                        City city = new City(cities.getString("name"),
                                             cities.getInt("count"));
                        // Add the city to the region
                        r.cities.add(city);
                    }
                    // Add the region to the country
                    c.regions.add(r);
                }
                // Add the countries to the countrylist
                countryList.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Db.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return countryList;
    }
}
