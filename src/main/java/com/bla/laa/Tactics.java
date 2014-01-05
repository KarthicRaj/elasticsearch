package com.bla.laa;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.common.joda.time.DateTime;
import org.elasticsearch.common.joda.time.format.DateTimeFormat;
import org.elasticsearch.common.joda.time.format.DateTimeFormatter;
import twitter4j.Status;
import twitter4j.TwitterException;

import java.io.IOException;
import java.util.List;

public class Tactics {
    static Logger logger = LogManager.getLogger(Tactics.class);

    Search search = new Search();
    Store store = new Store();
    Utils utils = new Utils();

    Boolean keepExecuting= true;

    public Tactics() {
        logger.debug("Tactics constructor, registering shutdown hook ");
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                logger.info("Shutdown Hook triggered, performing graceful shutdown !!!");
                keepExecuting = false;
         }
        });
    }

    //TODO looks like this is not working properly 
    protected void finalize( ) throws Throwable {
        logger.debug("Finalizing class, closing connection ");
        store.closeClient();
    }

    public void searchByDynamicKey() throws IOException, InterruptedException, TwitterException {
        logger.info("Will search by dynamic word");

        String queryStr = "maxima";
        while ( keepExecuting ){
            List<Status> tweets =  search.doSearch(search.createSimpeQuery(queryStr));
            queryStr = utils.getRandomWord(tweets);
            store.doStore(tweets);
            Thread.sleep(5000);
        }
        store.closeClient();
    }

    public void searchByGeo() throws TwitterException, IOException, InterruptedException {
        logger.info("Will search by geo, 300 km rad around riga");

        while ( keepExecuting ){
            List<Status> tweets =  search.doSearch(search.createGeoQuery());
            store.doStore(tweets);
            Thread.sleep(5000);
        }
    }

    public void searchByOnDayInPass() throws IOException, InterruptedException, TwitterException {
        logger.info("Will search by date in pass");

        String charArray[] = {
                "q", "w", "e", "ē", "r", "t", "y", "u", "ū", "i", "ī", "o", "p", "a", "ā", "s", "š", "d", "f",
                "g", "h", "j", "k", "ķ", "l", "ļ", "z", "ž", "x", "c", "č", "v", "b", "n", "ņ", "m"
        };
        DateTime date = new DateTime();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");

        while ( keepExecuting ){
            date = date.minusDays(1);
            for (String ch : charArray){
                List<Status> tweets =  search.doSearch(search.createSimpeQueryOnDate(ch, date));
                store.doStore(tweets);
                Thread.sleep(5000);
            }
        }

    }

}
