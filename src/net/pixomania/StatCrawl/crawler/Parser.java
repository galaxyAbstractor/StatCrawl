package net.pixomania.StatCrawl.crawler;

import java.net.InetAddress;
import java.net.URL;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import net.pixomania.StatCrawl.crawler.stat.LinksStat;
import net.pixomania.StatCrawl.db.DbQueue;
import net.pixomania.StatCrawl.db.Operation;
import net.pixomania.StatCrawl.db.QueueItem;
import org.jsoup.nodes.Document;

/**
 *
 * @author galaxyAbstractor
 */
public class Parser extends SwingWorker<Void, Void>{

    private Document html;
    private String url;
    private JProgressBar progress;
    private int rowIndex;
    private DefaultTableModel model = CrawlView.getModel();
    
    /**
     * Creates a new parser
     * @param html the HTML of the URL
     * @param url the URL
     */
    public Parser(Document html, String url){
        this.html = html;
        this.url = url;
    }
    
    @Override
    protected Void doInBackground() throws Exception {
        // Add the parser to the table so we can monitor its status 
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                model.addRow(new Object[]{url, new JProgressBar()});
            }
        });
        
        // Get its rowindex so we can alter it later on. NEEDS TO BE CHANGED!
        rowIndex = 0;
        for(int i = 0;i<model.getRowCount();i++){
            if(model.getValueAt(i, 0).equals(url)){
                rowIndex = i;
                break;
            }
        }
        
        progress = (JProgressBar) model.getValueAt(rowIndex, 1);
        
       

        // This URL is crawled and does not need to be crawled again
        DbQueue.addQuery(new QueueItem(url, Operation.CRAWLED));
        
        // Creates the parser that parses the links
        Stat linkparser = new LinksStat(html);
        linkparser.start();
        
        // Host and IP stats
        String host = new URL(url).getHost();
        String ip = InetAddress.getByName(host).getHostAddress();
        DbQueue.addQuery(new QueueItem(ip, Operation.IP));
        
        DbQueue.addQuery(new QueueItem(host, Operation.HOST));
        
        // Remove the row. NEEDS TO BE CHANGED!
        model.removeRow(rowIndex);
        System.out.println("Removed parser");
        return null;
    }
}
