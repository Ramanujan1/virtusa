package com.footballapp.utils;

public class Constants {
    public static final String DATA_FILE = "datafile";
    public static final String FILE_DELIMITER = ",";

    public enum MatchLocation {
        HOME,
        AWAY
    }

    public enum FileLoadMode {
        LOAD_ON_STARTUP
    }

    public static final String VERSES = "_vs_";
}