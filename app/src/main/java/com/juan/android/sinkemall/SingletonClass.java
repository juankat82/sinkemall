package com.juan.android.sinkemall;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.juan.android.database.DataBaseHelper;
import com.juan.android.database.GameOnWrapper;
import com.juan.android.database.HeldGame;
import com.juan.android.database.MachineShipsHelper;
import com.juan.android.database.MachineShipsTable;
import com.juan.android.database.MachineShipsWrapper;
import com.juan.android.database.MissedHit;
import com.juan.android.database.MissedHitComHelper;
import com.juan.android.database.MissedHitComWrapper;
import com.juan.android.database.MissedHitYouHelper;
import com.juan.android.database.MissedHitYouWrapper;
import com.juan.android.database.OwnSunkShips;
import com.juan.android.database.OwnSunkShipsHelper;
import com.juan.android.database.OwnSunkShipsWrapper;
import com.juan.android.database.RankCursorWrapper;
import com.juan.android.database.Records.RecordDataBase;
import com.juan.android.database.SQLGameCheckerOpenHelper;
import com.juan.android.database.ShipWrapper;
import com.juan.android.database.Ships;
import com.juan.android.database.ShipsHelper;
import com.juan.android.database.TurnDatabase;
import com.juan.android.database.TurnHelper;
import com.juan.android.database.TurnWrapper;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import static com.juan.android.database.HeldGame.Cols.GAME_IS_HELD;
import static com.juan.android.database.HeldGame.HELD_GAME_NAME;
import static com.juan.android.database.MachineShipsTable.Cols.COLOR;
import static com.juan.android.database.MachineShipsTable.Cols.COMP_SHIP_ID;
import static com.juan.android.database.MachineShipsTable.Cols.INDEX_A_NUMBER;
import static com.juan.android.database.MachineShipsTable.Cols.INDEX_A_STATE;
import static com.juan.android.database.MachineShipsTable.Cols.INDEX_B_NUMBER;
import static com.juan.android.database.MachineShipsTable.Cols.INDEX_B_STATE;
import static com.juan.android.database.MachineShipsTable.Cols.INDEX_C_NUMBER;
import static com.juan.android.database.MachineShipsTable.Cols.INDEX_C_STATE;
import static com.juan.android.database.MachineShipsTable.Cols.INDEX_D_NUMBER;
import static com.juan.android.database.MachineShipsTable.Cols.INDEX_D_STATE;
import static com.juan.android.database.MachineShipsTable.Cols.STATE;

/**
 * Created by juan on 12/11/17.
 */

public class SingletonClass {
    public static final String TAG="Singleton";
    private static SingletonClass sSingletonClass;
    public static List<ShipCoord> sShipList;
    private static List<MachineShip> sMachineShipList;
    private static Context mContext;
    private static SQLiteDatabase mRecordsDataBase;
    private static SQLiteDatabase mGameCheckIsOnDatabase;
    private static SQLiteDatabase mShipDataBase;
    private static SQLiteDatabase mMachineShipsDatabase;
    private static SQLiteDatabase mTurnDatabase;
    private static SQLiteDatabase mMissedHitCom;
    private static SQLiteDatabase mMissedHitYou;
    private static SQLiteDatabase mOwnSunkShipsDatabase;

    static int times=0;
    private static boolean sComeBack=false;
    private static boolean canStart=false;
    private static float mDpiMultiplier;
    private static int sMachineOneOk=0;//means not ready, repeat process til ship can be deployed
    private static int sMachineTwoOk=0;//means not ready, 1 means ready;
    private static int sMachineThreeOk=0;
    private static Turn sTurn;
    private static boolean isGameHeld;
    private static boolean sAudioSetting;
    private static boolean sVideoSetting;
    private static boolean sShowAbout;
    private static int sMachineSunkShips=0;
    private static int sOwnSunkShips=0;
    private static List<Ranks> sRanks;
    private static List<Integer> sMachineFailures;
    private static List<Integer> sOwnFailures;

    public static SingletonClass get(Context context)
    {
        if (sSingletonClass==null)
            sSingletonClass=new SingletonClass(context);
        return sSingletonClass;
    }
    public SingletonClass(Context context)
    {
        if (sOwnFailures==null)
            sOwnFailures=new ArrayList<>();
        if (sMachineFailures==null)
            sMachineFailures=new ArrayList<>();

        mContext=context.getApplicationContext();
        sShipList=new ArrayList<>();
        mRecordsDataBase=new DataBaseHelper(mContext).getWritableDatabase();//THIS IS THE ONE FOR RECORDS

        mGameCheckIsOnDatabase=new SQLGameCheckerOpenHelper(mContext).getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(GAME_IS_HELD,false);
        mGameCheckIsOnDatabase.insert(HELD_GAME_NAME,null,cv);

        mOwnSunkShipsDatabase=new OwnSunkShipsHelper(mContext).getWritableDatabase();
        ContentValues cvOwnSunk=new ContentValues();
        cvOwnSunk.put(OwnSunkShips.Cols.OWN_SUNK_SHIPS,0);
        mOwnSunkShipsDatabase.insert(OwnSunkShips.TABLE_NAME,null,cvOwnSunk);

        mShipDataBase=new ShipsHelper(mContext).getWritableDatabase();
        mMachineShipsDatabase=new MachineShipsHelper(mContext).getWritableDatabase();
        mTurnDatabase=new TurnHelper(mContext).getWritableDatabase();
        mMissedHitCom=new MissedHitComHelper(mContext).getWritableDatabase();
        mMissedHitYou=new MissedHitYouHelper(mContext).getWritableDatabase();
        sMachineShipList=new ArrayList<>();

        //Consult database first, get cursor, if not null, check that there is at least one NOT MACHINE DEFAULT, if so, retrieve them, otherwise
        //start from scratch
        sRanks=queryRecords(null,null);
        if (sRanks.isEmpty() || sRanks==null)
        {
            Log.e(TAG,"its null or empty");
            List<ContentValues> temp=startDefaults();
            insertList(temp);
        }
        sRanks=queryRecords(null,null);//NOW ITS FUCKING FULL!!!!!
        sMachineShipList=retrieveMachineShips();
    }
    public static void setDp(float dp)
    {
        mDpiMultiplier=dp;
    }

