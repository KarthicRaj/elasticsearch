package com.bla.laa;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.StatusAdapter;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class TwitterSearchDaemon {

    private static final int MAX_RESULTS_PER_SEARCH = 100;

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Queue<Twitter> twitters = new LinkedList<Twitter>();

    private Queue<Query> queries = new LinkedList<Query>();
    private Map<Query, QueryResult> lastQueryResults = new HashMap<Query, QueryResult>();

    private List<StatusListener> listeners = new LinkedList<StatusListener>();

    private Timer timer = new Timer();

    public TwitterSearchDaemon() {
    }

    public void addTwitter(Twitter twitter) {
        twitters.add(twitter);
    }

    public void addQuery(Query query) {
        queries.add(query);
    }

    public void addQuery(String strQuery) {
        addQuery(strQuery, null);
    }

    public void addQuery(String strQuery, String lang) {

        Query query = new Query();
        query.setQuery(strQuery);
        if (lang != null)
            query.setLang(lang);
        query.setCount(MAX_RESULTS_PER_SEARCH);
        query.setResultType(Query.RECENT);

        addQuery(query);
    }

    public void addQuery(double latitude, double longitude, double radius) {

        Query query = new Query();
        query.setGeoCode(new GeoLocation(latitude, longitude), radius, Query.KILOMETERS);
        query.setCount(MAX_RESULTS_PER_SEARCH);
        query.setResultType(Query.RECENT);

        addQuery(query);
    }

    public void addListener(StatusListener listener) {
        listeners.add(listener);
    }

    protected Twitter getNextTwitter() {
        Twitter twitter = twitters.poll();
        twitters.offer(twitter);
        return twitter;
    }

    public void start(int intervalInSeconds) {

        TimerTask task = new TimerTask() {
            @Override public void run() {
                TwitterSearchDaemon.this.run();
            }
        };

        timer.scheduleAtFixedRate(task, 0, intervalInSeconds * 1000);
    }

    public void run() {
        for (Query query : queries) {
            try {
                process(getNextTwitter(), query);
            } catch (TwitterException e) {
                e.printStackTrace();
            }
        }
    }

    private void process(Twitter twitter, Query query) throws TwitterException {

        QueryResult lastQueryResult = lastQueryResults.get(query);

        if (lastQueryResult != null)
            query.setSinceId(lastQueryResult.getMaxId());

        QueryResult queryResult = twitter.search(query);
        List<Status> statuses = queryResult.getTweets();

        System.out.println(sdf.format(new Date()) + ": " + statuses.size() + " search results retrieved for " + query);

        process(statuses);

        lastQueryResults.put(query, queryResult);
	}

    private void onException(String message, Exception e) {
        System.err.println(message + " - " + e.getMessage());
        e.printStackTrace(System.err);
    }

    private void process(List<Status> statuses) {
        for (Status status : statuses) {
            process(status);
        }
    }

    private void process(Status status) {
        for (StatusListener listener : listeners) {
            try {
                listener.onStatus(status);
            } catch (Exception e) {
                onException("Exception while notifying listener", e);
            }
        }
    }

    public static void main(String[] args) {

        TwitterSearchDaemon daemon = new TwitterSearchDaemon();

        daemon.addTwitter(TwitterFactory.getSingleton());

        daemon.addListener(new StatusAdapter() {
            @Override
            public void onStatus(Status status) {
                System.out.println(status.getId() + " " +
                                   sdf.format(status.getCreatedAt()) + " " +
                                   status.getUser().getScreenName() + ": " +
                                   status.getText().replaceAll("\\s", " "));
            }
        });

        daemon.addQuery("latvija");
        daemon.addQuery(56.968936, 24.105163, 20);

        daemon.start(15);
    }

}
