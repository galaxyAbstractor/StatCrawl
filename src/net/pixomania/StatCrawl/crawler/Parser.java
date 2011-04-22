package net.pixomania.StatCrawl.crawler;

import java.net.InetAddress;
import java.net.URL;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import net.pixomania.StatCrawl.client.ClientView;
import net.pixomania.StatCrawl.crawler.stat.LinksStat;
import net.pixomania.StatCrawl.networking.DbStore;
import net.pixomania.StatCrawl.networking.Operation;
import net.pixomania.StatCrawl.networking.QueueItem;
import net.pixomania.statcrawl.crawler.stat.ImageStat;
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
    private DefaultTableModel model = ClientView.getModel();
    private Crawler crawler = CrawlerSingleton.getCrawler();
    
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
            @Override
            public void run() {
                model.addRow(new Object[]{url, new JProgressBar()});
            }
        });
        
        // This URL is crawled and does not need to be crawled again
        QueueItem qiCrawled = new QueueItem();
        qiCrawled.data = url;
        qiCrawled.operation = Operation.CRAWLED;
        DbStore.add(qiCrawled);
        
        // Creates the parser that parses the links
        Stat linkparser = new LinksStat(html);
        linkparser.parse();
        
        Stat imageparser = new ImageStat(html);
        imageparser.parse();
        
        // Host and IP stats
        String host = new URL(url).getHost();
        String ip = InetAddress.getByName(host).getHostAddress();
        QueueItem qiIP =  new QueueItem();
        qiIP.data = ip;
        qiIP.operation = Operation.IP;
        DbStore.add(qiIP);

        QueueItem qiHost = new QueueItem();
        qiHost.data = host;
        qiHost.operation = Operation.HOST;
        DbStore.add(qiHost);
        
        // Tell the crawler that we are done parsing
        crawler.doneParsing();
        System.out.println("Removed parser - "+ crawler.getDoneParsing() +" of "+ crawler.getSitesToCrawl());

        // Get its rowindex so we can alter it later on. NEEDS TO BE CHANGED!
        rowIndex = 0;
        for(int i = 0;i<model.getRowCount();i++){
            if(model.getValueAt(i, 0).equals(url)){
                rowIndex = i;
                break;
            }
        }

        progress = (JProgressBar) model.getValueAt(rowIndex, 1);
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                progress.setValue(100);
            }
        });
        return null;
    }
}