    public SQLiteDatabase getmSQLiteDatabase()
    {
        return mRecordsDataBase;
    }

    public static boolean getComeBack()
    {
        return sComeBack;
    }

    public static void setComeBack(boolean comeBack)
    {
        sComeBack=comeBack;
    }

    //creates default values
    public List<ContentValues> startDefaults()
    {
        List<Ranks> defaultRanks=new ArrayList<>();//base list
        List<ContentValues> contentValuesList=new ArrayList<>();

        for (int i=1;i<=5;i++)
        {
            Ranks rank=new Ranks();
            rank.setId(i);
            int newChar=65+i-1;
            char[] c={(char)newChar,(char)newChar,(char) newChar};
            String name=new String(c);
            rank.setName(name);
            rank.setPoints(110000-i*10000);
            rank.setMachineDefault(true);
            defaultRanks.add(rank);
        }

        for (Ranks r : defaultRanks)
        {
            ContentValues contentValues=new ContentValues();
            int id=r.getId();
            contentValues.put(RecordDataBase.Cols.POSITION,id);
            String name=r.getName();
            contentValues.put(RecordDataBase.Cols.PLAYER_NAME,name);
            long points=r.getPoints();
            contentValues.put(RecordDataBase.Cols.POINTS,points);
            boolean machDef=r.isMachineDefault();
            contentValues.put(RecordDataBase.Cols.MACHINE_DEFAULT,machDef);
            contentValuesList.add(contentValues);
        }
        return contentValuesList;
    }
    public void insertList(List<ContentValues> valueList)
    {
        for (ContentValues value : valueList)
        {
            mRecordsDataBase.insert(RecordDataBase.NAME,null,value);//needs name, null values hack,content value
        }
    }

    public static void setNewRecord(Ranks rank)
    {
        int indexRequired=rank.getId();
        List<Ranks> tempRankList=new ArrayList<>();
        List<ContentValues> recordListValues=new ArrayList<>();

        if (sRanks.isEmpty() || sRanks==null)//check ranks VARIABLE is set (NOT THE DATABASE)
            sRanks=queryRecords(null,null);//IF not set, query the database
        else//calculates the rest of the list
        {
            for (int i=0;i<sRanks.size();i++)
            {
                if (sRanks.get(i).getId()<indexRequired)
                    tempRankList.add(sRanks.get(i));
                if (sRanks.get(i).getId()==indexRequired)
                    tempRankList.add(rank);
                if (sRanks.get(i).getId()>indexRequired)
                {
                    sRanks.get(i-1).setId(i+1);
                    tempRankList.add(sRanks.get(i-1));
                }
            }
            sRanks=null;//empties the record variable
            sRanks=tempRankList;//fills up the sRanks (ranking list vble) with updated data
            mRecordsDataBase.execSQL("DELETE FROM "+ RecordDataBase.NAME); //wipe the records database

            for (int i=0;i<sRanks.size();i++)
            {
                ContentValues cv=new ContentValues();
                cv.put(RecordDataBase.Cols.POSITION,sRanks.get(i).getId());
                cv.put(RecordDataBase.Cols.PLAYER_NAME,sRanks.get(i).getName());
                cv.put(RecordDataBase.Cols.POINTS,sRanks.get(i).getPoints());
                cv.put(RecordDataBase.Cols.MACHINE_DEFAULT,sRanks.get(i).isMachineDefault());
                recordListValues.add(cv);
            }
            //inserts the new records in the database
            for (int i=0;i<recordListValues.size();i++)
                mRecordsDataBase.insert(RecordDataBase.NAME,null,recordListValues.get(i));
        }
    }
    public static List<Ranks> queryRecords(String whereClause,String[] whereArgs)
    {
        List<Ranks> mRanksList;
        Cursor cursor=mRecordsDataBase.query(RecordDataBase.NAME,null,whereClause,whereArgs,null,null,null);
        RankCursorWrapper wrapper =new RankCursorWrapper(cursor,mContext);
        mRanksList=wrapper.getRanks(cursor);
        return mRanksList;
    }
    public static boolean isGameStarted()
    {
            Cursor cursor=mGameCheckIsOnDatabase.rawQuery("SELECT * FROM '"+HeldGame.HELD_GAME_NAME+"'",null);
            GameOnWrapper gOW=new GameOnWrapper(mContext,cursor);

            isGameHeld=gOW.isGameOn(cursor);
                Log.e(TAG,"Is Game On: "+isGameHeld);
        return isGameHeld;
    }

