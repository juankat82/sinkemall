package com.juan.android.database;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;

import com.juan.android.sinkemall.Ranks;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by juan on 12/11/17.
 */

public class RankCursorWrapper extends CursorWrapper {

    private Cursor mCursor;
    private Context mContext;
    private List<Ranks> mRanks=new ArrayList<>();

    public RankCursorWrapper(Cursor cursor, Context context)
    {
        super(cursor);
        mCursor=cursor;
        mContext=context;
    }

    public List<Ranks> getRanks(Cursor cursor)
    {
        mCursor=cursor;
       try{
            mCursor.moveToFirst();
            while(!mCursor.isAfterLast())
            {
                Ranks rank=new Ranks();
                String position=mCursor.getString(getColumnIndex(Records.RecordDataBase.Cols.POSITION));
                String name=mCursor.getString(getColumnIndex(Records.RecordDataBase.Cols.PLAYER_NAME));
                String points=mCursor.getString(getColumnIndex(Records.RecordDataBase.Cols.POINTS));
                String machineDefault=mCursor.getString(getColumnIndex(Records.RecordDataBase.Cols.MACHINE_DEFAULT));
                rank.setId(Integer.parseInt(position));
                rank.setName(name);
                rank.setPoints(Long.parseLong(points));
                rank.setMachineDefault(Boolean.parseBoolean(machineDefault));
                mRanks.add(rank);
                mCursor.moveToNext();
            }
        }
       finally
       {
           mCursor.close();
       }
        return mRanks;
    }
}
