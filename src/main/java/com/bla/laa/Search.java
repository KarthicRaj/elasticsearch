package com.bla.laa;

import twitter4j.*;
import twitter4j.json.DataObjectFactory;

import java.util.List;

public class Search {


    public List<Status> doSearch(String byQuery) throws TwitterException {
        System.out.println("query by : "+ byQuery);

        Twitter twitter = TwitterFactory.getSingleton();
        Query query = new Query(byQuery);
        query.setLang("lv");

        QueryResult result = null;
        try {
            result = twitter.search(query);
        } catch (TwitterException e) {
            e.printStackTrace();
        }

        for (Status status : result.getTweets()) {
            System.out.println("@" + status.getId() + " : " + status.getText());
        }

        return result.getTweets();
    }
}
