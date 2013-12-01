package com.bla.laa;

import twitter4j.*;

import java.util.List;

public final class Main {
    public static void main (String args[]) throws TwitterException {
        Search search = new Search();
        List<Status> tweets =  search.doSearch();
        Store store = new Store();
        store.doStore(tweets);

    }
}