package com.bla.laa;

import org.apache.logging.log4j.LogManager;
import twitter4j.*;

import java.io.IOException;
import org.apache.logging.log4j.Logger;


public class Main {
    static Logger logger = LogManager.getLogger(Main.class);

    public static void main(String args[]) throws TwitterException, InterruptedException, IOException {
        logger.info("Starting lv-twitter crawler ... ");

        //new Tactics().searchByDynamicKey();
        //new Tactics().searchByGeo();
        new Tactics().searchByOnDayInPass();


    }
}