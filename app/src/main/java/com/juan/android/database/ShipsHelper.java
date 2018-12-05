package com.juan.android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by juan on 03/01/18.
 */

public class ShipsHelper extends SQLiteOpenHelper {

    private Context mContext;
    private final static int VERSION=1;
    private final static String DATABASE_NAME="shipDataBase.db";


    public ShipsHelper (Context context)
    {

        super(context,DATABASE_NAME,null,VERSION);//context,database name, factory (Cursor factory),version
        mContext=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+Ships.ShipTable.NAME+ " ("+
                Ships.ShipTable.Cols.ID+", "+
                Ships.ShipTable.Cols.CHECK_ONE +", "+
                Ships.ShipTable.Cols.CHECK_TWO+", "+
                Ships.ShipTable.Cols.CHECK_THREE+", "+
                Ships.ShipTable.Cols.CHECK_FOUR+", "+
                Ships.ShipTable.Cols.INDEX_A_STATE+", "+
                Ships.ShipTable.Cols.INDEX_B_STATE+", "+
                Ships.ShipTable.Cols.INDEX_C_STATE+", "+
                Ships.ShipTable.Cols.INDEX_D_STATE+", "+
                Ships.ShipTable.Cols.STATE+", "+
                Ships.ShipTable.Cols.COLOR+")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
