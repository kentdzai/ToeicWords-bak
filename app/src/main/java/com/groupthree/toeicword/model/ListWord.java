package com.groupthree.toeicword.model;

public class ListWord {
    public int Id;
    public String Word;
    public String Mean;
    public int FavouriteWord;

    public ListWord(int id, String word, String mean, int favouriteWord) {
        Id = id;
        Word = word;
        Mean = mean;
        FavouriteWord = favouriteWord;
    }

    public boolean getCheck() {
        if (FavouriteWord == 0) {
            return false;
        }
        return true;
    }

    public void setCheck(boolean checking) {
        if (checking) {
            FavouriteWord = 1;
        } else {
            FavouriteWord = 0;
        }
    }
}
