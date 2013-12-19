package com.bla.laa;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.node.Node;
import twitter4j.Status;
import twitter4j.json.DataObjectFactory;

import java.io.IOException;
import java.util.List;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;


public class Store {

    public void doStore(List<Status> tweets) {

        Client client =  getClient();
        for (Status status : tweets) {
            String id = String.valueOf(status.getId());
            String strObj =  DataObjectFactory.getRawJSON(status);
            storeStrObj(client, id, strObj  );
        }
    }

    public void doStore(Status tweet) {

        Client client =  getClient();
        String id = String.valueOf(tweet.getId());
        String strObj =  DataObjectFactory.getRawJSON(tweet);
        storeStrObj(client, id, strObj  );

    }

    private void storeStrObj(Client client, String id, String strObj){
        IndexResponse response = client.prepareIndex("twitter", "tweet", id)
                .setSource(strObj)
                .execute()
                .actionGet();
        System.out.println(response.getId());
    }

    private Client getClient(){
        Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", "lvTwetterCluster").build();
        Client client = new TransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress("192.168.56.101", 9300))
                .addTransportAddress(new InetSocketTransportAddress("192.168.56.102", 9300));

        return client;
    }


    public static void main (String args[]) throws IOException {
        new Store().test2();

    }


    private void test2() throws IOException {
        Client client =  getClient();

        client.admin().indices().prepareDelete().execute().actionGet();
        client.admin().indices().prepareCreate("twitter").execute().actionGet();
        client.admin().cluster().prepareHealth().setWaitForYellowStatus().execute().actionGet();

        String mapping = jsonBuilder()
                .startObject()
                    .startObject("tweet")
                        .startObject("_timestamp")
                            .field("enabled", "yes")
                            .field("store", "yes")
                            .field("path","post_date")
                        .endObject()
                        .startObject("properties")
                            .startObject("field1")
                                .field("type", "string")
                                .field("store", "yes")
                            .endObject()
                            .startObject("field2")
                                .field("type", "string")
                                .field("store", "no")
                            .endObject()
                        .endObject()
                    .endObject()
                .endObject().string();

        client.admin().indices().preparePutMapping().setType("tweet").setSource(mapping).execute().actionGet();

        client.prepareIndex("twitter", "tweet", "3").setSource(jsonBuilder().startObject()
                .field("field1", "value1")
                .field("post_date", "2009-11-15T14:12:13")
                .endObject()).execute().actionGet();

        client.admin().indices().prepareRefresh().execute().actionGet();

    }



}
