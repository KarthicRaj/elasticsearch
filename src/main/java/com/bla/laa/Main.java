package com.bla.laa;

import twitter4j.*;
import twitter4j.json.DataObjectFactory;

import java.util.List;

public final class Main {
    public static void main(String args[]) throws TwitterException {


        String queryStr = "maxima";

        while (true){
            List<Status> tweets =  new Search().doSearch(queryStr);
            queryStr = new Utils().getRandomWord(tweets);
            new Store().doStore(tweets);
        }


        /*
        TwitterSearchDaemon daemon = new TwitterSearchDaemon();
        daemon.addListener(new StatusAdapter() {
            @Override
            public void onStatus(Status status) {
                System.out.println(status.getId());
                System.out.println(DataObjectFactory.getRawJSON(status));
                new Store().doStore(status);
            }
        });
        daemon.addQuery("latvija");
        daemon.addQuery(56.968936, 24.105163, 20);
        daemon.start();
        */
    }
}