package com.juan.android.database;

/**
 * Created by juan on 01/02/18.
 */

public class TurnDatabase {

    public static final String TABLE="Turns_table";//table name

    public class Cols
    {
        public static final String TURNS="turns_passed";//number of turns so far
        public static final String TIMES_HIT="times_hit";//how many times COM has hit YOU
        public static final String TIMES_HITTING="times_hitting"; //how many times YOU have hit COM
        public static final String HITS_NEXT="who_hits_next";
    }
}
