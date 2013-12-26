package com.bla.laa;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twitter4j.*;
import twitter4j.json.DataObjectFactory;

import java.util.List;

public class Search {
    static Logger logger = LogManager.getLogger(Search.class.getName());


    public List<Status> doSearch(String byQuery) throws TwitterException {
        logger.info("Query by : "+ byQuery);

        Twitter twitter = TwitterFactory.getSingleton();
        Query query = new Query(byQuery);
        query.setLang("lv");
        query.setCount(100);

        QueryResult result = null;
        try {
            result = twitter.search(query);
        } catch (TwitterException e) {
            logger.error(e);
        }

        logger.info("Found : "+ result.getTweets().size() +" tweets");

        for (Status status : result.getTweets()) {
            logger.info("Found : "+ status.getId());
            logger.debug("@" + status.getId() + " [ " + status.getCreatedAt() + " ] "+ " : " + status.getText());
        }

        return result.getTweets();
    }


}
