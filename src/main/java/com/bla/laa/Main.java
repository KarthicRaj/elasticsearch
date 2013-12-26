package com.bla.laa;

import org.apache.logging.log4j.LogManager;
import twitter4j.*;

import java.io.IOException;
import java.util.List;
import org.apache.logging.log4j.Logger;


public class Main {
    static Logger logger = LogManager.getLogger(Main.class);

    public static void main(String args[]) throws TwitterException, InterruptedException, IOException {
        logger.info("Starting lv-twitter crawler ... ");

        Search search = new Search();
        Store store = new Store();
        Utils utils = new Utils();

        String queryStr = "maxima";
        while (true){
            List<Status> tweets =  search.doSearch(queryStr);
            queryStr = utils.getRandomWord(tweets);
            store.doStore(tweets);
            Thread.sleep(5000);
        }

    }
}