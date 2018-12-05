package com.juan.android.database;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

/**
 * Created by juan on 15/11/17.
 */

public class GameOnWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    private Cursor mCursor;
    private Context mContext;
    private static final String TAG="GameOnWrapper";
    private boolean isGameOn;

    public GameOnWrapper(Context context, Cursor cursor) {
        super(cursor);
        mCursor=cursor;
        mContext=context;
    }

    public boolean isGameOn(Cursor cursor) {
        mCursor = cursor;

//        if (mCursor!=null)
//        {
//            Log.e(TAG,"Cursor isnt null");
            try {
                mCursor.moveToFirst();
                String str=mCursor.getString(getColumnIndex(HeldGame.Cols.GAME_IS_HELD));
//                Log.e(TAG,"Cursor content: "+str);
                if (str.equals("1"))
                    isGameOn=true;
                else
                    isGameOn=false;
            } finally {
                mCursor.close();
            }
//    }
//        else
//        {
//            Log.e(TAG,"Cursor is null");
//            isGameOn=false;
//        }
//
        return isGameOn;
    }
}