    ////////////////////////////////USED WHEN WE START A NEW GAME/////////////////
    public static void startNewGame()
    {
        ContentValues cvGameOn=new ContentValues();
        cvGameOn.put(GAME_IS_HELD,false);
        ContentValues cvOwnSunk=new ContentValues();
        cvOwnSunk.put(OwnSunkShips.Cols.OWN_SUNK_SHIPS,0);

        mGameCheckIsOnDatabase.execSQL("DELETE FROM "+HELD_GAME_NAME);
        mGameCheckIsOnDatabase.insert(HELD_GAME_NAME,null,cvGameOn);
        mOwnSunkShipsDatabase.execSQL("DELETE FROM "+OwnSunkShips.TABLE_NAME);
        mOwnSunkShipsDatabase.insert(OwnSunkShips.TABLE_NAME,null,cvOwnSunk);
        mMissedHitCom.delete(MissedHit.MISSED_HIT_TABLE,null,null);
        mMissedHitYou.delete(MissedHit.MISSED_HIT_TABLE,null,null);
        mShipDataBase.execSQL("DELETE FROM "+Ships.ShipTable.NAME);
        sShipList=null;
        isGameHeld=false;
        sMachineSunkShips=0;
        sOwnSunkShips=0;
        sShipList=new ArrayList<>();
        mRecordsDataBase=new DataBaseHelper(mContext).getWritableDatabase();
        mGameCheckIsOnDatabase=new SQLGameCheckerOpenHelper(mContext).getWritableDatabase();
        mShipDataBase=new ShipsHelper(mContext).getWritableDatabase();;
        mMachineShipsDatabase=new MachineShipsHelper(mContext).getWritableDatabase();
        mTurnDatabase=new TurnHelper(mContext).getWritableDatabase();
        times=0;
        sComeBack=false;
        canStart=false;
        sMachineOneOk=0;//means not ready, repeat process til ship can be deployed
        sMachineTwoOk=0;//means not ready, 1 means ready;
        sMachineThreeOk=0;
        if (sMachineShipList!=null)
            sMachineShipList.clear();
        sMachineShipList=new ArrayList<>();
        sMachineFailures=null;
        sMachineFailures=new ArrayList<>();
        sOwnFailures=null;
        sOwnFailures=new ArrayList<>();
        startMachineSettings();
        resetTurnsToZero();
        modifyTurnsDatabase(0,0,0);
        sTurn=readTurnDatabase();
    }

    public static void startOwnDefaults()
    {
        List<ContentValues> cvs=new ArrayList<>();
        Cursor cur=mShipDataBase.rawQuery("SELECT * FROM "+Ships.ShipTable.NAME,null);
        ShipWrapper sW=new ShipWrapper(mContext,cur);
        List<OwnShip> ls=sW.getOwnShips(cur);

        for (int i=0;i<ls.size();i++)
        {
            ContentValues cv=new ContentValues();
            cv.put(Ships.ShipTable.Cols.ID,ls.get(i).getmId());
            cv.put(Ships.ShipTable.Cols.CHECK_ONE,ls.get(i).getmIndexA());
            cv.put(Ships.ShipTable.Cols.CHECK_TWO,ls.get(i).getmIndexB());
            cv.put(Ships.ShipTable.Cols.CHECK_THREE,ls.get(i).getmIndexC());
            cv.put(Ships.ShipTable.Cols.CHECK_FOUR,ls.get(i).getmIndexD());
            cv.put(Ships.ShipTable.Cols.INDEX_A_STATE,ls.get(i).getmIndexAState());
            cv.put(Ships.ShipTable.Cols.INDEX_B_STATE,ls.get(i).getmIndexBState());
            cv.put(Ships.ShipTable.Cols.INDEX_C_STATE,ls.get(i).getmIndexCState());
            cv.put(Ships.ShipTable.Cols.INDEX_D_STATE,ls.get(i).getmIndexDState());
            cv.put(Ships.ShipTable.Cols.STATE,"up");
            cv.put(Ships.ShipTable.Cols.COLOR,ls.get(i).getmColor());
            cvs.add(cv);
        }

        mShipDataBase.delete(Ships.ShipTable.NAME,null,null);

        for (int i=0;i<cvs.size();i++)
            mShipDataBase.insert(Ships.ShipTable.NAME,null,cvs.get(i));
    }
    public static void startMachineSettings() ////CHECK THE FOR LOOP TILL MMACHINESHIPDATABASE AND UNCOMMENT IF NEEDED
    {
        ContentValues contentValues=new ContentValues();
        int n=-1;
        int verticalOrHorizontal=0;//0 means horizontal and 1 means vertical
        mMachineShipsDatabase.execSQL("DELETE FROM "+ MachineShipsTable.MACHINE_SHIPS_TABLE_NAME);
        sOwnSunkShips=0;
        //creates virtual ships for the machine
        SecureRandom random=new SecureRandom();
        SecureRandom col=new SecureRandom();
        SecureRandom row=new SecureRandom();
        //ship one
        int shipOne=random.nextInt(64);
        //ship two
        verticalOrHorizontal=random.nextInt()%2;//will  be 0 or 1 now to decide how it will be, either H or V
        int[] shipTwo=new int[2];
        while (sMachineOneOk==0)//if called method doesnt set sMchineOk to 1, it will be called continuously as it understand the ship hasnt been set
            shipTwo=calculateShipTwo(verticalOrHorizontal,shipOne,col,row);
        //ship three
        verticalOrHorizontal=random.nextInt()%2;
        int[] shipThree=new int[3];
        while (sMachineTwoOk==0)
            shipThree=calculateShipThree(verticalOrHorizontal,shipOne,shipTwo,col,row);
        //ship four
        verticalOrHorizontal=random.nextInt()%2;
        int[] shipFour=new int[4];
        while (sMachineThreeOk==0)
            shipFour=calculateShipFour(verticalOrHorizontal,shipOne,shipTwo,shipThree,col,row);

        //insert registries in the database
        //shipOne
        ContentValues cv1=new ContentValues();
        cv1.put(COMP_SHIP_ID,"shipCompOne");
        cv1.put(INDEX_A_NUMBER,String.valueOf(shipOne));
        cv1.put(INDEX_B_NUMBER,"-1");
        cv1.put(INDEX_C_NUMBER,"-1");
        cv1.put(INDEX_D_NUMBER,"-1");
        cv1.put(INDEX_A_STATE,"X");
        cv1.put(INDEX_B_STATE,"NA");
        cv1.put(INDEX_C_STATE,"NA");
        cv1.put(INDEX_D_STATE,"NA");
        cv1.put(COLOR,"black");
        cv1.put(STATE,"up");
        //ship two
        ContentValues cv2=new ContentValues();
        cv2.put(COMP_SHIP_ID,"shipCompTwo");
        cv2.put(INDEX_A_NUMBER,String.valueOf(shipTwo[0]));
        cv2.put(INDEX_B_NUMBER,String.valueOf(shipTwo[1]));
        cv2.put(INDEX_C_NUMBER,"-1");
        cv2.put(INDEX_D_NUMBER,"-1");
        cv2.put(INDEX_A_STATE,"X");
        cv2.put(INDEX_B_STATE,"X");
        cv2.put(INDEX_C_STATE,"NA");
        cv2.put(INDEX_D_STATE,"NA");
        cv2.put(COLOR,"yellow");
        cv2.put(STATE,"up");
        //ship three
        ContentValues cv3=new ContentValues();
        cv3.put(COMP_SHIP_ID,"shipCompThree");
        cv3.put(INDEX_A_NUMBER,String.valueOf(shipThree[0]));
        cv3.put(INDEX_B_NUMBER,String.valueOf(shipThree[1]));
        cv3.put(INDEX_C_NUMBER,String.valueOf(shipThree[2]));
        cv3.put(INDEX_D_NUMBER,"-1");
        cv3.put(INDEX_A_STATE,"X");
        cv3.put(INDEX_B_STATE,"X");
        cv3.put(INDEX_C_STATE,"X");
        cv3.put(INDEX_D_STATE,"NA");
        cv3.put(COLOR,"blue");
        cv3.put(STATE,"up");
        //ship four
        ContentValues cv4=new ContentValues();
        cv4.put(COMP_SHIP_ID,"shipCompFour");
        cv4.put(INDEX_A_NUMBER,String.valueOf(shipFour[0]));
        cv4.put(INDEX_B_NUMBER,String.valueOf(shipFour[1]));
        cv4.put(INDEX_C_NUMBER,String.valueOf(shipFour[2]));
        cv4.put(INDEX_D_NUMBER,String.valueOf(shipFour[3]));
        cv4.put(INDEX_A_STATE,"X");
        cv4.put(INDEX_B_STATE,"X");
        cv4.put(INDEX_C_STATE,"X");
        cv4.put(INDEX_D_STATE,"X");
        cv4.put(COLOR,"red");
        cv4.put(STATE,"up");
        //insert content values in registry
        mMachineShipsDatabase.insert(MachineShipsTable.MACHINE_SHIPS_TABLE_NAME,null,cv1);
        mMachineShipsDatabase.insert(MachineShipsTable.MACHINE_SHIPS_TABLE_NAME,null,cv2);
        mMachineShipsDatabase.insert(MachineShipsTable.MACHINE_SHIPS_TABLE_NAME,null,cv3);
        mMachineShipsDatabase.insert(MachineShipsTable.MACHINE_SHIPS_TABLE_NAME,null,cv4);
        //assign data to an arraylist
        setMachineShips(shipOne,shipTwo,shipThree,shipFour);
        sMachineSunkShips=0;
        sOwnSunkShips=0;
    }

