package com.juan.android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by juan on 01/02/18.
 */

public class TurnHelper extends SQLiteOpenHelper {

    private Context mContext;
    private static final int VERSION=1;
    private static final String DATABASE_NAME="turnsdatabase.db";

    public TurnHelper(Context context)
    {
        super(context,DATABASE_NAME,null,VERSION);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+TurnDatabase.TABLE + "( "+
                TurnDatabase.Cols.TURNS+", "+
                TurnDatabase.Cols.TIMES_HIT+", "+
                TurnDatabase.Cols.TIMES_HITTING+", "+
                TurnDatabase.Cols.HITS_NEXT+")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
