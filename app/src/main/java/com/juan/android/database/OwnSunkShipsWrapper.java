package com.juan.android.database;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;

import com.juan.android.sinkemall.OwnShip;

/**
 * Created by juan on 14/03/18.
 */

public class OwnSunkShipsWrapper extends CursorWrapper {
    private Cursor mCursor;
    private Context mContext;

    public OwnSunkShipsWrapper(Context context, Cursor cursor) {
        super(cursor);
        mCursor=cursor;
        mContext=context;
    }

    public int getOwnSunkShips(Cursor cursor) {
        mCursor = cursor;
        int ownSunkShips=0;
        try
        {
            mCursor.moveToFirst();
            ownSunkShips=mCursor.getInt(mCursor.getColumnIndex(OwnSunkShips.Cols.OWN_SUNK_SHIPS));
        }
        finally {
            mCursor.close();
        }

        return ownSunkShips;


    }
}