    //Not to use with database, this is meant to be used when databse is set
    public static void setMachineShips(int shipOne, int[] shipTwo, int[] shipThree, int[] shipFour) {

        MachineShip mOne=new MachineShip();
        mOne.setmId("shipCompOne");
        mOne.setmIndexA(shipOne);
        mOne.setmIndexB(-1);
        mOne.setmIndexC(-1);
        mOne.setmIndexD(-1);
        mOne.setmIndexAState("X");
        mOne.setmIndexBState("X");
        mOne.setmIndexCState("X");
        mOne.setmIndexDState("X");
        mOne.setmColor("black");
        mOne.setmState("up");

        MachineShip mTwo=new MachineShip();
        mTwo.setmId("shipCompTwo");
        mTwo.setmIndexA(shipTwo[0]);
        mTwo.setmIndexB(shipTwo[1]);
        mTwo.setmIndexC(-1);
        mTwo.setmIndexD(-1);
        mTwo.setmIndexAState("X");
        mTwo.setmIndexBState("X");
        mTwo.setmIndexCState("NA");
        mTwo.setmIndexDState("NA");
        mTwo.setmColor("yellow");
        mTwo.setmState("up");

        MachineShip mThree=new MachineShip();
        mThree.setmId("shipCompThree");
        mThree.setmIndexA(shipThree[0]);
        mThree.setmIndexB(shipThree[1]);
        mThree.setmIndexC(shipThree[2]);
        mThree.setmIndexD(-1);
        mThree.setmIndexAState("X");
        mThree.setmIndexBState("X");
        mThree.setmIndexCState("X");
        mThree.setmIndexDState("X");
        mThree.setmColor("blue");
        mThree.setmState("up");

        MachineShip mFour=new MachineShip();
        mFour.setmId("shipCompFour");
        mFour.setmIndexA(shipFour[0]);
        mFour.setmIndexB(shipFour[1]);
        mFour.setmIndexC(shipFour[2]);
        mFour.setmIndexD(shipFour[3]);
        mFour.setmIndexAState("X");
        mFour.setmIndexBState("X");
        mFour.setmIndexCState("NA");
        mFour.setmIndexDState("NA");
        mFour.setmColor("red");
        mFour.setmState("up");

        sMachineShipList.add(mOne);
        sMachineShipList.add(mTwo);
        sMachineShipList.add(mThree);
        sMachineShipList.add(mFour);
    }
    public static void setShips(ShipCoord ship)
    {
        if (sShipList==null)
            sShipList=new ArrayList<>();
        sShipList.add(ship);
    }
    public static void setGameHeld(boolean setHeldOn)
    {
        mGameCheckIsOnDatabase.execSQL("DELETE FROM "+ HELD_GAME_NAME);
        ContentValues cv=new ContentValues();
        cv.put(GAME_IS_HELD,setHeldOn);//check it if fails and pass a STRING??
        Log.e(TAG,"SetHeldOn: "+setHeldOn);
        mGameCheckIsOnDatabase.insert(HELD_GAME_NAME,null,cv);
        isGameHeld=setHeldOn;
    }

