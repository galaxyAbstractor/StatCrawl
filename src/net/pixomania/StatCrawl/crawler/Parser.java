/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.pixomania.StatCrawl.crawler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import net.pixomania.StatCrawl.db.Db;
import net.pixomania.StatCrawl.db.DbSingleton;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author galaxyAbstractor
 */
public class Parser extends SwingWorker<Void, Void>{

    private Elements links;
    private String url;
    private JProgressBar progress;
    private double linkCount;
    private double currentLoop;
    private int rowIndex;
    
    public Parser(Elements links, String url){
        this.links = links;
        this.url = url;
    }
    
    @Override
    protected Void doInBackground() throws Exception {
         java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Main.model.addRow(new Object[]{url, new JProgressBar()});
            }
        });
        
        rowIndex = 0;
        for(int i = 0;i<Main.model.getRowCount();i++){
            if(Main.model.getValueAt(i, 0).equals(url)){
                rowIndex = i;
                break;
            }
        }
        
        progress = (JProgressBar) Main.model.getValueAt(rowIndex, 1);
        
        Db db = DbSingleton.getDb();
        ArrayList<URL> pending = new ArrayList<URL>();
        
        linkCount = links.size()*2;
        currentLoop = 0;
        
        for (Element link : links) {
                
            String linkURL = link.attr("abs:href");

            // Make sure we have an URL, otherwise we are stuck here forever
            if(linkURL.isEmpty()) continue;
            if(!linkURL.startsWith("http")) continue;

            URL url1;
            try {
                url1 = new URL(linkURL);
            } catch(MalformedURLException e){
                System.out.println("Malformed URL");
                continue;
            }
            
            
            // Add the URL to the pending list
            pending.add(url1); 

            // Update the status
            // Main.updateStatus();
            // Main.linksTotal++;
            
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    progress.setValue((int) ((currentLoop++/linkCount)*100));

                    //Main.model.setValueAt(progress, rowIndex, 1);
                }
            });
           
            
        }

        for(URL pUrl : pending){
           db.insertPending(pUrl.toString());
           
           java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    progress.setValue((int) ((currentLoop++/linkCount)*100));

                    //Main.model.setValueAt(progress, rowIndex, 1);
                }
            });
        }

        pending.clear();

        // This URL is crawled and does not need to be crawled again
        db.insertCrawled(url); 

        db.insertIP(new URL(url).getHost());

        db.insertHost(new URL(url).getHost());

        Main.model.removeRow(rowIndex);
        System.out.println("Removed parser");
        return null;
    }
}
