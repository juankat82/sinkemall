package com.juan.android.sinkemall;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;


/**
 * Created by juan on 18/11/17.
 */

public class AttackBoard extends Fragment {

    private Handler handler;
    private static final String TAG="AttackBoard";
    private static final String POSITION="Position";
    private static final String POINTS="Points";
    private Turn mTurn;
    private boolean isGameOver=false;
    private int mTurnNumber;
    private int mTimesHit;
    private int mTimesHitting;
    private int sunkOwnShips;
    private int mWhoHitsNext;
    private int mClickCounter;
    private SecureRandom sc;
    private TextView mTextView;
    private TextView mBattleTextView;
    private Drawable hitShipOneDrawableIcon;
    private Drawable hitShipTwoDrawableIcon;
    private Drawable hitShipThreeDrawableIcon;
    private Drawable hitShipFourDrawableIcon;
    private Drawable missDrawableIcon;
    private Drawable mSunkIconOne;
    private Drawable mSunkIconTwo;
    private Drawable mSunkIconThree;
    private Drawable mSunkIconFour;
    private View v;
    private static VideoView mVideoView;
    private static Context mContext;
    private TextView mTextViewAbout;
    private Uri mUriSink;
    private Uri mUriHit;
    private Uri mUriWin;
    private Uri mUriLose;
    private GridLayout mGridLayout;
    private GridLayout mGridLayoutVideoView;
    private int mMachineSunkShips;
    private List<Button> mButtonList;
    private List<MachineShip> mMachineShipList;
    private List<Integer> mMachineMissList;
    private List<Integer> mYouMissList;
    private static List<OwnShip> ownShips=null;
    private Map<Integer,Integer> mComAttacksMap;
    private static boolean mIsResumeGame;

