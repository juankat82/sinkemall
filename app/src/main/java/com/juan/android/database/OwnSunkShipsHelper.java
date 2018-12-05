package com.juan.android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by juan on 14/03/18.
 */

public class OwnSunkShipsHelper extends SQLiteOpenHelper {

    private Context mContext;
    private static final int VERSION=1;
    private static final String DATABASE_NAME="ownSunkShips.db";

    public OwnSunkShipsHelper (Context context)
    {
        super(context,DATABASE_NAME,null,VERSION);//context,database name, factory (Cursor factory),version
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+OwnSunkShips.TABLE_NAME+ " ("+
                OwnSunkShips.Cols.OWN_SUNK_SHIPS+")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