    public static void setVideoOff(boolean videoSetting)
    {
        sVideoSetting=videoSetting;
    }
    public static boolean isVideoOff()
    {
        return sVideoSetting;
    }
    public static void setAudioOff(boolean audioSetting)
    {
        sAudioSetting=audioSetting;
    }
    public static void showAbout(boolean show)
    {
        sShowAbout=show;
    }
    public static boolean isShowAbout()
    {
        return sShowAbout;
    }
    public static boolean isAudioOff()
    {
        return sAudioSetting;
    }

    public static void setShipListToZero()
    {

        if (mShipDataBase!=null)
            mShipDataBase.execSQL("DELETE FROM "+Ships.ShipTable.NAME);

        //CHECK THIS OUT, LIKELY TO BE ERASABLE 99%
        boolean value=false;
        if (mGameCheckIsOnDatabase==null)
            mGameCheckIsOnDatabase=new SQLGameCheckerOpenHelper(mContext).getWritableDatabase();
        else
            mGameCheckIsOnDatabase.execSQL("DELETE FROM "+HELD_GAME_NAME);
        ContentValues cv=new ContentValues();
        cv.put(GAME_IS_HELD,value);
        mGameCheckIsOnDatabase.insert(HELD_GAME_NAME,null,cv);
    }
    public static List<ShipCoord> getShipList()
    {
        return sShipList;
    }

        //FIRST TIME ONLY
    public static void setShipsOnDatabase(List<Integer> ships,int option)//option 0 will take the ELSE side, means new game
    {
        List<Integer> shipList=ships;

        int counter=0;
        //Integers are: shipId, button1, button2, button3 and button4 when available,Color code
        ContentValues cv1;//=new ContentValues();
        ContentValues cv2;//=new ContentValues();
        ContentValues cv3;//=new ContentValues();
        ContentValues cv4;//=new ContentValues();

        int cv1Size=0;//size of the registry of first ship
        int cv2Size=0;//second ...
        int cv3Size=0;
        int cv4Size=0;

        if (option==0)
        {
            for (int i = 0; i < shipList.size(); i++) {

                if (shipList.get(i) == R.id.ship_one) {
                    int from = i;
                    int til = i + 3;
                    cv1Size = 3;
                    cvSwitch(from, cv1Size, shipList);
                }
                if (shipList.get(i) == R.id.ship_two) {
                    int from = i;
                    cv2Size = 4;
                    cvSwitch(from, cv2Size, shipList);
                }
                if (shipList.get(i) == R.id.ship_two_b) {
                    int from = i;
                    cv2Size = 4;
                    cvSwitch(from, cv2Size, shipList);
                }
                if (shipList.get(i) == R.id.ship_three) {
                    int from = i;
                    cv3Size = 5;
                    cvSwitch(from, cv3Size, shipList);
                }
                if (shipList.get(i) == R.id.ship_three_b) {
                    int from = i;
                    cv3Size = 5;
                    cvSwitch(from, cv3Size, shipList);
                }
                if (shipList.get(i) == R.id.ship_four) {
                    int from = i;
                    cv4Size = 6;
                    cvSwitch(from, cv4Size, shipList);
                }
                if (shipList.get(i) == R.id.ship_four_b) {
                    int from = i;
                    cv4Size = 6;
                    cvSwitch(from, cv4Size, shipList);
                }
            }
        }
    }
    private static void cvSwitch(int from,int size,List<Integer> shipList)
    {
        int next=from;
        switch (size)
        {
            case 3:
                ContentValues contentValues1 = new ContentValues();
                contentValues1.put("ship_id", shipList.get(next));
                contentValues1.put("check_one", shipList.get(next + 1));
                contentValues1.put("check_two", -1);
                contentValues1.put("check_three", -1);
                contentValues1.put("check_four", -1);
                contentValues1.put("a_state","X");
                contentValues1.put("b_state","NA");
                contentValues1.put("c_state","NA");
                contentValues1.put("d_state","NA");
                contentValues1.put("state","up");
                contentValues1.put("color", shipList.get(next + 2));
                mShipDataBase.insert(Ships.ShipTable.NAME, null, contentValues1);
                Log.i(TAG, "CV: " + contentValues1.toString());
                break;
            case 4:
                ContentValues contentValues2 = new ContentValues();
                contentValues2.put("ship_id",shipList.get(next));
                contentValues2.put("check_one",shipList.get(next+1));
                contentValues2.put("check_two",shipList.get(next+2));
                contentValues2.put("check_three",-1);
                contentValues2.put("check_four",-1);
                contentValues2.put("a_state","X");
                contentValues2.put("b_state","X");
                contentValues2.put("c_state","NA");
                contentValues2.put("d_state","NA");
                contentValues2.put("state","up");
                contentValues2.put("color",shipList.get(next+3));
                Log.i(TAG,"CV: "+contentValues2.toString());
                mShipDataBase.insert(Ships.ShipTable.NAME,null,contentValues2);
                break;
            case 5:
                ContentValues contentValues3 = new ContentValues();
                contentValues3.put("ship_id",shipList.get(next));
                contentValues3.put("check_one",shipList.get(next+1));
                contentValues3.put("check_two",shipList.get(next+2));
                contentValues3.put("check_three",shipList.get(next+3));
                contentValues3.put("check_four",-1);
                contentValues3.put("a_state","X");
                contentValues3.put("b_state","X");
                contentValues3.put("c_state","X");
                contentValues3.put("d_state","NA");
                contentValues3.put("state","up");
                contentValues3.put("color",shipList.get(next+4));
                Log.i(TAG,"CV: "+contentValues3.toString());
                mShipDataBase.insert(Ships.ShipTable.NAME,null,contentValues3);
                break;
            case 6:
                ContentValues contentValues4 = new ContentValues();
                contentValues4.put("ship_id",shipList.get(next));
                contentValues4.put("check_one",shipList.get(next+1));
                contentValues4.put("check_two",shipList.get(next+2));
                contentValues4.put("check_three",shipList.get(next+3));
                contentValues4.put("check_four",shipList.get(next+4));
                contentValues4.put("a_state","X");
                contentValues4.put("b_state","X");
                contentValues4.put("c_state","X");
                contentValues4.put("d_state","X");
                contentValues4.put("state","up");
                contentValues4.put("color",shipList.get(next+5));
                Log.i(TAG,"CV: "+contentValues4.toString());
                mShipDataBase.insert(Ships.ShipTable.NAME,null,contentValues4);
                break;
        }
    }



