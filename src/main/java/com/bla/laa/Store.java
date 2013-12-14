package com.bla.laa;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.node.Node;
import twitter4j.Status;
import twitter4j.json.DataObjectFactory;

import java.util.List;

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
        IndexResponse response = client.prepareIndex("twitter_2", "tweet", id)
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


}
