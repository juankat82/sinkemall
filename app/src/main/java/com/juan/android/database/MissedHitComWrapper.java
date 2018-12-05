package com.juan.android.database;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by juan on 09/03/18.
 */

public class MissedHitComWrapper extends CursorWrapper {

    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */

    private List<Integer> mMachineMissedHitList;
    private Context mContext;
    private Cursor mCursor;

    public MissedHitComWrapper(Cursor cursor) {
        super(cursor);
        mCursor=cursor;
        mMachineMissedHitList=new ArrayList<>();
    }

    public List<Integer> getMachineMissedHitList(Cursor cursor)
    {
        mCursor=cursor;

        try
        {
            mCursor.moveToFirst();
            while(!mCursor.isAfterLast())
            {
                int cell=cursor.getInt(getColumnIndex(MissedHit.Cols.INDEX));
                mMachineMissedHitList.add(cell);
                mCursor.moveToNext();
            }
        }
        finally {
            mCursor.close();
        }

        return mMachineMissedHitList;
    }

}
