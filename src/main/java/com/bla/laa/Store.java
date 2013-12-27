package com.bla.laa;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import static org.elasticsearch.common.xcontent.XContentFactory.smileBuilder;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

public class Store {

    public static final String LV_TWETTER_CLUSTER = "lvTwetterCluster";
    static Logger logger = LogManager.getLogger(Store.class.getName());

    private String indicesName = "tweets";
    private String typeName = "tweet";
    private Client client;

    public void doStore(List<Status> tweets) throws IOException {
        logger.info("Storing ( " + tweets.size() + " ) tweets");

        Client client =  getClient();
        for (Status status : tweets) {
            String id = String.valueOf(status.getId());
            String strObj =  DataObjectFactory.getRawJSON(status);
            storeStrObj(client, id, strObj  );
        }

        logger.info("Doc count ( " + getDocCount() + " ) ");

    }

    private void storeStrObj(Client client, String id, String strObj){
        logger.debug("Try to store tweet : " + id);

        if (!client.prepareGet(indicesName, typeName, id).execute().actionGet().isExists()){
            IndexResponse response = client.prepareIndex(indicesName, typeName, id)
                    .setSource(strObj)
                    .execute()
                    .actionGet();
            logger.debug("Stored tweet id :  " + response.getId() + " version " + response.getVersion());

        } else {
            logger.debug("Tweet with id  ( " + id + " ) already exists ");
        }

    }

    private Client getClient() throws IOException {
        if ( client == null ) {
            logger.info(" Traying to connect cluster : "+ LV_TWETTER_CLUSTER );
            Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", LV_TWETTER_CLUSTER).build();
            client = new TransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress("192.168.56.101", 9300))
                    .addTransportAddress(new InetSocketTransportAddress("192.168.56.102", 9300));
            createMapping();
        }
        return client;
    }

    private long getDocCount() throws IOException {
        Client client =  getClient();
        long count = client.prepareCount(indicesName).execute().actionGet().getCount();
        return count;
    }


    private void createMapping() throws IOException {
        Client client =  getClient();

        client.admin().indices().prepareDelete().execute().actionGet();
        if (!client.admin().indices().prepareExists(indicesName).execute().actionGet().isExists()){
            logger.info("Creating indice ( "+ indicesName +" ) with type ( "+ typeName +" ) ");
            client.admin().indices().prepareCreate(indicesName).execute().actionGet();
            client.admin().cluster().prepareHealth().setWaitForYellowStatus().execute().actionGet();


            StringBuilder sb = new StringBuilder();
            sb.append("{                                                           ");
            sb.append("    \""+ typeName +"\": {                                   ");
            sb.append("        \"_source\": {                                      ");
            sb.append("            \"enabled\": \"true\"                           ");
            sb.append("        },                                                  ");
            sb.append("        \"_timestamp\": {                                   ");
            sb.append("            \"enabled\": \"yes\",                           ");
            sb.append("            \"store\":   \"yes\",                           ");
            sb.append("            \"path\":    \"created_at\",                    ");
            sb.append("            \"index\":   \"no\",                            ");
            sb.append("            \"format\":  \"EEE MMM yy HH:mm:ss Z yyyy\"     ");
            sb.append("        },                                                  ");
            sb.append("        \"properties\": {                                   ");
            sb.append("            \"created_at\": {                               ");
            sb.append("                \"type\":   \"date\",                       ");
            sb.append("                \"format\": \"EEE MMM yy HH:mm:ss Z yyyy\"  ");
            sb.append("            },                                              ");
            sb.append("            \"user.created_at\": {                          ");
            sb.append("                \"type\":   \"date\",                       ");
            sb.append("                \"format\": \"EEE MMM yy HH:mm:ss Z yyyy\"  ");
            sb.append("            }                                               ");
            sb.append("        }                                                   ");
            sb.append("    }                                                       ");
            sb.append("}                                                           ");

            client.admin().indices().preparePutMapping().setType(typeName).setSource(sb.toString()).execute().actionGet();

            //client.prepareIndex(indicesName, typeName).setSource(jsonBuilder().startObject()
            //        .field("created_at", "Thu Dec 26 09:29:56 +0000 2013")
            //        .endObject()).execute().actionGet();

            client.admin().indices().prepareRefresh().execute().actionGet();
        }
    }



}
