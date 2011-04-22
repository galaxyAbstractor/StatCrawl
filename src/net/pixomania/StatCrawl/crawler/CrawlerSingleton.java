package net.pixomania.StatCrawl.crawler;

/**
 *
 * @author galaxyAbstractor
 */
public class CrawlerSingleton {
    private static Crawler crawler;

    private CrawlerSingleton() {

    }

    /**
     * Returns the Crawler object
     * @return the crawler object
     */
    public static synchronized Crawler getCrawler(){
        if(crawler == null) crawler = new Crawler();
        return crawler;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}