    public static void resetTurnsToZero()
    {
        mTurnDatabase.execSQL("DELETE FROM "+TurnDatabase.TABLE);//wipes the table
        ContentValues cv=new ContentValues();
        cv.put(TurnDatabase.Cols.TURNS,0);
        cv.put(TurnDatabase.Cols.TIMES_HIT,0);
        cv.put(TurnDatabase.Cols.TIMES_HITTING,0);
        mTurnDatabase.insert(TurnDatabase.TABLE,null,cv);
    }
    private static void modifyTurnsDatabase(int turnsPassed,int timesHit,int timesHitting)//enter calues)
    {
        resetTurnsToZero();
        ContentValues contentValues=new ContentValues();

        contentValues.put("turns_passed",turnsPassed);
        contentValues.put("times_hit",timesHit);
        contentValues.put("times_hitting",timesHitting);
        mTurnDatabase.insert(TurnDatabase.TABLE,null,contentValues);
    }
    public static Turn readTurnDatabase()
    {
        Cursor cursor=mTurnDatabase.rawQuery("SELECT * FROM '"+TurnDatabase.TABLE+"'",null);
        TurnWrapper turnWrapper=new TurnWrapper(cursor,mContext);
        sTurn= turnWrapper.getTurnData(cursor);
        return sTurn;
    }



    public static List<Button> getShipsFromDatabase(String whereClause,String[] whereArgs,int size,View view,List<Button> buttonList)
    {

        List<Button> ships;
        Cursor cursor=mShipDataBase.rawQuery("SELECT * FROM "+Ships.ShipTable.NAME,null);
        ShipWrapper shipWrapper=new ShipWrapper(cursor,mContext,view,buttonList);
        if (cursor.getCount()>0)
        {
            ships=shipWrapper.getShips(cursor,size);
            canStart=true;
            return ships;
        }
        else
        {
            return ships=new ArrayList<>();
        }
    }
    public static void setOwnShips(List<OwnShip> ownShips)
    {
        List<ContentValues> cvs=new ArrayList<>();
        cvs.clear();
        mShipDataBase.execSQL("DELETE FROM "+Ships.ShipTable.NAME);
        for (int i=0;i<ownShips.size();i++)
        {
            ContentValues cv=new ContentValues();
            cv.put(Ships.ShipTable.Cols.ID,ownShips.get(i).getmId());
            cv.put(Ships.ShipTable.Cols.CHECK_ONE,ownShips.get(i).getmIndexA());
            cv.put(Ships.ShipTable.Cols.CHECK_TWO,ownShips.get(i).getmIndexB());
            cv.put(Ships.ShipTable.Cols.CHECK_THREE,ownShips.get(i).getmIndexC());
            cv.put(Ships.ShipTable.Cols.CHECK_FOUR,ownShips.get(i).getmIndexD());
            cv.put(Ships.ShipTable.Cols.INDEX_A_STATE,ownShips.get(i).getmIndexAState());
            cv.put(Ships.ShipTable.Cols.INDEX_B_STATE,ownShips.get(i).getmIndexBState());
            cv.put(Ships.ShipTable.Cols.INDEX_C_STATE,ownShips.get(i).getmIndexCState());
            cv.put(Ships.ShipTable.Cols.INDEX_D_STATE,ownShips.get(i).getmIndexDState());
            cv.put(Ships.ShipTable.Cols.STATE,ownShips.get(i).getmState());
            cv.put(Ships.ShipTable.Cols.COLOR,ownShips.get(i).getmColor());
            cvs.add(cv);
        }
        for (int i=0;i<cvs.size();i++)
        {
            mShipDataBase.insert(Ships.ShipTable.NAME,null,cvs.get(i));
        }
    }
    public static List<OwnShip> getOwnShips()
    {
        Cursor cursor=mShipDataBase.rawQuery("SELECT * FROM '"+Ships.ShipTable.NAME+"'",null);
        ShipWrapper sW=new ShipWrapper(cursor,mContext,null,null);
        List<OwnShip> list=sW.getOwnShips(cursor);
        return list;
    }

