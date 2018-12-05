package com.juan.android.database;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import com.juan.android.sinkemall.SingletonClass;
import com.juan.android.sinkemall.Turn;

/**
 * Created by juan on 01/02/18.
 */

public class TurnWrapper extends CursorWrapper {

    public Cursor mCursor;
    public Context mContext;
    public static final String TAG="turnwrapper";
    private Turn mTurn;

    public TurnWrapper(Cursor cursor,Context context)
    {
        super(cursor);
        mContext=context;
        mCursor=cursor;
        mTurn=new Turn();
    }

    public Turn getTurnData(Cursor cursor)
    {
        mCursor=cursor;
        if (mCursor.getCount()==0 || mCursor==null)
        {
            SingletonClass.resetTurnsToZero();

        }
        else {
            try {

                mCursor.moveToFirst();
                mTurn.setmTurnNumber(mCursor.getInt(mCursor.getColumnIndex(TurnDatabase.Cols.TURNS)));
                mTurn.setmTimesHit(mCursor.getInt(mCursor.getColumnIndex(TurnDatabase.Cols.TIMES_HIT)));
                mTurn.setmTimesHitting(mCursor.getInt(mCursor.getColumnIndex(TurnDatabase.Cols.TIMES_HITTING)));
                mTurn.setmWhohitNext(mCursor.getInt(mCursor.getColumnIndex(TurnDatabase.Cols.HITS_NEXT)));
            } finally {
                mCursor.close();
            }
        }
        return mTurn;
    }

}
