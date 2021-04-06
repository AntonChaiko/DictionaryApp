package com.example.dictionaryapp.model;

import java.util.ArrayList;

public class TranslateData {

    private ArrayList<Matches> matches;

    public ArrayList<Matches> getMatches() {
        return matches;
    }

    public static class Matches{
        String segment;

        String translation;

        public String getSegment() {
            return segment;
        }

        public String getTranslation() {
            return translation;
        }

    }
}
