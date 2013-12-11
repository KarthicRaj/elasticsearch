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

        Client client = new TransportClient()
                .addTransportAddress(new InetSocketTransportAddress("127.0.0.1", 9300));
        for (Status status : tweets) {
            IndexResponse response = client.prepareIndex("twitter", "tweet", String.valueOf(status.getId()))
                    .setSource(DataObjectFactory.getRawJSON(status))
                    .execute()
                    .actionGet();

            System.out.println(response.getId());
        }
    }

    public void doStore(Status tweet) {

        Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", "lvTwetterCluster").build();
        Client client = new TransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress("192.168.56.101", 9300))
                .addTransportAddress(new InetSocketTransportAddress("192.168.56.102", 9300));

        //Node node = nodeBuilder().node();
        //Client client = node.client();

        IndexResponse response = client.prepareIndex("twitter", "tweet", String.valueOf(tweet.getId()))
                .setSource(DataObjectFactory.getRawJSON(tweet))
                .execute()
                .actionGet();

        System.out.println(response.getId());


    }


}
