package com.bla.laa;

import twitter4j.*;

import java.util.List;

public final class Main {
    public static void main(String args[]) throws TwitterException {
        //Search search = new Search();
        //List<Status> tweets =  search.doSearch();

        Store store = new Store();
        //store.doStore(tweets);

        TwitterSearchDaemon daemon = new TwitterSearchDaemon();
        daemon.addListener(new StatusAdapter() {
            @Override
            public void onStatus(Status status) {
                System.out.println(status.getId());
                new Store().doStore(status);
            }
        });
        daemon.addQuery("latvija");
        daemon.addQuery(56.968936, 24.105163, 20);
        daemon.start();
    }
}