    public static AttackBoard newFragment(Context context)//},boolean isResumeGame)
    {
        Bundle args =new Bundle();
        AttackBoard fragment=new AttackBoard();
        fragment.setArguments(args);
        mContext=context;
        mIsResumeGame=SingletonClass.isGameStarted();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        handler=new Handler();
        sunkOwnShips=SingletonClass.getOwnSunkShips();
        mIsResumeGame=SingletonClass.isGameStarted();
        mTurn=SingletonClass.readTurnDatabase();
        mMachineSunkShips=SingletonClass.getMachineSunkShips();
        mTurnNumber=mTurn.getmTurnNumber();
        mTimesHit=mTurn.getmTimesHit();
        mTimesHitting=mTurn.getmTimesHitting();
        mWhoHitsNext=mTurn.getWhoHitnext();//0 YOU, 1 COM
        mClickCounter=mTurnNumber%2;
        mMachineShipList=SingletonClass.retrieveMachineShips();//KEEPS THE MACHINE SHIP LIST
        sc=new SecureRandom();

        hitShipOneDrawableIcon=getResources().getDrawable(R.drawable.hit_one);
        hitShipTwoDrawableIcon=getResources().getDrawable(R.drawable.hit_two);
        hitShipThreeDrawableIcon=getResources().getDrawable(R.drawable.hit_three);
        hitShipFourDrawableIcon=getResources().getDrawable(R.drawable.hit_four);
        missDrawableIcon=getResources().getDrawable(R.drawable.ic_hitweb);
        mSunkIconOne=getResources().getDrawable(R.drawable.skull_one);
        mSunkIconTwo=getResources().getDrawable(R.drawable.skull_two);
        mSunkIconThree=getResources().getDrawable(R.drawable.skull_three);
        mSunkIconFour=getResources().getDrawable(R.drawable.skull_four);
        mComAttacksMap=new ArrayMap<>(64);
        for (int i=0;i<64;i++)
        {
            mComAttacksMap.put(i,1);//1 means available, 0 means unavailable
        }

        mUriHit=Uri.parse("android.resource://"+getActivity().getPackageName()+"/"+R.raw.hit);///check this out
        mUriSink=Uri.parse("android.resource://"+getActivity().getPackageName()+"/"+R.raw.sink);///check this out
        mUriWin=Uri.parse("android.resource://"+getActivity().getPackageName()+"/"+R.raw.victory);///check this out
        mUriLose=Uri.parse("android.resource://"+getActivity().getPackageName()+"/"+R.raw.defeat);///check this out
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        v=inflater.inflate(R.layout.attack_board,container,false);
        mGridLayout=v.findViewById(R.id.frame_main_game);
        mGridLayoutVideoView=v.findViewById(R.id.grid_layout_video_view);
        mTextView=v.findViewById(R.id.attack_text_view);
        mBattleTextView=v.findViewById(R.id.in_game_textview);
        mVideoView=v.findViewById(R.id.video_view_hit);



        mTextViewAbout=v.findViewById(R.id.about_button_final_self);
        mTextViewAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setVisibility(View.GONE);
            }
        });



        if (mWhoHitsNext==1)
            machineAttack();
        else
            mTextView.setText(R.string.your_turn);


        mButtonList=createButtons(v);

        for (int i=0;i<mButtonList.size();i++)
        {
            mButtonList.get(i).setOnClickListener(handler);
        }
        /////////////////THIS WILL SET UP THE RESUME GAME OPTION/////////////////
        //////////////RECREATES OWN FAILURES AFTER RESUMING/////////////////////
        if (SingletonClass.getOwnFailures().size()>0)//.length>0)
        {
            List<Integer> temp = SingletonClass.getOwnFailures();
            for (int i = 0; i < SingletonClass.getOwnFailures().size();i++){//length; i++) {
                {
                    mButtonList.get(temp.get(i)).setBackgroundResource(R.drawable.ic_hitweb);
                    mButtonList.get(temp.get(i)).setOnClickListener(null);
                }
            }
        }
        if (SingletonClass.getMachineFailures().size()>0)//.length>0)
        {
           List<Integer> temp = SingletonClass.getMachineFailures();
            int[] tempArray=new int[temp.size()];

            for (int i = 0; i < temp.size();i++){
                {
                    tempArray[i]=temp.get(i);
                    int ind=temp.get(i);
                    if (mComAttacksMap.containsKey(ind))
                    {
                        mComAttacksMap.remove(ind);
                        mComAttacksMap.put(ind, 0);
                    }
                }
            }
            MyFinalBoardFragment.changeYOUShipsIndexes(tempArray,missDrawableIcon);
        }
        /////////////////////////////////////////////////////////////////////////////////////////

        mTurn=SingletonClass.readTurnDatabase();
        mTurnNumber=mTurn.getmTurnNumber();
        mWhoHitsNext=mTurn.getWhoHitnext();
        mTimesHit=mTurn.getmTimesHit();
        mTimesHitting=mTurn.getmTimesHitting();

        if (mTurnNumber>0)
        {
            if (mMachineMissList==null)
                mMachineMissList=SingletonClass.getMachineFailures();
            if (mYouMissList==null)
                mYouMissList=SingletonClass.getOwnFailures();
            mMachineSunkShips=SingletonClass.getMachineSunkShips();
            SingletonClass.setMachineSunkShips(mMachineSunkShips);

            resumeMachineShips();
            sunkOwnShips=SingletonClass.getOwnSunkShips();
            SingletonClass.setOwnSunkShips(sunkOwnShips);
            resumeOwnShips();
        }
        else
        {
            sunkOwnShips=0;
            mMachineSunkShips=0;
            mMachineMissList=new ArrayList<>();
            mYouMissList=new ArrayList<>();
        }
        return v;
    }
    private void resumeOwnShips()
    {
        ownShips=SingletonClass.getOwnShips();

        for (int i=0;i<ownShips.size();i++)
        {
            OwnShip ownShip=ownShips.get(i);
            int id=ownShip.getmId();
            int a=ownShip.getmIndexA();
            int b=ownShip.getmIndexB();
            int c=ownShip.getmIndexC();
            int d=ownShip.getmIndexD();
            String aState=ownShip.getmIndexAState();
            String bState=ownShip.getmIndexBState();
            String cState=ownShip.getmIndexCState();
            String dState=ownShip.getmIndexDState();
            String state=ownShip.getmState();
            int color=ownShip.getmColor();

            if (state.equals("up"))
            {
                switch (id)
                {
                    case R.id.ship_one:
                        if (aState.equals("O"))
                        {
                            if (mComAttacksMap.containsKey(a))
                            {
                                mComAttacksMap.remove(a);
                                mComAttacksMap.put(a, 0);
                            }
                            MyFinalBoardFragment.hitYOUShipsIndexes(a,mSunkIconOne);//sinks the shipasd
                        }
                        break;
                    case R.id.ship_two:
                    case R.id.ship_two_b:
                        List <Integer> lstYellow=new ArrayList<>();
                        if (aState.equals("O"))
                        {
                            lstYellow.add(a);
                            if (mComAttacksMap.containsKey(a))
                            {
                                mComAttacksMap.remove(a);
                                mComAttacksMap.put(a, 0);
                            }
                        }
                        if (bState.equals("O"))
                        {
                            lstYellow.add(b);
                            if (mComAttacksMap.containsKey(b))
                            {
                                mComAttacksMap.remove(b);
                                mComAttacksMap.put(b, 0);
                            }
                        }
                        int [] bHitArray=new int[lstYellow.size()];
                        for (int j=0;j<lstYellow.size();j++)
                            bHitArray[j]=lstYellow.get(j);
                        MyFinalBoardFragment.changeYOUShipsIndexes(bHitArray,hitShipTwoDrawableIcon);
                        lstYellow.clear();
                        break;
                    case R.id.ship_three:
                    case R.id.ship_three_b:
                        List <Integer> lstBlue=new ArrayList<>();
                        if (aState.equals("O"))
                        {
                            lstBlue.add(a);
                            if (mComAttacksMap.containsKey(a))
                            {
                                mComAttacksMap.remove(a);
                                mComAttacksMap.put(a, 0);
                            }
                        }
                        if (bState.equals("O"))
                        {
                            lstBlue.add(b);
                            if (mComAttacksMap.containsKey(b))
                            {
                                mComAttacksMap.remove(b);
                                mComAttacksMap.put(b, 0);
                            }
                        }
                        if (cState.equals("O"))
                        {
                            lstBlue.add(c);
                            if (mComAttacksMap.containsKey(c))
                            {
                                mComAttacksMap.remove(c);
                                mComAttacksMap.put(c, 0);
                            }
                        }
                        int [] cHitArray=new int[lstBlue.size()];
                        for (int j=0;j<lstBlue.size();j++)
                            cHitArray[j]=lstBlue.get(j);
                        MyFinalBoardFragment.changeYOUShipsIndexes(cHitArray,hitShipThreeDrawableIcon);
                        lstBlue.clear();
                        break;
                    case R.id.ship_four:
                    case R.id.ship_four_b:
                        List <Integer> lstRed=new ArrayList<>();
                        if (aState.equals("O"))
                        {
                            lstRed.add(a);
                            if (mComAttacksMap.containsKey(a))
                            {
                                mComAttacksMap.remove(a);
                                mComAttacksMap.put(a, 0);
                            }
                        }
                        if (bState.equals("O"))
                        {
                            lstRed.add(b);
                            if (mComAttacksMap.containsKey(b))
                            {
                                mComAttacksMap.remove(b);
                                mComAttacksMap.put(b, 0);
                            }
                        }
                        if (cState.equals("O"))
                        {
                            lstRed.add(c);
                            if (mComAttacksMap.containsKey(c))
                            {
                                mComAttacksMap.remove(c);
                                mComAttacksMap.put(c, 0);
                            }
                        }
                        if (dState.equals("O"))
                        {
                            lstRed.add(c);
                            if (mComAttacksMap.containsKey(d))
                            {
                                mComAttacksMap.remove(d);
                                mComAttacksMap.put(d, 0);
                            }
                        }
                        int [] dHitArray=new int[lstRed.size()];
                        for (int j=0;j<lstRed.size();j++)
                            dHitArray[j]=lstRed.get(j);
                        MyFinalBoardFragment.changeYOUShipsIndexes(dHitArray,hitShipFourDrawableIcon);
                        lstRed.clear();
                        break;
                }
            }
            if (state.equals("down"))
            {
                switch (id)
                {
                    case R.id.ship_one:
                        MyFinalBoardFragment.hitYOUShipsIndexes(a,mSunkIconOne);//sinks the shipasd
                        if (mComAttacksMap.containsKey(a))
                        {
                            mComAttacksMap.remove(a);
                            mComAttacksMap.put(a, 0);
                        }
                        break;
                    case R.id.ship_two:
                    case R.id.ship_two_b:
                        int[] bIndexes={a,b};
                        MyFinalBoardFragment.changeYOUShipsIndexes(bIndexes,mSunkIconTwo);
                        if (mComAttacksMap.containsKey(a))
                        {
                            mComAttacksMap.remove(a);
                            mComAttacksMap.put(a, 0);
                        }
                        if (mComAttacksMap.containsKey(b))
                        {
                            mComAttacksMap.remove(b);
                            mComAttacksMap.put(b, 0);
                        }
                        break;
                    case R.id.ship_three:
                    case R.id.ship_three_b:
                        int[] cIndexes={a,b,c};
                        MyFinalBoardFragment.changeYOUShipsIndexes(cIndexes,mSunkIconThree);
                        if (mComAttacksMap.containsKey(a))
                        {
                            mComAttacksMap.remove(a);
                            mComAttacksMap.put(a, 0);
                        }
                        if (mComAttacksMap.containsKey(b))
                        {
                            mComAttacksMap.remove(b);
                            mComAttacksMap.put(b, 0);
                        }
                        if (mComAttacksMap.containsKey(c))
                        {
                            mComAttacksMap.remove(c);
                            mComAttacksMap.put(c, 0);
                        }
                        break;
                    case R.id.ship_four:
                    case R.id.ship_four_b:
                        int[] dIndexes={a,b,c,d};
                        MyFinalBoardFragment.changeYOUShipsIndexes(dIndexes,mSunkIconFour);
                        if (mComAttacksMap.containsKey(a))
                        {
                            mComAttacksMap.remove(a);
                            mComAttacksMap.put(a, 0);
                        }
                        if (mComAttacksMap.containsKey(b))
                        {
                            mComAttacksMap.remove(b);
                            mComAttacksMap.put(b, 0);
                        }
                        if (mComAttacksMap.containsKey(c))
                        {
                            mComAttacksMap.remove(c);
                            mComAttacksMap.put(c, 0);
                        }
                        if (mComAttacksMap.containsKey(d))
                        {
                            mComAttacksMap.remove(d);
                            mComAttacksMap.put(d, 0);
                        }
                        break;
                }
            }
        }
    }
    private void resumeMachineShips()//THIS REFERS TO MY VERY OWN ATTACKS OVER THE MACHINE
    {
        mMachineShipList=SingletonClass.retrieveMachineShips();
        SingletonClass.setMachineSunkShips(-1);
        for (int i=0;i<mMachineShipList.size();i++)
        {
            MachineShip mach=mMachineShipList.get(i);

            String id=mach.getmId();
            int a=mach.getmIndexA();
            int b=mach.getmIndexB();
            int c=mach.getmIndexC();
            int d=mach.getmIndexD();
            String aState=mach.getmIndexAState();
            String bState=mach.getmIndexBState();
            String cState=mach.getmIndexCState();
            String dState=mach.getmIndexDState();
            String state=mach.getmState();
            String color=mach.getmColor();

            if (state.equals("up"))
            {
                switch(color)
                {
                    case "yellow":
                        if (aState.equals("O"))
                        {
                            mButtonList.get(a).setBackground(hitShipTwoDrawableIcon);
                            mButtonList.get(a).setOnClickListener(null);
                        }
                        if (bState.equals("O")) ////DOUBLE CHECK THIS, CAREFUL WITH THE DOWN, MAYBE SWAP WITH IF ABOVE AND WRITE A "DOWN" to ensure the skull gets drawn
                        {
                            mButtonList.get(b).setBackground(hitShipTwoDrawableIcon);
                            mButtonList.get(b).setOnClickListener(null);
                        }
                        break;
                    case "blue":
                        if (aState.equals("O"))
                        {
                            mButtonList.get(a).setBackground(hitShipThreeDrawableIcon);
                            mButtonList.get(a).setOnClickListener(null);
                        }
                        if (bState.equals("O"))
                        {
                            mButtonList.get(b).setBackground(hitShipThreeDrawableIcon);
                            mButtonList.get(b).setOnClickListener(null);
                        }
                        if (cState.equals("O"))
                        {
                            mButtonList.get(c).setBackground(hitShipThreeDrawableIcon);
                            mButtonList.get(c).setOnClickListener(null);
                        }
                        break;
                    case "red":
                        if (aState.equals("O"))
                        {
                            mButtonList.get(a).setBackground(hitShipFourDrawableIcon);
                            mButtonList.get(a).setOnClickListener(null);
                        }
                        if (bState.equals("O"))
                        {
                            mButtonList.get(b).setBackground(hitShipFourDrawableIcon);
                            mButtonList.get(b).setOnClickListener(null);
                        }
                        if (cState.equals("O"))
                        {
                            mButtonList.get(c).setBackground(hitShipFourDrawableIcon);
                            mButtonList.get(c).setOnClickListener(null);
                        }
                        if (dState.equals("O"))
                        {
                            mButtonList.get(d).setBackground(hitShipFourDrawableIcon);
                            mButtonList.get(d).setOnClickListener(null);
                        }
                        break;
                }
            }
            if (state.equals("down"))
            {
                mMachineSunkShips=SingletonClass.getMachineSunkShips();
                switch(color)
                {
                    case "black":
                        mButtonList.get(a).setBackground(mSunkIconOne);
                        mButtonList.get(a).setOnClickListener(null);
                        break;
                    case "yellow":
                        mButtonList.get(a).setBackground(mSunkIconTwo);
                        mButtonList.get(a).setOnClickListener(null);
                        mButtonList.get(b).setBackground(mSunkIconTwo);
                        mButtonList.get(b).setOnClickListener(null);
                        break;
                    case "blue":
                        mButtonList.get(a).setBackground(mSunkIconThree);
                        mButtonList.get(a).setOnClickListener(null);
                        mButtonList.get(b).setBackground(mSunkIconThree);
                        mButtonList.get(b).setOnClickListener(null);
                        mButtonList.get(c).setBackground(mSunkIconThree);
                        mButtonList.get(c).setOnClickListener(null);
                        break;
                    case "red":
                        mButtonList.get(a).setBackground(mSunkIconFour);
                        mButtonList.get(a).setOnClickListener(null);
                        mButtonList.get(b).setBackground(mSunkIconFour);
                        mButtonList.get(b).setOnClickListener(null);
                        mButtonList.get(c).setBackground(mSunkIconFour);
                        mButtonList.get(c).setOnClickListener(null);
                        mButtonList.get(d).setBackground(mSunkIconFour);
                        mButtonList.get(d).setOnClickListener(null);
                        break;
                }
            }
        }
    }
    public void showTextAbout(boolean opt)
    {
        if (opt==true)
        {
            mTextViewAbout.setVisibility(View.VISIBLE);
        }
    }
    //Will be called by the listener. This is the attacking method
    ////////////////////////////////YOU ATTACK STARTS HERE////////////////////////////////////
    private void attack(int cell)
    {

        int isShipSunk=0;
        int touchedShipNumber=-1;//means none untouched, 0 - 3 is the range
        int matchingIndex=-1;//-1 means NO INDEX MATCHES WHICH IS IMPOSSIBLE
        for (int i=0;i<mMachineShipList.size();i++)//CHECKS THAT SHIP WASNT HIT
        {
            //This well could be an entire OR line, like this for looks
            if (mMachineShipList.get(i).getmIndexA()==cell)
            {
                mMachineShipList.get(i).setmIndexAState("O");
                mYouMissList.add(cell);
                matchingIndex=cell;
                touchedShipNumber=i;
            }
            if (mMachineShipList.get(i).getmIndexB()==cell)
            {
                mMachineShipList.get(i).setmIndexBState("O");
                mYouMissList.add(cell);
                matchingIndex=cell;
                touchedShipNumber=i;
            }
            if (mMachineShipList.get(i).getmIndexC()==cell)
            {
                mMachineShipList.get(i).setmIndexCState("O");
                mYouMissList.add(cell);
                matchingIndex=cell;
                touchedShipNumber=i;
            }
            if (mMachineShipList.get(i).getmIndexD()==cell)
            {
                mMachineShipList.get(i).setmIndexDState("O");
                mYouMissList.add(cell);
                matchingIndex=cell;
                touchedShipNumber=i;
            }
        }
        //CHECK IF WE SINK A SHIP By CALLING METHOD checkIfShipIsSunk(machineShip,touchedIndex,shipNumber (similar to machineship?))
        if (matchingIndex==cell)
        {
            switch (touchedShipNumber)
            {
                case 0:
                    mButtonList.get(matchingIndex).setBackground(hitShipOneDrawableIcon);//change button icon
                    //check the ship has no more indexes available then draw the skull icon
                    isShipSunk=checkIfShipIsSunk(mMachineShipList.get(0),matchingIndex,touchedShipNumber);
                    if (isShipSunk==1)
                    {
                        mMachineShipList.get(0).setmState("down");
                        mMachineShipList.get(0).setmIndexAState("O");
                        mMachineShipList.get(0).setmIndexBState("NA");
                        mMachineShipList.get(0).setmIndexCState("NA");
                        mMachineShipList.get(0).setmIndexDState("NA");
                        mMachineShipList.get(0).setmColor("black");
                    }
                    break;
                case 1:
                    mButtonList.get(matchingIndex).setBackground(hitShipTwoDrawableIcon);//change button icon
                    isShipSunk=checkIfShipIsSunk(mMachineShipList.get(1),matchingIndex,touchedShipNumber);
                    if (isShipSunk==1)
                    {
                        mMachineShipList.get(1).setmState("down");
                        mMachineShipList.get(1).setmIndexAState("O");
                        mMachineShipList.get(1).setmIndexBState("O");
                        mMachineShipList.get(1).setmIndexCState("NA");
                        mMachineShipList.get(1).setmIndexDState("NA");
                        mMachineShipList.get(1).setmColor("yellow");
                    }
                    break;
                case 2:
                    mButtonList.get(matchingIndex).setBackground(hitShipThreeDrawableIcon);//change button icon
                    isShipSunk=checkIfShipIsSunk(mMachineShipList.get(2),matchingIndex,touchedShipNumber);
                    if (isShipSunk==1)
                    {
                        mMachineShipList.get(2).setmState("down");
                        mMachineShipList.get(2).setmIndexAState("O");
                        mMachineShipList.get(2).setmIndexBState("O");
                        mMachineShipList.get(2).setmIndexCState("O");
                        mMachineShipList.get(2).setmIndexDState("NA");
                        mMachineShipList.get(2).setmColor("blue");
                    }
                    break;
                case 3:
                    mButtonList.get(matchingIndex).setBackground(hitShipFourDrawableIcon);//change button icon
                    isShipSunk=checkIfShipIsSunk(mMachineShipList.get(3),matchingIndex,touchedShipNumber);
                    if (isShipSunk==1)
                    {
                        mMachineShipList.get(3).setmState("down");
                        mMachineShipList.get(3).setmIndexAState("O");
                        mMachineShipList.get(3).setmIndexBState("O");
                        mMachineShipList.get(3).setmIndexCState("O");
                        mMachineShipList.get(3).setmIndexDState("O");
                        mMachineShipList.get(3).setmColor("red");
                    }
                    break;
            }

            touchedShipNumber=-1;
            if (!SingletonClass.isVideoOff())
            {
                mGridLayout.setVisibility(View.GONE);
                mGridLayoutVideoView.setVisibility(View.VISIBLE);

                if (isShipSunk==1)
                {
                    mBattleTextView.setText(R.string.sunk_text);

                    mVideoView.setVideoURI(mUriSink);
                    mVideoView.start();
                    mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            mBattleTextView.setText(R.string.press_to_continue_text);
                            mVideoView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mVideoView.setOnClickListener(null);
                                    mGridLayoutVideoView.setVisibility(View.GONE);
                                    mGridLayout.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    });
                }
                else
                {
                    mBattleTextView.setText(R.string.hit_text);
                    mGridLayout.setVisibility(View.GONE);
                    mGridLayoutVideoView.setVisibility(View.VISIBLE);
                    mVideoView.setVideoURI(mUriHit);//mUriSink or mUriHit
                    mVideoView.start();
                    mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            mBattleTextView.setText(R.string.press_to_continue_text);
                            mVideoView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mVideoView.setOnClickListener(null);
                                    mGridLayoutVideoView.setVisibility(View.GONE);
                                    mGridLayout.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    });
                }
            }
            else
            {
                mBattleTextView.setText(R.string.hit_text);
            }
        }
        else
        {
            mButtonList.get(cell).setBackground(missDrawableIcon);
            mYouMissList.add(cell);
            mBattleTextView.setText(R.string.missed_text);
        }
    }

    private int checkIfShipIsSunk(MachineShip machineShip,int matchingIndex,int index)
    {
        if (machineShip.getmIndexA()==matchingIndex)
            machineShip.setmIndexAState("O");

        if (machineShip.getmIndexB()==matchingIndex)
            machineShip.setmIndexBState("O");

        if (machineShip.getmIndexC()==matchingIndex)
            machineShip.setmIndexCState("O");

        if (machineShip.getmIndexD()==matchingIndex)
            machineShip.setmIndexDState("O");

        switch(machineShip.getmId())
        {
            case "shipCompOne":
                if (checkA(machineShip).equals("O"))
                {
                    machineShip.setmState("down");
                    mTimesHitting++;
                    mButtonList.get(machineShip.getmIndexA()).setBackground(mSunkIconOne);
                }
                break;
            case "shipCompTwo":
                if (checkA(machineShip).equals("O") && checkB(machineShip).equals("O"))
                {
                    machineShip.setmState("down");
                    mTimesHitting++;
                    mButtonList.get(machineShip.getmIndexA()).setBackground(mSunkIconTwo);
                    mButtonList.get(machineShip.getmIndexB()).setBackground(mSunkIconTwo);
                }
                break;
            case "shipCompThree":
                if (checkA(machineShip).equals("O") && checkB(machineShip).equals("O") && checkC(machineShip).equals("O"))
                {
                    machineShip.setmState("down");
                    mTimesHitting++;
                    mButtonList.get(machineShip.getmIndexA()).setBackground(mSunkIconThree);
                    mButtonList.get(machineShip.getmIndexB()).setBackground(mSunkIconThree);
                    mButtonList.get(machineShip.getmIndexC()).setBackground(mSunkIconThree);
                }
                break;
            case "shipCompFour":
                if (checkA(machineShip).equals("O") && checkB(machineShip).equals("O") && checkC(machineShip).equals("O") && checkD(machineShip).equals("O"))
                {
                    machineShip.setmState("down");
                    mTimesHitting++;
                    mButtonList.get(machineShip.getmIndexA()).setBackground(mSunkIconFour);
                    mButtonList.get(machineShip.getmIndexB()).setBackground(mSunkIconFour);
                    mButtonList.get(machineShip.getmIndexC()).setBackground(mSunkIconFour);
                    mButtonList.get(machineShip.getmIndexD()).setBackground(mSunkIconFour);
                }
                break;
        }
        int count=0;

        if (machineShip.getmState().equals("down"))
        {
            mMachineSunkShips++;
            return 1;
        }
        else
            return 0;
    }

    private String checkA(MachineShip machShip)
    {
        return machShip.getmIndexAState();
    }
    private String checkB(MachineShip machShip)
    {
        return machShip.getmIndexBState();
    }
    private String checkC(MachineShip machShip)
    {
        return machShip.getmIndexCState();
    }
    private String checkD(MachineShip machShip)
    {
        return machShip.getmIndexDState();
    }
    ////////////////////////////////YOU ATTACK ENDS HERE////////////////////////////////////



    ////////////////////////////////COM ATTACK STARTS HERE////////////////////////////////////
    private void machineAttack()
    {
        CountDownTimer countDownTimer=new CountDownTimer(100,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTextView.setText(R.string.coms_turn);
            }
            @Override
            public void onFinish() {
                mTextView.setText(R.string.your_turn);
               // SystemClock.sleep(500);

                if (ownShips==null || ownShips.isEmpty())
                    ownShips=SingletonClass.getOwnShips();//CONTAINS ALL THE INDEXES PROPERTIES
                int shipSunk=-1;//means no ship is sunk, 1 means yes
                //List<Button> mYouButtons=MyFinalBoardFragment.retrieveYOUShipsButtons();
                int[] chosenButtonsToTurn={-1,-1,-1,-1};//new int[4];//this is to be sent to MyFinalBoardFragment.changeYOUShipsIndexes

                //////////////////////////////////////
                //this is machines attacking code
                int randomAttackAt=sc.nextInt(64);
                while (mComAttacksMap.get(randomAttackAt)==0)
                {
                    randomAttackAt=sc.nextInt(64);
                }
                mComAttacksMap.put(randomAttackAt,0);  //now we set the CHOSEN AND AVAILABLE VARIABLE in map to 0 to make unavailable

                int chosenShip=-1;

                chosenShip=checkMachineAttackAt(randomAttackAt);//will set the vble as hit

                switch(chosenShip)//will change icons and update vble
                {
                    case 0:
                        if (ownShips.get(0).getmIndexAState().equals("O"))
                        {
                            ownShips.get(0).setmState("down");
                            sunkOwnShips++;
                            chosenButtonsToTurn[0]=ownShips.get(0).getmIndexA();
                            MyFinalBoardFragment.changeYOUShipsIndexes(chosenButtonsToTurn,mSunkIconOne);
                            if (!SingletonClass.isVideoOff())
                            {
                                sinkAnimation();
                            }
                            mBattleTextView.setText(R.string.machine_sunk_text);
                            mTimesHit++;
                        }
                        break;
                    case 1:
                        if (ownShips.get(1).getmIndexAState().equals("O") && ownShips.get(1).getmIndexBState().equals("O"))
                        {
                            ownShips.get(1).setmState("down");
                            sunkOwnShips++;
                            chosenButtonsToTurn[0]=ownShips.get(1).getmIndexA();
                            chosenButtonsToTurn[1]=ownShips.get(1).getmIndexB();
                            MyFinalBoardFragment.changeYOUShipsIndexes(chosenButtonsToTurn,mSunkIconTwo);
                            if (!SingletonClass.isVideoOff())
                            {
                                sinkAnimation();
                            }
                            mBattleTextView.setText(R.string.machine_sunk_text);
                            mTimesHit++;
                        }
                        break;
                    case 2:
                        if (ownShips.get(2).getmIndexAState().equals("O") && ownShips.get(2).getmIndexBState().equals("O") && ownShips.get(2).getmIndexCState().equals("O"))
                        {
                            ownShips.get(2).setmState("down");
                            sunkOwnShips++;
                            chosenButtonsToTurn[0]=ownShips.get(2).getmIndexA();
                            chosenButtonsToTurn[1]=ownShips.get(2).getmIndexB();
                            chosenButtonsToTurn[2]=ownShips.get(2).getmIndexC();
                            MyFinalBoardFragment.changeYOUShipsIndexes(chosenButtonsToTurn,mSunkIconThree);
                            if (!SingletonClass.isVideoOff())
                            {
                                sinkAnimation();
                            }
                            mBattleTextView.setText(R.string.machine_sunk_text);
                            mTimesHit++;
                        }
                        break;
                    case 3:
                        if (ownShips.get(3).getmIndexAState().equals("O") && ownShips.get(3).getmIndexBState().equals("O") && ownShips.get(3).getmIndexCState().equals("O") && ownShips.get(3).getmIndexDState().equals("O"))
                        {
                            ownShips.get(3).setmState("down");
                            sunkOwnShips++;
                            chosenButtonsToTurn[0]=ownShips.get(3).getmIndexA();
                            chosenButtonsToTurn[1]=ownShips.get(3).getmIndexB();
                            chosenButtonsToTurn[2]=ownShips.get(3).getmIndexC();
                            chosenButtonsToTurn[3]=ownShips.get(3).getmIndexD();

                            MyFinalBoardFragment.changeYOUShipsIndexes(chosenButtonsToTurn,mSunkIconFour);
                            if (!SingletonClass.isVideoOff())
                            {
                                sinkAnimation();
                            }
                            mBattleTextView.setText(R.string.machine_sunk_text);
                            mTimesHit++;

                        }
                        break;
                }
                chosenButtonsToTurn[0]=-1;
                chosenButtonsToTurn[1]=-1;
                chosenButtonsToTurn[2]=-1;
                chosenButtonsToTurn[3]=-1;

                mClickCounter++;
                mClickCounter=mClickCounter%2;
                mWhoHitsNext=mClickCounter;
                SingletonClass.setOwnSunkShips(sunkOwnShips);

            }
        }.start();
    }

    private int checkMachineAttackAt(int cell)
    {
        int hitShip=-1;

        for (int i=0;i<ownShips.size();i++)
        {
            if (cell==ownShips.get(i).getmIndexA())
            {
                ownShips.get(i).setmIndexAState("O");
                hitShip=i;

            }
            if (cell==ownShips.get(i).getmIndexB())
            {
                ownShips.get(i).setmIndexBState("O");
                hitShip=i;
            }
            if (cell==ownShips.get(i).getmIndexC())
            {
                ownShips.get(i).setmIndexCState("O");
                hitShip=i;
            }
            if (cell==ownShips.get(i).getmIndexD())
            {
                ownShips.get(i).setmIndexDState("O");
                hitShip=i;
            }
        }
        int color=-1;//used to pick up the color of the HIT symbol
        mBattleTextView.setText(R.string.hit_text);
        switch(hitShip)
        {
            case 0:MyFinalBoardFragment.hitYOUShipsIndexes(cell,hitShipOneDrawableIcon);
                break;
            case 1:MyFinalBoardFragment.hitYOUShipsIndexes(cell,hitShipTwoDrawableIcon);
                break;
            case 2:MyFinalBoardFragment.hitYOUShipsIndexes(cell,hitShipThreeDrawableIcon);
                break;
            case 3:MyFinalBoardFragment.hitYOUShipsIndexes(cell,hitShipFourDrawableIcon);
                break;
        }

        if (hitShip!=-1) {
            if (!SingletonClass.isVideoOff())
                hitAnimation();
        }
        else
        {
            MyFinalBoardFragment.hitYOUShipsIndexes(cell,missDrawableIcon);
            mMachineMissList.add(cell);
            mBattleTextView.setText(R.string.machine_missed_text);
        }
        SystemClock.sleep(800);
        return hitShip;
    }

    ///////////////////////////////////COM ATTACK ENDS HERE/////////////////
    ////////////////////HELPING METHODS LIKE VIDEO PLAYING//////////////////////
    private void sinkAnimation()
    {
        mBattleTextView.setText(R.string.sunk_text);
        mGridLayout.setVisibility(View.GONE);
        mGridLayoutVideoView.setVisibility(View.VISIBLE);
        mVideoView.setVideoURI(mUriSink);
        mVideoView.start();
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mBattleTextView.setText(R.string.press_to_continue_text);
                mVideoView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mVideoView.setOnClickListener(null);
                        mGridLayoutVideoView.setVisibility(View.GONE);
                        mGridLayout.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

    private void hitAnimation()
    {
        mBattleTextView.setText(R.string.hit_text);
        mGridLayout.setVisibility(View.GONE);
        mGridLayoutVideoView.setVisibility(View.VISIBLE);
        mVideoView.setVideoURI(mUriHit);//mUriSink or mUriHit
        mVideoView.start();
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mBattleTextView.setText(R.string.press_to_continue_text);
                mVideoView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view) {
                        mVideoView.setOnClickListener(null);
                        mGridLayoutVideoView.setVisibility(View.GONE);
                        mGridLayout.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

    ////////////////////////LISTENER FOR BUTTONS//////////////////////
    private class Handler implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            mGridLayout.setEnabled(false);
            int id=view.getId();
            int index=-1;//-1 means unstarted
            for (int i=0;i<mButtonList.size();i++)
            {
                if (mButtonList.get(i).getId()==id)
                    index=i;//checks what ship is hit
            }
            //if machine attacked already or its your turn
            attack(index);
            mClickCounter++;
            mClickCounter=mClickCounter%2;
            mWhoHitsNext=mClickCounter;
            view.setOnClickListener(null);

            SecureRandom scTimer=new SecureRandom();//used for the timer
            CountDownTimer countDownTimer=new CountDownTimer(100,800) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    machineAttack();

                    mTurnNumber++;
                    if (mMachineSunkShips==4)
                    {
                        Log.i(TAG,"Machine SUnk Ships: "+mMachineSunkShips);
                        mVideoView.setOnClickListener(null);
                        gameOver(0);//0 means YOU
                    }
                    else if (sunkOwnShips==4)//COM to YOU
                    {
                        Log.i(TAG,"Own SUnk Ships: "+sunkOwnShips);
                        gameOver(1);//if machine has sunk 4 ships then game is over, 1 means COM wins
                    }
                    else
                    {
                        mTextView.setText(R.string.your_turn);
                        mGridLayout.setEnabled(true);
                    }
                }
            }.start();
        }
    }

    private void gameOver(int winnerId)//0 means YOU and 1 means COM
    {
        isGameOver=true;
        SingletonClass.setGameHeld(false);
        SingletonClass.setOwnSunkShips(0);
        SingletonClass.setOwnSunkShipsIntoDatabase(0);
        SingletonClass.setMachineSunkShips(0);
        MediaController mc = new MediaController(mContext);
        mGridLayoutVideoView.setVisibility(View.VISIBLE);
        mGridLayout.setVisibility(View.GONE);
        for (int i = 0; i < mButtonList.size(); i++)
            mButtonList.get(i).setOnClickListener(null);
        mVideoView.setSoundEffectsEnabled(true);
        if (winnerId==0)
        {
            mTextView.setText(R.string.you_win_text);
            mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            mVideoView.setMediaController(mc);
            mVideoView.setVideoURI(mUriWin);
            if (!SingletonClass.isVideoOff())
            {
                mVideoView.start();
                mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mTextView.setText(R.string.you_win_text);
                            mBattleTextView.setText(R.string.click_to_finish_anim);
                            mVideoView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    executeAfterGameIsOver(winnerId);
                                }
                            });
                    }
                });
            }
            else //if video is disabled
            {
                mTextView.setText(R.string.you_win_text);
                mBattleTextView.setText(R.string.click_to_finish_anim);
                executeAfterGameIsOver(winnerId);
            }
        }
        else
        {
            mTextView.setText(R.string.you_lose_text);
            mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            mVideoView.setMediaController(mc);
            mVideoView.setVideoURI(mUriLose);

            if(!SingletonClass.isVideoOff())
            {
                mVideoView.start();
                mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mTextView.setText(R.string.you_lose_text);
                        mBattleTextView.setText(R.string.click_to_finish);
                        mVideoView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                executeAfterGameIsOver(winnerId);
                            }
                        });
                    }
                });
            }
            else    //if video is disabled
            {
                mTextView.setText(R.string.you_lose_text);
                mBattleTextView.setText(R.string.click_to_finish_anim);
                executeAfterGameIsOver(winnerId);
            }
        }
    }
    ////////////////////////LISTENER FOR BUTTONS ENDS HERE/////////////////////////

    ////////////////////////HELPING CLASSES FOR  GAME OVER AND  PUNCTUATION SYSTEM//////////
    private void executeAfterGameIsOver(int winnerId)
    {
        //when animation finishes, check punctuation and launch an activity/fragment to write your record then send to main menu
        SingletonClass.setGameHeld(false);
        List<Ranks> ranksList=SingletonClass.queryRecords(null,null);
        //CALCULATE PUNCTUATION
        long points=calculateEarnedPoints(mTurnNumber,mTimesHit,winnerId);//calculate points earned
        int isToBeSet=checkEarnedPoints(points,ranksList);//gets position when you stand in the rank. -1 means YOU ARE OUT
        if (isToBeSet>=0 && isToBeSet<5) //if you are in the ranking, add one so you are not an INDEX but a real number and be able to used for recording
        {
            isToBeSet++;
            Intent intent=RecordEnterActivity.newIntent(mContext);
            intent.putExtra(POSITION,isToBeSet);
            intent.putExtra(POINTS,points);
            CountDownTimer timer=new CountDownTimer(2000,200) {
                @Override
                public void onTick(long l) {
                    v.setAlpha(v.getAlpha()-0.2f);
                }

                @Override
                public void onFinish() {
                    startActivity(intent);
                }
            }.start();
        }
    }
    private int checkEarnedPoints(long point,List<Ranks> rankList)
    {
        int position=-1;
        for(int i=rankList.size()-1;i>=0;i--)
        {
            if (point>rankList.get(i).getPoints())
            {
                position=i;
            }
        }
        return position;
    }
    private long calculateEarnedPoints(int turnNumber,int timesHit, int winnerId)
    {
        long totalPoints=9999999;
        long temp=(totalPoints/turnNumber)+1;
        temp=temp-(timesHit*10000);
        if (winnerId==1)
            temp-=50000;
        return temp;
    }
    ////////////////BUTTON CREATION, NOT IMPORTANT AFTER CREATION ANYMORE/////////////
    private List<Button> createButtons(View v)
    {
        List<Button> buttons=new ArrayList<>();
        Button button1= v.findViewById(R.id.button1);
        button1.setText("1");
        buttons.add(button1);

        Button button2= v.findViewById(R.id.button2);
        button2.setText("2");
        buttons.add(button2);

        Button button3= v.findViewById(R.id.button3);
        button3.setText("3");
        buttons.add(button3);

        Button button4= v.findViewById(R.id.button4);
        button4.setText("4");
        buttons.add(button4);

        Button button5= v.findViewById(R.id.button5);
        button5.setText("5");
        buttons.add(button5);

        Button button6= v.findViewById(R.id.button6);
        button6.setText("6");
        buttons.add(button6);

        Button button7= v.findViewById(R.id.button7);
        button7.setText("7");
        buttons.add(button7);

        Button button8= v.findViewById(R.id.button8);
        button8.setText("8");
        buttons.add(button8);

        Button button9= v.findViewById(R.id.button9);
        button9.setText("9");
        buttons.add(button9);

        Button button10= v.findViewById(R.id.button10);
        button10.setText("10");
        buttons.add(button10);

        Button button11= v.findViewById(R.id.button11);
        button11.setText("11");
        buttons.add(button11);

        Button button12= v.findViewById(R.id.button12);
        button12.setText("12");
        buttons.add(button12);

        Button button13= v.findViewById(R.id.button13);
        button13.setText("13");
        buttons.add(button13);

        Button button14= v.findViewById(R.id.button14);
        button14.setText("14");
        buttons.add(button14);

        Button button15= v.findViewById(R.id.button15);
        button15.setText("15");
        buttons.add(button15);

        Button button16= v.findViewById(R.id.button16);
        button16.setText("16");
        buttons.add(button16);

        Button button17= v.findViewById(R.id.button17);
        button17.setText("17");
        buttons.add(button17);

        Button button18= v.findViewById(R.id.button18);
        button18.setText("18");
        buttons.add(button18);

        Button button19= v.findViewById(R.id.button19);
        button19.setText("19");
        buttons.add(button19);

        Button button20= v.findViewById(R.id.button20);
        button20.setText("20");
        buttons.add(button20);

        Button button21= v.findViewById(R.id.button21);
        button21.setText("21");
        buttons.add(button21);

        Button button22= v.findViewById(R.id.button22);
        button22.setText("22");
        buttons.add(button22);

        Button button23= v.findViewById(R.id.button23);
        button23.setText("23");
        buttons.add(button23);

        Button button24= v.findViewById(R.id.button24);
        button24.setText("24");
        buttons.add(button24);

        Button button25= v.findViewById(R.id.button25);
        button25.setText("25");
        buttons.add(button25);

        Button button26= v.findViewById(R.id.button26);
        button26.setText("26");
        buttons.add(button26);

        Button button27= v.findViewById(R.id.button27);
        button27.setText("27");
        buttons.add(button27);

        Button button28= v.findViewById(R.id.button28);
        button28.setText("28");
        buttons.add(button28);

        Button button29= v.findViewById(R.id.button29);
        button29.setText("29");
        buttons.add(button29);

        Button button30= v.findViewById(R.id.button30);
        button30.setText("30");
        buttons.add(button30);

        Button button31= v.findViewById(R.id.button31);
        button31.setText("31");
        buttons.add(button31);

        Button button32= v.findViewById(R.id.button32);
        button32.setText("32");
        buttons.add(button32);

        Button button33= v.findViewById(R.id.button33);
        button33.setText("33");
        buttons.add(button33);

        Button button34= v.findViewById(R.id.button34);
        button34.setText("34");
        buttons.add(button34);

        Button button35= v.findViewById(R.id.button35);
        button35.setText("35");
        buttons.add(button35);

        Button button36= v.findViewById(R.id.button36);
        button36.setText("36");
        buttons.add(button36);

        Button button37= v.findViewById(R.id.button37);
        button37.setText("37");
        buttons.add(button37);

        Button button38= v.findViewById(R.id.button38);
        button38.setText("38");
        buttons.add(button38);

        Button button39= v.findViewById(R.id.button39);
        button39.setText("39");
        buttons.add(button39);

        Button button40= v.findViewById(R.id.button40);
        button40.setText("40");
        buttons.add(button40);

        Button button41= v.findViewById(R.id.button41);
        button41.setText("41");
        buttons.add(button41);

        Button button42= v.findViewById(R.id.button42);
        button42.setText("42");
        buttons.add(button42);

        Button button43= v.findViewById(R.id.button43);
        button43.setText("43");
        buttons.add(button43);

        Button button44= v.findViewById(R.id.button44);
        button44.setText("44");
        buttons.add(button44);

        Button button45= v.findViewById(R.id.button45);
        button45.setText("45");
        buttons.add(button45);

        Button button46= v.findViewById(R.id.button46);
        button46.setText("46");
        buttons.add(button46);

        Button button47= v.findViewById(R.id.button47);
        button47.setText("47");
        buttons.add(button47);

        Button button48= v.findViewById(R.id.button48);
        button48.setText("48");
        buttons.add(button48);

        Button button49= v.findViewById(R.id.button49);
        button49.setText("49");
        buttons.add(button49);

        Button button50= v.findViewById(R.id.button50);
        button50.setText("50");
        buttons.add(button50);

        Button button51= v.findViewById(R.id.button51);
        button51.setText("51");
        buttons.add(button51);

        Button button52= v.findViewById(R.id.button52);
        button52.setText("52");
        buttons.add(button52);

        Button button53= v.findViewById(R.id.button53);
        button53.setText("53");
        buttons.add(button53);

        Button button54= v.findViewById(R.id.button54);
        button54.setText("54");
        buttons.add(button54);

        Button button55= v.findViewById(R.id.button55);
        button55.setText("55");
        buttons.add(button55);

        Button button56= v.findViewById(R.id.button56);
        button56.setText("56");
        buttons.add(button56);

        Button button57= v.findViewById(R.id.button57);
        button57.setText("57");
        buttons.add(button57);

        Button button58= v.findViewById(R.id.button58);
        button58.setText("58");
        buttons.add(button58);

        Button button59= v.findViewById(R.id.button59);
        button59.setText("59");
        buttons.add(button59);

        Button button60= v.findViewById(R.id.button60);
        button60.setText("60");
        buttons.add(button60);

        Button button61= v.findViewById(R.id.button61);
        button61.setText("61");
        buttons.add(button61);

        Button button62= v.findViewById(R.id.button62);
        button62.setText("62");
        buttons.add(button62);

        Button button63= v.findViewById(R.id.button63);
        button63.setText("63");
        buttons.add(button63);

        Button button64= v.findViewById(R.id.button64);
        button64.setText("64");
        buttons.add(button64);

        return buttons;
    }
    ///////////////////OVERRIDED LIFE CYCLE METHODS///////////////////
    @Override
    public void onPause()
    {
        if (!isGameOver)
        {
            mTurn.setmTurnNumber(mTurnNumber);
            mTurn.setmTimesHit(mTimesHit);
            mTurn.setmTimesHitting(mTimesHitting);
            mTurn.setmWhohitNext(mWhoHitsNext);
            SingletonClass.writeTurnOnDatabase(mTurn);
            SingletonClass.setMachineShipsNonDefault(mMachineShipList);//wont restart positions
            SingletonClass.setMachineFailuresOnDatabase(mMachineMissList);//SingletonClass.getMachineFailures());
            SingletonClass.setOwnFailuresOnDatabase(mYouMissList);//SingletonClass.getOwnFailures());
            SingletonClass.setOwnSunkShipsIntoDatabase(sunkOwnShips);
            SingletonClass.setOwnSunkShips(sunkOwnShips);
            SingletonClass.setOwnShips(ownShips);
            SingletonClass.setGameHeld(true);
        }
        else
        {
            SingletonClass.startMachineSettings();//restart positions
            SingletonClass.setOwnSunkShips(0);
            SingletonClass.setMachineSunkShips(0);
            SingletonClass.setOwnSunkShipsIntoDatabase(0);
            SingletonClass.startOwnDefaults();//THIS CAN BE A PROBLEM AND IS NOT 100% NEEDED BUT TO TRY TO CORRECT THE BUG
        }
        super.onPause();
    }
    @Override
    public void onDestroy()
    {
        if (!isGameOver)
        {
            mTurn.setmTurnNumber(mTurnNumber);
            mTurn.setmTimesHit(mTimesHit);
            mTurn.setmTimesHitting(mTimesHitting);
            mTurn.setmWhohitNext(mWhoHitsNext);
            SingletonClass.writeTurnOnDatabase(mTurn);
            SingletonClass.setMachineShipsNonDefault(mMachineShipList);
            SingletonClass.setMachineFailuresOnDatabase(mMachineMissList);//SingletonClass.getMachineFailures());
            SingletonClass.setOwnFailuresOnDatabase(mYouMissList);//SingletonClass.getOwnFailures());
            SingletonClass.setOwnSunkShipsIntoDatabase(sunkOwnShips);
            SingletonClass.setOwnSunkShips(sunkOwnShips);
            SingletonClass.setOwnShips(ownShips);
            SingletonClass.setGameHeld(true);
        }
        else
        {
            SingletonClass.startMachineSettings();
            SingletonClass.setOwnSunkShips(0);
            SingletonClass.setMachineSunkShips(0);
            SingletonClass.setOwnSunkShipsIntoDatabase(0);
            SingletonClass.startOwnDefaults();//THIS CAN BE A PROBLEM AND IS NOT 100% NEEDED BUT TO TRY TO CORRECT THE BUG
        }
        super.onDestroy();
    }
}

