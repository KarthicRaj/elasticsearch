package com.bla.laa;

import twitter4j.*;
import twitter4j.json.DataObjectFactory;

public class Search {


    public void doSearch() throws TwitterException {
        Twitter twitter = TwitterFactory.getSingleton();
        Query query = new Query("maxima");
        query.setLang("lv");

        QueryResult result = null;
        try {
            result = twitter.search(query);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        for (Status status : result.getTweets()) {
            System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText() + "\n");
            System.out.println(DataObjectFactory.getRawJSON(status));
        }

    }
}
