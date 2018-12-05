package com.juan.android.database;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by juan on 09/03/18.
 */

public class MissedHitYouWrapper extends CursorWrapper {
    private List<Integer> mYouMissedHitList;
    private Context mContext;
    private Cursor mCursor;

    public MissedHitYouWrapper(Cursor cursor) {
        super(cursor);
        mCursor=cursor;
        mYouMissedHitList=new ArrayList<>();
    }

    public List<Integer> getYouMissedHitList(Cursor cursor)
    {
        mCursor=cursor;

        try
        {
            mCursor.moveToFirst();
            while(!mCursor.isAfterLast())
            {
                int cell=cursor.getInt(getColumnIndex(MissedHit.Cols.INDEX));
                mYouMissedHitList.add(cell);
                mCursor.moveToNext();
            }
        }
        finally {
            mCursor.close();
        }

        return mYouMissedHitList;
    }
}
