package com.juan.android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by juan on 09/03/18.
 */

public class MissedHitYouHelper extends SQLiteOpenHelper {
    private Context mContext;
    private static final int VERSION=1;
    private static final String DATABASE_NAME="missedHitYou.db";

    public MissedHitYouHelper(Context context)
    {
        super(context,DATABASE_NAME,null,VERSION);
        mContext=context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+MissedHit.MISSED_HIT_TABLE+ " ("+
                MissedHit.Cols.INDEX+")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
