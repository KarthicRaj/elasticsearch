package com.bla.laa;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twitter4j.Status;

import java.util.List;
import java.util.Random;

public class Utils {
    static Logger logger = LogManager.getLogger(Utils.class.getName());


    private Random random = new Random();
    private String wasteSymb[] ={"(", ")",",",".",":",";","'","[","]","{","}","_","-","*","&","^","%","$","£","@","!","~"};

    public String getRandomWord(List<Status> tweets){
        if (tweets.size() == 0 ){
            return "maxima";
        }
        logger.debug("working with tweets : " + tweets.size());
        int tweetCount =  tweets.size();

        String tweetText = tweets.get(randomizer(tweetCount)).getText();
        logger.debug("selected text : " + tweetText);

        String tokinazedText[] = tweetText.split(" ");

        String selectedWord = tokinazedText[randomizer(tokinazedText.length)];
        logger.debug("selected word : " + selectedWord);
        selectedWord = removeWaste(selectedWord);
        logger.debug("cleared word : " + selectedWord);

        if (selectedWord.length() < 3){
            selectedWord = getRandomWord(tweets);
        }

        return selectedWord;
    }

    private int randomizer(int seed ){
        logger.debug("seed : " + seed);
        return  random.nextInt(seed);
    }

    private String removeWaste(String word){
        for (String symb : wasteSymb ){
            word = word.replace(symb, "");
        }
        return word;
    }

}
