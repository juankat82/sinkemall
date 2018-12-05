package com.juan.android.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import com.juan.android.sinkemall.MachineShip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by juan on 28/01/18.
 */

public class MachineShipsWrapper extends CursorWrapper {

    private Cursor mCursor;
    private List<MachineShip> mMachineShipList;
    private final static String TAG="MachineShipsWrapper";

    public MachineShipsWrapper(Cursor cursor) {
        super(cursor);
        mCursor=cursor;
        if (mMachineShipList==null)
            mMachineShipList=new ArrayList<>();

    }
    public List<MachineShip> getShips(Cursor cursor)
    {
        mCursor=cursor;
        //fill arraylist from database
        try
        {
            mCursor.moveToFirst();

            while(!mCursor.isAfterLast())
            {
                MachineShip machineShip=new MachineShip();
                machineShip.setmId(mCursor.getString(getColumnIndex(MachineShipsTable.Cols.COMP_SHIP_ID)));
                machineShip.setmIndexA(mCursor.getInt(getColumnIndex(MachineShipsTable.Cols.INDEX_A_NUMBER)));
                machineShip.setmIndexB(mCursor.getInt(getColumnIndex(MachineShipsTable.Cols.INDEX_B_NUMBER)));
                machineShip.setmIndexC(mCursor.getInt(getColumnIndex(MachineShipsTable.Cols.INDEX_C_NUMBER)));
                machineShip.setmIndexD(mCursor.getInt(getColumnIndex(MachineShipsTable.Cols.INDEX_D_NUMBER)));
                machineShip.setmIndexAState(mCursor.getString(getColumnIndex(MachineShipsTable.Cols.INDEX_A_STATE)));
                machineShip.setmIndexBState(mCursor.getString(getColumnIndex(MachineShipsTable.Cols.INDEX_B_STATE)));
                machineShip.setmIndexCState(mCursor.getString(getColumnIndex(MachineShipsTable.Cols.INDEX_C_STATE)));
                machineShip.setmIndexDState(mCursor.getString(getColumnIndex(MachineShipsTable.Cols.INDEX_D_STATE)));
                machineShip.setmColor(mCursor.getString(getColumnIndex(MachineShipsTable.Cols.COLOR)));
                machineShip.setmState(mCursor.getString(getColumnIndex(MachineShipsTable.Cols.STATE)));
                mMachineShipList.add(machineShip);
                mCursor.moveToNext();
            }
        }
        finally {
            mCursor.close();
        }
        Log.i(TAG,"Machine ship List size: "+mMachineShipList.size());
        return mMachineShipList;
    }
}
