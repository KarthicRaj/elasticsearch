package com.bla.laa;

import twitter4j.Status;

import java.util.List;
import java.util.Random;

public class Utils {

    private Random random = new Random();
    private String wasteSymb[] ={"(", ")",",",".",":",";","'","[","]","{","}","_","-","*","&","^","%","$","£","@","!","~"};

    public String getRandomWord(List<Status> tweets){
        if (tweets.size() == 0 ){
            return "maxima";
        }
        System.out.println("working with tweets : "+ tweets.size());
        int tweetCount =  tweets.size();

        String tweetText = tweets.get(randomizer(tweetCount)).getText();
        System.out.println("selected text : "+ tweetText);

        String tokinazedText[] = tweetText.split(" ");

        String selectedWord = tokinazedText[randomizer(tokinazedText.length)];
        System.out.println("selected word : "+ selectedWord);
        selectedWord = removeWaste(selectedWord);
        System.out.println("cleared word : "+ selectedWord);

        if (selectedWord.length() < 3){
            selectedWord = getRandomWord(tweets);
        }

        return selectedWord;
    }

    private int randomizer(int seed ){
        System.out.println("seed : "+ seed);
        return  random.nextInt(seed);
    }

    private String removeWaste(String word){
        for (String symb : wasteSymb ){
            word = word.replace(symb, "");
        }
        return word;
    }

}