    public static void writeTurnOnDatabase(Turn turn)
    {
        ContentValues cv=new ContentValues();//fills values for database
        cv.put(TurnDatabase.Cols.TURNS,turn.getmTurnNumber());
        cv.put(TurnDatabase.Cols.TIMES_HIT,turn.getmTimesHit());
        cv.put(TurnDatabase.Cols.TIMES_HITTING,turn.getmTimesHitting());
        cv.put(TurnDatabase.Cols.HITS_NEXT,turn.getWhoHitnext());
        mTurnDatabase.execSQL("DELETE FROM "+TurnDatabase.TABLE);//wipes turn table
        mTurnDatabase.insert(TurnDatabase.TABLE,null,cv);
    }
    public static int getMachineSunkShips()
    {
        return sMachineSunkShips;
    }

    public static void setMachineSunkShips(int num)//if -1 just increase in one
    {
        if (num==-1)
            sMachineSunkShips++;
        else
            sMachineSunkShips=num;
    }
    public static void setOwnSunkShips(int num)//if -1 just increase in one
    {
        if (num==-1)
            sOwnSunkShips++;
        else
            sOwnSunkShips=num;
    }
    public static void setOwnSunkShipsIntoDatabase(int sunkOnes)
    {
        ContentValues cv=new ContentValues();
        cv.put(OwnSunkShips.Cols.OWN_SUNK_SHIPS,sunkOnes);
        mOwnSunkShipsDatabase.execSQL("DELETE FROM "+OwnSunkShips.TABLE_NAME);
        mOwnSunkShipsDatabase.insert(OwnSunkShips.TABLE_NAME,null,cv);
    }
    public static int getOwnSunkShips()
    {
        Cursor cursor=mOwnSunkShipsDatabase.rawQuery("SELECT * FROM "+ OwnSunkShips.TABLE_NAME,null);
        OwnSunkShipsWrapper wrapper=new OwnSunkShipsWrapper(mContext,cursor);
        sOwnSunkShips=wrapper.getOwnSunkShips(cursor);
        return sOwnSunkShips;
    }

    public static List<MachineShip> retrieveMachineShips()//change void for a List
    {
        Cursor cursor=mMachineShipsDatabase.rawQuery("SELECT * FROM "+ MachineShipsTable.MACHINE_SHIPS_TABLE_NAME,null);
        MachineShipsWrapper machineShipsWrapper=new MachineShipsWrapper(cursor);

        if (sMachineShipList.isEmpty() || sMachineShipList==null)
        {
            sMachineShipList= machineShipsWrapper.getShips(cursor);
        }
        return sMachineShipList;
    }
    public static void setMachineShipsNonDefault(List<MachineShip> machineShipList)
    {
        List<ContentValues> cvList=new ArrayList<>();
        for (int i=0;i<machineShipList.size();i++)
        {
            ContentValues cv=new ContentValues();
            cv.put(MachineShipsTable.Cols.COMP_SHIP_ID,machineShipList.get(i).getmId());
            cv.put(MachineShipsTable.Cols.INDEX_A_NUMBER,machineShipList.get(i).getmIndexA());
            cv.put(MachineShipsTable.Cols.INDEX_B_NUMBER,machineShipList.get(i).getmIndexB());
            cv.put(MachineShipsTable.Cols.INDEX_C_NUMBER,machineShipList.get(i).getmIndexC());
            cv.put(MachineShipsTable.Cols.INDEX_D_NUMBER,machineShipList.get(i).getmIndexD());
            cv.put(MachineShipsTable.Cols.INDEX_A_STATE,machineShipList.get(i).getmIndexAState());
            cv.put(MachineShipsTable.Cols.INDEX_B_STATE,machineShipList.get(i).getmIndexBState());
            cv.put(MachineShipsTable.Cols.INDEX_C_STATE,machineShipList.get(i).getmIndexCState());
            cv.put(MachineShipsTable.Cols.INDEX_D_STATE,machineShipList.get(i).getmIndexDState());
            cv.put(MachineShipsTable.Cols.COLOR,machineShipList.get(i).getmColor());
            cv.put(MachineShipsTable.Cols.STATE,machineShipList.get(i).getmState());
            cvList.add(cv);

        }

        mMachineShipsDatabase.execSQL("DELETE FROM "+ MachineShipsTable.MACHINE_SHIPS_TABLE_NAME);
        for (int i=0;i<cvList.size();i++)
        {
            ContentValues cv=cvList.get(i);
            mMachineShipsDatabase.insert(MachineShipsTable.MACHINE_SHIPS_TABLE_NAME,null,cv);
        }

    }
    private static int[] calculateShipFour(int orientation, int one, int[] two, int[] three, SecureRandom col,SecureRandom row)
    {
        int shipFour[]=new int[4];
        int c;
        int r;
        boolean done=false;

        while (done!=true)
        {

            c=col.nextInt(4);
            r=row.nextInt(4);
            int temp=c+(r*8);

            if (orientation==0)
            {
                if (temp!=one && temp!=two[0] && temp!=two[1] && temp!=three[0] && temp!=three[1] && temp!=three[2])
                {
                    if (temp+1!=one && temp+1!=two[0] && temp+1!=two[1] && temp+1!=three[0] && temp+1!=three[1] && temp+1!=three[2])
                    {
                        if (temp+2!=one && temp+2!=two[0] && temp+2!=two[1] && temp+2!=three[0] && temp+2!=three[1] && temp+2!=three[2])
                        {
                            if (temp+3!=one && temp+3!=two[0] && temp+3!=two[1] && temp+3!=three[0] && temp+3!=three[1] && temp+3!=three[2])
                            {
                                shipFour[0]=temp;
                                shipFour[1]=temp+1;
                                shipFour[2]=temp+2;
                                shipFour[3]=temp+3;
                                done=true;
                            }
                        }
                    }
                }
            }
            else
            {
                if (temp!=one && temp!=two[0] && temp!=two[1] && temp!=three[0] && temp!=three[1] && temp!=three[2])
                {
                    if (temp+8!=one && temp+8!=two[0] && temp+8!=two[1] && temp+8!=three[0] && temp+8!=three[1] && temp+8!=three[2])
                    {
                        if (temp+16!=one && temp+16!=two[0] && temp+16!=two[1] && temp+16!=three[0] && temp+16!=three[1] && temp+16!=three[2])
                        {
                            if (temp+24!=one && temp+24!=two[0] && temp+24!=two[1] && temp+24!=three[0] && temp+24!=three[1] && temp+24!=three[2])
                            {
                                shipFour[0]=temp;
                                shipFour[1]=temp+8;
                                shipFour[2]=temp+16;
                                shipFour[3]=temp+24;
                                done=true;
                            }
                        }
                    }
                }
            }
        }
        sMachineThreeOk=1;
        return shipFour;

    }
    private static int[] calculateShipThree(int orientation,int one,int[] two,SecureRandom col,SecureRandom row)
    {
        int shipThree[]=new int[3];
        int c;
        int r;
        boolean done=false;

        while(done!=true)
        {
            c=col.nextInt(5);
            r=row.nextInt(5);
            int temp=c+(r*8);

            if (orientation==0)
            {
                if (temp!=one && temp!=two[0] && temp!=two[1])
                {
                    if (temp+1!= one && temp+1!= two[0] && temp+2!= two[1])
                    {
                        if (temp+2!= one && temp+2!= two[0] && temp+2!= two[1])
                        {
                            shipThree[0]=temp;
                            shipThree[1]=temp+1;
                            shipThree[2]=temp+2;
                            done=true;
                        }
                    }
                }
            }
            else
            {
                if (temp!=one && temp!=two[0] && temp!=two[1])
                {
                    if (temp+8!= one && temp+8!= two[0] && temp+8!= two[1])
                    {
                        if (temp+16!= one && temp+16!= two[0] && temp+16!= two[1])
                        {
                            shipThree[0]=temp;
                            shipThree[1]=temp+8;
                            shipThree[2]=temp+16;
                            done=true;
                        }
                    }
                }
            }
        }
        sMachineTwoOk=1;
        return shipThree;
    }
    private static int[] calculateShipTwo(int orientation, int one, SecureRandom col, SecureRandom row)
    {
        int shipTwo[]=new int[2];
        int c;
        int r;
        boolean done=false;

        while (done!=true)
        {
            c=col.nextInt(6);
            r=row.nextInt(6);
            int temp=c+(r*8);

            if (orientation==0)//horizontal
            {
                if (temp!=one && temp+1!=one)
                {
                    shipTwo[0]=temp;
                    shipTwo[1]=temp+1;
                    done=true;
                }
            }
            else
            {
                if (temp!=one && temp+8!=one)
                {
                    shipTwo[0]=temp;
                    shipTwo[1]=temp+8;
                    done=true;
                }
            }
        }
        sMachineOneOk=1;//allows the caller method to stop and store the ships data
        return shipTwo;
    }


