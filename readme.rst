Makes stats about the Internet. School project.

Country database from http://ipinfodb.com/ip_database.php is needed (Complete (city) single table)

Changelog
=======
* Apr 11, 2011: Ability to chose ports and host. Some initial start of a way to send requests and responses.
* Apr 10, 2011: Made a table for links currently in crawl-state.
* Apr 09, 2011: Continued a bit with the host/node environment. Can now connect clients to server.
* Apr 08, 2011: Started implementation of a Host/node environment. A server that should handle database, and clients that crawls
* Apr 07, 2011: Image stats added. Changed the Stat interface to normal object instead of Thread. Random nullpointer in "Thread-2" occuring...
* Apr 06, 2011: Major revamp. A sort of "plugable" parse system implemented, but not fully done. A database queue system was implemented. This slows the crawling down a bit, but it keeps the stat more accurate. Some commenting and documentation.
* Mar 18, 2011: Changed the way of how links to crawl are fetched, they are now fetched in batches of 10. Added a way to open the statview frame.
* Feb 12, 2011: Parser is in own thread. Table for progress of every parser thread running.
* Jan 30, 2011: Re-organisation of files and packages. Stat frame started. JXTreeTable added for Country stats (wow this one was a hard one to figure out lol). Country stats improved.
* Jan 22, 2011: Added country, city and region stats for the IPs. 
* Jan 21, 2011: Added host stats. IP get's only counted once per hostname. Gets stuck if website is down, need to implement some sort of timeout continue stuff
* Jan 20, 2011: Should only crawl sites that is text/html, we don't want to crawl images as jsoup apparently gets stuck there. If an URL contains # we still get stuck though :s. Basic IP stats added.
* Jan 19, 2011: Further mySQL transition
* Jan 18, 2011: Started transition from vars to mySQL to store pending and crawled URLs
* Jan 14, 2011: Start. It get's stuck at twitter though... Have to fix that