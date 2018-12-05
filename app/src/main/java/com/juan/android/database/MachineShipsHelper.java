package com.juan.android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by juan on 27/01/18.
 *
 * STATE value can be:
 *  untouched--ship on it but never hit
 *  hit--ship on it plus been hit
 *
 *  missed--no ship but its been hit
 *  empty--no ship on it plus not hit
 *
 *
 */

public class MachineShipsHelper extends SQLiteOpenHelper {

    private Context mContext;
    private static final int VERSION=1;
    private static final String DATABASE_NAME="machineShips.db";

    public MachineShipsHelper(Context context)
    {
        super(context,DATABASE_NAME,null,VERSION);
        mContext=context;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("create table "+MachineShipsTable.MACHINE_SHIPS_TABLE_NAME+ " ("+
            MachineShipsTable.Cols.COMP_SHIP_ID+", "+
            MachineShipsTable.Cols.INDEX_A_NUMBER+", "+
            MachineShipsTable.Cols.INDEX_B_NUMBER+", "+
            MachineShipsTable.Cols.INDEX_C_NUMBER+", "+
            MachineShipsTable.Cols.INDEX_D_NUMBER+", "+
                MachineShipsTable.Cols.INDEX_A_STATE+", "+
                MachineShipsTable.Cols.INDEX_B_STATE+", "+
                MachineShipsTable.Cols.INDEX_C_STATE+", "+
                MachineShipsTable.Cols.INDEX_D_STATE+", "+
            MachineShipsTable.Cols.COLOR+", "+
            MachineShipsTable.Cols.STATE+")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}