    ////////////////////////////SETS FAILURES//////////////////////////

    public static void setMachineFailuresOnDatabase(List<Integer> miss)//int[] miss)
    {
        //SET FAILS INTO A DATABASE
        List<ContentValues> cvList=new ArrayList<>();

        //wipes database first then fill up
        mMissedHitCom.delete(MissedHit.MISSED_HIT_TABLE,null,null);

        for (int i=0;i<miss.size();i++)//length;i++)
        {
            ContentValues cv = new ContentValues();
            cv.put(MissedHit.Cols.INDEX, miss.get(i));//miss[i]);
            cvList.add(cv);
        }

        for (int i=0;i<cvList.size();i++)
            mMissedHitCom.insert(MissedHit.MISSED_HIT_TABLE,null,cvList.get(i));
    }
    public static void setOwnFailuresOnDatabase(List<Integer> miss)//int[] miss)
    {
        //SET FAILS INTO A DATABASE
        //wipes database first then fill up
        mMissedHitYou.execSQL("DELETE FROM "+MissedHit.MISSED_HIT_TABLE);
        List<ContentValues> cvList=new ArrayList<>();

        for (int i=0;i<miss.size();i++)//length;i++)
        {
            ContentValues cv = new ContentValues();
            cv.put(MissedHit.Cols.INDEX, miss.get(i));//miss[i]);
            cvList.add(cv);
        }

        for (ContentValues c : cvList)
            mMissedHitYou.insert(MissedHit.MISSED_HIT_TABLE,null,c);
    }
    public static List<Integer> getMachineFailures()//int[] getMachineFailures()
    {
        if (sMachineFailures==null || sMachineFailures.size()==0)
        {
            Cursor cursor=mMissedHitCom.rawQuery("SELECT * FROM "+MissedHit.MISSED_HIT_TABLE,null);
            MissedHitComWrapper wrapper=new MissedHitComWrapper(cursor);
            sMachineFailures= wrapper.getMachineMissedHitList(cursor);
        }
        return sMachineFailures;
    }
    public static List<Integer> getOwnFailures()
    {
        if (sOwnFailures==null || sOwnFailures.size()==0)
        {
            Cursor cursor=mMissedHitYou.rawQuery("SELECT * FROM "+MissedHit.MISSED_HIT_TABLE,null);
            MissedHitYouWrapper wrapper=new MissedHitYouWrapper(cursor);
            sOwnFailures= wrapper.getYouMissedHitList(cursor);
        }

        return sOwnFailures;
    }
}
