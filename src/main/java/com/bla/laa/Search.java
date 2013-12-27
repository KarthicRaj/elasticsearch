package com.bla.laa;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.common.joda.time.DateTime;
import twitter4j.*;
import twitter4j.json.DataObjectFactory;

import java.util.List;

public class Search {
    static Logger logger = LogManager.getLogger(Search.class.getName());


    public List<Status> doSearch(Query query) throws TwitterException {
        logger.info ("Query by : "+ query.getQuery() +" , "+ query.getGeocode() + " , "+ query.getSince() + " , "+ query.getUntil());
        logger.debug("Query by : " + query.toString()); 
        Twitter twitter = TwitterFactory.getSingleton();

        QueryResult result = null;
        try {
            result = twitter.search(query);
        } catch (TwitterException e) {
            logger.error(e);
        }

        logger.info("Found : "+ result.getTweets().size() +" tweets");

        for (Status status : result.getTweets()) {
            logger.debug("@" + status.getId() + " [ " + status.getCreatedAt() + " ] "+ " : " + status.getText());
        }

        return result.getTweets();
    }

    public Query createSimpeQuery(String searchString){
        Query query = new Query(searchString);
        query.setLang("lv");
        query.setCount(100);

        return query;
    }

    public Query createGeoQuery(){
        Query query = new Query();
        GeoLocation geoLocation = new GeoLocation(56.95919584 , 24.10789312);
        query.setGeoCode(geoLocation, 300, Query.KILOMETERS);
        query.setLang("lv");
        query.setCount(100);

        return query;
    }

    public Query createSimpeQueryOnDate(String queryStr, DateTime date){
        Query query = new Query(queryStr);
        query.setLang("lv");
        query.setCount(100);
        query.setSince(date.toString("yyyy-MM-dd"));
        query.setUntil(date.plusDays(1).toString("yyyy-MM-dd"));

        return query;
    }

}
