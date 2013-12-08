package com.bla.laa;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusAdapter;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

public class TwitterStreamDaemon implements StatusListener {

    private static final SimpleDateFormat SDF_DATETIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private TwitterStream twitterStream;

    private List<StatusListener> listeners = new LinkedList<StatusListener>();


    public TwitterStreamDaemon(TwitterStream twitterStream) {
        this.twitterStream = twitterStream;
        this.twitterStream.addListener(this);
    }

    public void addListener(StatusListener listener) {
        listeners.add(listener);
    }

    public void track(Collection<String> keywordsets) {
        track(keywordsets.toArray(new String[keywordsets.size()]));
    }

    public void track(String[] keywordsets) {
        FilterQuery filterQuery = new FilterQuery();
        filterQuery.track(keywordsets);
        track(filterQuery);
    }

    public void track(FilterQuery filterQuery) {
        twitterStream.cleanUp();
        twitterStream.filter(filterQuery);
    }

    private void printException(String message, Exception e) {
        System.err.println(message + " - " + e.getMessage());
        e.printStackTrace(System.err);
    }

    @Override
    public void onException(Exception ex) {
        for (StatusListener listener : listeners) {
            try { listener.onException(ex); }
            catch (Exception e) { printException("Exception while notifying listener about " + ex, e); }
        }
    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
        for (StatusListener listener : listeners) {
            try { listener.onDeletionNotice(statusDeletionNotice); }
            catch (Exception e) { printException("Exception while notifying listener about " + statusDeletionNotice, e); }
        }
	}

    @Override
    public void onScrubGeo(long userId, long upToStatusId) {
        for (StatusListener listener : listeners) {
            try { listener.onScrubGeo(userId, upToStatusId); }
            catch (Exception e) { printException("Exception while notifying listener about scrubGeo(" + userId + ", " + upToStatusId + ")", e); }
        }
    }

    @Override
    public void onStallWarning(StallWarning warning) {
        for (StatusListener listener : listeners) {
            try { listener.onStallWarning(warning); }
            catch (Exception e) { printException("Exception while notifying listener about " + warning, e); }
        }
    }

    @Override
    public void onStatus(Status status) {
        for (StatusListener listener : listeners) {
            try { listener.onStatus(status); }
            catch (Exception e) { printException("Exception while notifying listener about " + status, e); }
        }
    }

    @Override
    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
        for (StatusListener listener : listeners) {
            try { listener.onTrackLimitationNotice(numberOfLimitedStatuses); }
            catch (Exception e) { printException("Exception while notifying listener about limited statuses " + numberOfLimitedStatuses, e); }
        }
    }

    public static void main(String[] args) {

        TwitterStreamDaemon daemon = new TwitterStreamDaemon(TwitterStreamFactory.getSingleton());

        daemon.addListener(new StatusAdapter() {
            @Override
            public void onStatus(Status status) {
                System.out.println(status.getId() + " " +
                                   SDF_DATETIME.format(status.getCreatedAt()) + " " +
                                   status.getUser().getScreenName() + ": " +
                                   status.getText().replaceAll("\\s", " "));
            }
        });

        List<String> keywordsets = new ArrayList<String>();

        keywordsets.add("latvia");
        keywordsets.add("balts sniegs");

        daemon.track(keywordsets);
    }
}
