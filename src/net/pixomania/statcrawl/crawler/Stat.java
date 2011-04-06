package net.pixomania.StatCrawl.crawler;

import org.jsoup.nodes.Document;

/**
 * The abstract for each stat to use
 * @author galaxyAbstractor
 */
public abstract class Stat extends Thread {

    protected Document html;
    
    public Stat(Document html) {
        this.html = html;
    }
    
    @Override
    public void run() {
        parse();
    }
    
    public abstract void parse();
    public abstract void save();
    
}
