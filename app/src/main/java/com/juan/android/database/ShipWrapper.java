package com.juan.android.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.juan.android.sinkemall.OwnShip;
import com.juan.android.sinkemall.R;
import com.juan.android.sinkemall.SingletonClass;

import java.util.ArrayList;
import java.util.List;

import com.juan.android.database.Ships.ShipTable.Cols;

/**
 * Created by juan on 03/01/18.
 */

public class ShipWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    private static Cursor mCursor;
    private Context mContext;
    private static final String TAG="ShipWrapper";
    private View mView;
    private List<Button> listOfShips;
    private List<Button> buttonList;
    private Cursor sCursor1;
    private List<OwnShip> sOwnShipList;

    public ShipWrapper(Cursor cursor,Context context,View view,List<Button> list) {
        super(cursor);
        mContext=context;
        mCursor=cursor;
        mView=view;
        listOfShips=list;
        buttonList=new ArrayList<>();
        SingletonClass.setComeBack(true);
    }
    public ShipWrapper(Context context,Cursor cursor)
    {
        super(cursor);
        mContext=context;
        mCursor=cursor;
    }
    public List<OwnShip> getOwnShips(Cursor cursor)
    {
        sOwnShipList=new ArrayList<>();
        sCursor1=cursor;
        String[] ship1=new String[11];
        String[] ship2=new String[11];
        String[] ship3=new String[11];
        String[] ship4=new String[11];

        try {
            sCursor1.moveToFirst();

            while (!sCursor1.isAfterLast()) {

                OwnShip ownShip=new OwnShip();
                ownShip.setmId(sCursor1.getInt(getColumnIndex(Cols.ID)));
                ownShip.setmIndexA(sCursor1.getInt(getColumnIndex(Cols.CHECK_ONE)));
                ownShip.setmIndexB(sCursor1.getInt(getColumnIndex(Cols.CHECK_TWO)));
                ownShip.setmIndexC(sCursor1.getInt(getColumnIndex(Cols.CHECK_THREE)));
                ownShip.setmIndexD(sCursor1.getInt(getColumnIndex(Cols.CHECK_FOUR)));
                ownShip.setmIndexAState(sCursor1.getString(getColumnIndex(MachineShipsTable.Cols.INDEX_A_STATE)));
                ownShip.setmIndexBState(sCursor1.getString(getColumnIndex(MachineShipsTable.Cols.INDEX_B_STATE)));
                ownShip.setmIndexCState(sCursor1.getString(getColumnIndex(MachineShipsTable.Cols.INDEX_C_STATE)));
                ownShip.setmIndexDState(sCursor1.getString(getColumnIndex(MachineShipsTable.Cols.INDEX_D_STATE)));
                ownShip.setmColor(sCursor1.getInt(getColumnIndex(MachineShipsTable.Cols.COLOR)));
                ownShip.setmState(sCursor1.getString(getColumnIndex(MachineShipsTable.Cols.STATE)));
                sOwnShipList.add(ownShip);
                sCursor1.moveToNext();

            }
        }
        finally
        {
            sCursor1.close();
        }

        return sOwnShipList;
    }
    public List<Button> getShips(Cursor cursor,int size)
    {
        mCursor=cursor;//copy here cos it can be NULL if restart game after very long time. This will give new values

        for (int i=0;i<size;i++)
        {
            buttonList.add(new Button(mContext));
            buttonList.get(i).setText(String.format("%s",i+1));
        }

        String[] ship1=new String[11];
        String[] ship2=new String[11];
        String[] ship3=new String[11];
        String[] ship4=new String[11];

        try
        {
            mCursor.moveToFirst();

            //ship One
                ship1[0] = mCursor.getString(mCursor.getColumnIndex(Cols.ID));
                ship1[1] = mCursor.getString(mCursor.getColumnIndex(Cols.CHECK_ONE));
                ship1[2] = mCursor.getString(mCursor.getColumnIndex(Cols.CHECK_TWO));
                ship1[3] = mCursor.getString(mCursor.getColumnIndex(Cols.CHECK_THREE));
                ship1[4] = mCursor.getString(mCursor.getColumnIndex(Cols.CHECK_FOUR));
                ship1[5] = mCursor.getString(mCursor.getColumnIndex(Cols.INDEX_A_STATE));
                ship1[6] = mCursor.getString(mCursor.getColumnIndex(Cols.INDEX_B_STATE));
                ship1[7] = mCursor.getString(mCursor.getColumnIndex(Cols.INDEX_C_STATE));
                ship1[8] = mCursor.getString(mCursor.getColumnIndex(Cols.INDEX_D_STATE));
                ship1[9] = mCursor.getString(mCursor.getColumnIndex(Cols.STATE));
                ship1[10] = mCursor.getString(mCursor.getColumnIndex(Cols.COLOR));
            mCursor.moveToNext();

            //ship Two

                ship2[0] = mCursor.getString(mCursor.getColumnIndex(Cols.ID));
                ship2[1] = mCursor.getString(mCursor.getColumnIndex(Cols.CHECK_ONE));
                ship2[2] = mCursor.getString(mCursor.getColumnIndex(Cols.CHECK_TWO));
                ship2[3] = mCursor.getString(mCursor.getColumnIndex(Cols.CHECK_THREE));
                ship2[4] = mCursor.getString(mCursor.getColumnIndex(Cols.CHECK_FOUR));
                ship2[5] = mCursor.getString(mCursor.getColumnIndex(Cols.INDEX_A_STATE));
                ship2[6] = mCursor.getString(mCursor.getColumnIndex(Cols.INDEX_B_STATE));
                ship2[7] = mCursor.getString(mCursor.getColumnIndex(Cols.INDEX_C_STATE));
                ship2[8] = mCursor.getString(mCursor.getColumnIndex(Cols.INDEX_D_STATE));
                ship2[9] = mCursor.getString(mCursor.getColumnIndex(Cols.STATE));
                ship2[10] = mCursor.getString(mCursor.getColumnIndex(Cols.COLOR));
            mCursor.moveToNext();

            //ship Three
                ship3[0] = mCursor.getString(mCursor.getColumnIndex(Cols.ID));
                ship3[1] = mCursor.getString(mCursor.getColumnIndex(Cols.CHECK_ONE));
                ship3[2] = mCursor.getString(mCursor.getColumnIndex(Cols.CHECK_TWO));
                ship3[3] = mCursor.getString(mCursor.getColumnIndex(Cols.CHECK_THREE));
                ship3[4] = mCursor.getString(mCursor.getColumnIndex(Cols.CHECK_FOUR));
                ship3[5] = mCursor.getString(mCursor.getColumnIndex(Cols.INDEX_A_STATE));
                ship3[6] = mCursor.getString(mCursor.getColumnIndex(Cols.INDEX_B_STATE));
                ship3[7] = mCursor.getString(mCursor.getColumnIndex(Cols.INDEX_C_STATE));
                ship3[8] = mCursor.getString(mCursor.getColumnIndex(Cols.INDEX_D_STATE));
                ship3[9] = mCursor.getString(mCursor.getColumnIndex(Cols.STATE));
                ship3[10] = mCursor.getString(mCursor.getColumnIndex(Cols.COLOR));
            mCursor.moveToNext();

            //ship Four
                ship4[0] = mCursor.getString(mCursor.getColumnIndex(Cols.ID));
                ship4[1] = mCursor.getString(mCursor.getColumnIndex(Cols.CHECK_ONE));
                ship4[2] = mCursor.getString(mCursor.getColumnIndex(Cols.CHECK_TWO));
                ship4[3] = mCursor.getString(mCursor.getColumnIndex(Cols.CHECK_THREE));
                ship4[4] = mCursor.getString(mCursor.getColumnIndex(Cols.CHECK_FOUR));
                ship4[5] = mCursor.getString(mCursor.getColumnIndex(Cols.INDEX_A_STATE));
                ship4[6] = mCursor.getString(mCursor.getColumnIndex(Cols.INDEX_B_STATE));
                ship4[7] = mCursor.getString(mCursor.getColumnIndex(Cols.INDEX_C_STATE));
                ship4[8] = mCursor.getString(mCursor.getColumnIndex(Cols.INDEX_D_STATE));
                ship4[9] = mCursor.getString(mCursor.getColumnIndex(Cols.STATE));
                ship4[10] = mCursor.getString(mCursor.getColumnIndex(Cols.COLOR));
            mCursor.moveToNext();
        }
        finally
        {
            mCursor.close();
        }

        doTheView(mView,listOfShips,buttonList,ship1,ship2,ship3,ship4);//main view, main buttons and this class buttons to replace the main ones
        return buttonList;
    }


    private void doTheView(View view, List<Button> listOfShips, List<Button> buttonList,String[] ship1,String[] ship2, String[] ship3, String[] ship4) {

        for (int i=1;i<ship1.length-6;i++)
        {
            //color fix
            int tempColor=Integer.valueOf(ship1[ship1.length-1]);//Long.parseLong(ship1[ship1.length-1]);
            if (!ship1[i].equals("-1"))
            {
                int value=Integer.valueOf(ship1[i]);

                listOfShips.get(value).setText(String.format("%s",value+1));

                listOfShips.get(value).setBackgroundColor(mContext.getResources().getColor(getColor(tempColor)));
            }
        }

        for (int i=1;i<ship2.length-6;i++)
        {
            //color fix
            int tempColor=Integer.valueOf(ship2[ship2.length-1]);//Long.parseLong(ship1[ship1.length-1]);
            if (!ship2[i].equals("-1"))
            {
                int value=Integer.valueOf(ship2[i]);
                listOfShips.get(value).setText(String.format("%s",value+1));
                listOfShips.get(value).setBackgroundColor(mContext.getResources().getColor(getColor(tempColor)));
            }
        }

        for (int i=1;i<ship3.length-6;i++)
        {
            //color fix
            int tempColor=Integer.valueOf(ship3[ship3.length-1]);//Long.parseLong(ship1[ship1.length-1]);
            if (!ship3[i].equals("-1"))
            {
                int value=Integer.valueOf(ship3[i]);
                listOfShips.get(value).setText(String.format("%s",value+1));
                listOfShips.get(value).setBackgroundColor(mContext.getResources().getColor(getColor(tempColor)));
            }
        }

        for (int i=1;i<ship4.length-6;i++)
        {
            //color fix
            int tempColor=Integer.valueOf(ship4[ship4.length-1]);//Long.parseLong(ship1[ship1.length-1]);
            if (!ship4[i].equals("-1"))
            {
                int value=Integer.valueOf(ship4[i]);
                listOfShips.get(value).setText(String.format("%s",value+1));
                listOfShips.get(value).setBackgroundColor(mContext.getResources().getColor(getColor(tempColor)));
            }
        }
    }

    private int getColor(int code)
    {
        int color=code;
        switch (color)
        {
            case -16777216:
                color= R.color.black;
                break;
            case -256:
                color= R.color.yellow;
                break;
            case -16776961:
                color= R.color.blue;
                break;
            case -65536:
                color= R.color.red;
                break;
        }
        return color;
    }
}
