package com.juan.android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by juan on 12/11/17.
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    private Context mContext;
    private static final int VERSION=1;
    private static final String DATABASE_NAME="hallOfFame.db";

    public DataBaseHelper (Context context)
    {
        super(context,DATABASE_NAME,null,VERSION);//context,database name, factory (Cursor factory),version
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+Records.RecordDataBase.NAME+ " ("+
                    Records.RecordDataBase.Cols.POSITION +", "+
                Records.RecordDataBase.Cols.PLAYER_NAME+", "+
                Records.RecordDataBase.Cols.POINTS+", "+
                Records.RecordDataBase.Cols.MACHINE_DEFAULT+")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
