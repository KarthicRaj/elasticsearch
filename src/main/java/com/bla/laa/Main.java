package com.bla.laa;

import org.apache.logging.log4j.LogManager;
import twitter4j.*;

import java.io.IOException;
import org.apache.logging.log4j.Logger;


public class Main {
    static Logger logger = LogManager.getLogger(Main.class);

    public static void main(String args[]) throws TwitterException, InterruptedException, IOException {
        logger.info("Starting twitter Crawler ... ");

        if (args.length != 0){
            Tactics tactics = new Tactics();
            switch (args[0]){
                case "1" :  {
                    tactics.searchByDynamicKey();
                }
                case "2" : {
                    tactics.searchByGeo();
                }
                case "3" : {
                    tactics.searchByOnDayInPass();
                }

            }
        } else {
            logger.info("Please specify crawling tactics [1,2,3] ! ");
        }

        logger.info("Program quiting ... ");
    }
}