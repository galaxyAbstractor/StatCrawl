/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.pixomania.StatCrawl.db;

import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author galaxyAbstractor
 */
public class DbTest {
    private Db db = DbSingleton.getDb();
    public DbTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    @Test
    public void getIPTotalCountTest(){
        Assert.assertNotNull(db.getIPTotalCount());
    }
    
    @Test
    public void getImageTotalCountTest(){
        Assert.assertNotNull(db.getImageTotalCount());
    }
    
    @Test
    public void getCountryStatTest(){
        Assert.assertNotNull(db.getCountryStats());
    }
    
    @Test
    public void getCrawlerStatTest(){
        Assert.assertNotNull(db.getCrawlerStats());
    }
    
    @Test
    public void getHostTotalCountTest(){
        Assert.assertNotNull(db.getHostTotalCount());
    }
    
    @Test
    public void getTopErrorsTest(){
        Assert.assertNotNull(db.getTopErrors());
    }
    
    @Test
    public void getTopHostsTest(){
        Assert.assertNotNull(db.getTopHosts());
    }
    
    @Test
    public void getTopIPTest(){
        Assert.assertNotNull(db.getTopIPs());
    }
    
    @Test
    public void getTopImageTypesTest(){
        Assert.assertNotNull(db.getTopImageTypes());
    }
    
    @Test
    public void getErrorTotalCountTest(){
        Assert.assertNotNull(db.getErrorTotalCount());
    }
    
    @Test
    public void isConnectedTest(){
        Assert.assertTrue(db.isConnected());
    }
}
