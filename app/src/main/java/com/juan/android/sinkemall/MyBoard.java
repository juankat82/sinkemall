package com.juan.android.sinkemall;

import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * Created by juan on 12/12/17.
 */

public class MyBoard extends Fragment {

    private static final String MEDIA_PLAYER_SESSION_ID = "mediaplayer_session_id";
    private static final String MUSIC_POSITION = "sound_track_position_now";
    private final String TAG="MyBoard";
    private ImageView mBoard;
    private ImageView shipOne;
    private ImageView shipTwo;
    private ImageView shipThree;
    private ImageView shipFour;
    private ImageView shipTwoB;
    private ImageView shipThreeB;
    private ImageView shipFourB;
    private float screenHeight;
    private float mBoardStart;
    private View view;
    private int screenWidth;
    private List<Ship> mShipList;
    private double screenWidthOneEight;
    private double screenHeightOneEight;
    private boolean shipsAreSet=false;
    private Button mButtonStart;
    private Button mAlignmentButton;
    private int mChosenIndex=1;//default 1
    private int mChosenIndexTwin=1;//1 default
    private Handler handler=new Handler();
    private List<int[]> mTwinList;
    private List<ImageView> mShipViewCollection;
    private LayoutInflater mInflater;
    private ViewGroup mViewGroup;
    private List<Ship> definitiveShipList;
    private TextView mTextView;
    private MediaPlayer mMediaPlayer;
    private Bundle savedInstance;

    public static MyBoard newFragment(Context context) {

        Bundle args = new Bundle();
        MyBoard fragment = new MyBoard();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        savedInstance=savedInstanceState;
        mShipList=new ArrayList<>();
        mTwinList=new ArrayList<>();
        mShipViewCollection=new ArrayList<>();
        SingletonClass.setShipListToZero();
        SingletonClass.startNewGame();
        if (definitiveShipList!=null)
        {
            definitiveShipList=null;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //set view, background  and measurements for later positioning
        mInflater=inflater;
        mViewGroup=container;
        getFragmentManager().popBackStack();
        setHasOptionsMenu(true);
        view=inflater.inflate(R.layout.self_board,container,false);

        mMediaPlayer = MainGameActivity.getMediaPlayer();
        mMediaPlayer.stop();
        mMediaPlayer=MediaPlayer.create(getContext(), R.raw.game_bgm);
        Toasty.Config.getInstance()
                .setWarningColor(getResources().getColor(R.color.orange))
                .setErrorColor(getResources().getColor(R.color.red))
                .setTextColor(getResources().getColor(R.color.black))
                .setToastTypeface(Typeface.DEFAULT_BOLD)
                .setTextSize(15).apply();
        if (savedInstanceState!=null)
        {
            mMediaPlayer.setAudioSessionId(savedInstanceState.getInt(MEDIA_PLAYER_SESSION_ID));
            mMediaPlayer.seekTo((int)savedInstanceState.getLong(MUSIC_POSITION));
        }
        mMediaPlayer.setLooping(true);
        if (!SingletonClass.isAudioOff())
            mMediaPlayer.start();

        mTextView= view.findViewById(R.id.my_board_text_view_about);
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTextView.setVisibility(View.GONE);
            }
        });
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                screenHeight=view.getHeight();
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        screenWidth=getResources().getDisplayMetrics().widthPixels;;

        mBoard=view.findViewById(R.id.main_game_background);
        mBoard.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mBoardStart=screenHeight-mBoard.getBackground().getMinimumHeight();
                screenHeightOneEight = (screenHeight - mBoardStart) / 8;
                mBoard.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        mBoard.setMaxHeight((int)mBoardStart+1);
        mBoard.setMinimumHeight((int)mBoardStart+1);

        screenWidthOneEight=screenWidth/8;
        screenHeightOneEight=mBoard.getBackground().getMinimumHeight()/8;
        /////////////////SET BUTTONS///////////////
        mAlignmentButton=(Button) view.findViewById(R.id.alignment_button);
        mButtonStart=(Button) view.findViewById(R.id.button_start);

        /////////////////SET SHIPS/////////////////
        ////////////////VERTICAL///////////////////
        shipOne=view.findViewById(R.id.ship_one);
        Drawable drOne=shipOne.getBackground();
        Rect rOne=drOne.getBounds();
        rOne.set(rOne.left,rOne.top,rOne.right+2,rOne.bottom-1);
        drOne.setBounds(rOne);
        shipOne.setImageDrawable(drOne);
        shipOne.setOnTouchListener(handler);
        shipOne.setMinimumHeight((int)screenHeightOneEight);
        shipOne.setMaxHeight((int)screenHeightOneEight);
        shipOne.setMinimumWidth((int)screenWidthOneEight);
        shipOne.setMaxWidth((int)screenWidthOneEight);
        mShipList.add(new Ship(shipOne.getId()));//index 0
        mShipViewCollection.add(shipOne);

        shipTwo=view.findViewById(R.id.ship_two);
        Drawable drTwo=shipTwo.getBackground();
        Rect rTwo=drTwo.getBounds();
        rTwo.set(rTwo.left+2,rTwo.top,rTwo.right+7,rTwo.bottom);
        drTwo.setBounds(rTwo);
        shipTwo.setBackground(drTwo);
        shipTwo.setBottom(shipTwo.getBottom()-2);

        shipTwo.setOnTouchListener(handler);
        shipTwo.setMinimumHeight((int)screenHeightOneEight-2);
        shipTwo.setMaxHeight((int)(screenHeightOneEight-2));
        shipTwo.setMinimumWidth((int)(screenWidthOneEight*2));
        shipTwo.setMaxWidth((int)(screenWidthOneEight*2));
        mShipViewCollection.add(shipTwo);
        mShipList.add(new Ship(shipTwo.getId()));//index 1

        shipThree=view.findViewById(R.id.ship_three);
        Drawable dr=shipThree.getBackground();
        Rect r=dr.getBounds();
        r.set(r.left,r.top,r.right-100,r.bottom);
        dr.setBounds(r);
        shipThree.setImageDrawable(dr);
        shipThree.setOnTouchListener(handler);
        shipThree.setMinimumHeight((int)screenHeightOneEight);
        shipThree.setMaxHeight((int)screenHeightOneEight);
        shipThree.setMinimumWidth((int)(screenWidthOneEight*3-4));
        shipThree.setMaxWidth((int)(screenWidthOneEight*3-4));
        mShipViewCollection.add(shipThree);
        mShipList.add(new Ship(shipThree.getId()));//index 2

        shipFour=view.findViewById(R.id.ship_four);
        shipFour.setOnTouchListener(handler);
        shipFour.setMinimumHeight((int)screenHeightOneEight-3);
        shipFour.setMaxHeight((int)screenHeightOneEight-3);
        shipFour.setMinimumWidth((int)(screenWidthOneEight*4-3));
        shipFour.setMaxWidth((int)(screenWidthOneEight*4-3));
        mShipViewCollection.add(shipFour);
        mShipList.add(new Ship(shipFour.getId()));//index 3

        mShipList.get(0).setVisible(true);
        mShipList.get(1).setVisible(true);
        mShipList.get(2).setVisible(true);
        mShipList.get(3).setVisible(true);
        /////////////HORIZONTAL//////////////////////
        shipTwoB=view.findViewById(R.id.ship_two_b);
        shipTwoB.setOnTouchListener(handler);
        shipTwoB.setMinimumHeight((int)screenHeightOneEight*2-1);
        shipTwoB.setMaxHeight((int)(screenHeightOneEight*2)-1);
        shipTwoB.setMinimumWidth((int)screenWidthOneEight-2);
        shipTwoB.setMaxWidth((int)screenWidthOneEight-2);
        mShipViewCollection.add(shipTwoB);
        mShipList.add(new Ship(shipTwoB.getId()));//index 4

        shipThreeB=view.findViewById(R.id.ship_three_b);
        Drawable drb=shipThreeB.getBackground();
        Rect rB=drb.getBounds();
        rB.set(0,0,(int)(screenWidthOneEight),(int)screenHeightOneEight*3);
        drb.setBounds(rB);
        shipThreeB.setImageDrawable(drb);
        shipThreeB.setOnTouchListener(handler);
        shipThreeB.setMinimumHeight((int)screenHeightOneEight*3);
        shipThreeB.setMaxHeight((int)screenHeightOneEight*3);
        shipThreeB.setMinimumWidth((int)screenWidthOneEight);
        shipThreeB.setMaxWidth((int)screenWidthOneEight);
        mShipViewCollection.add(shipThreeB);
        mShipList.add(new Ship(shipThreeB.getId()));//index 5

        shipFourB=view.findViewById(R.id.ship_four_b);
        shipFourB.setOnTouchListener(handler);
        shipFourB.setMinimumHeight((int)screenHeightOneEight*4);
        shipFourB.setMaxHeight((int)screenHeightOneEight*4);
        shipFourB.setMinimumWidth((int)screenWidthOneEight);
        shipFourB.setMaxWidth((int)screenWidthOneEight);
        mShipViewCollection.add(shipFourB);
        mShipList.add(new Ship(shipFourB.getId()));//index 6
        //////////////////////////////////////////////////
        int[] shipOneTwin={shipOne.getId(),0,shipOne.getId(),0};//hor id, hor index, ver id, ver index
        int[] shipTwoTwin={shipTwo.getId(),1,shipTwoB.getId(),4};//hor id, hor index, ver id, ver index
        int[] shipThreeTwin={shipThree.getId(),2,shipThreeB.getId(),5};
        int[] shipFourTwin={shipFour.getId(),3,shipFourB.getId(),6};
        mTwinList.add(shipOneTwin);
        mTwinList.add(shipTwoTwin);
        mTwinList.add(shipThreeTwin);
        mTwinList.add(shipFourTwin);

        //turns the ImageViews (actually dissapear the horizontal or vertical one in spite of its contrary)
        mAlignmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    mShipViewCollection.get(mChosenIndex).setVisibility(View.GONE);
                    mShipList.get(mChosenIndex).setVisible(false);
                    mShipViewCollection.get(mChosenIndexTwin).setVisibility(View.VISIBLE);
                    mShipList.get(mChosenIndexTwin).setVisible(true);
                    int interchangeVble=mChosenIndex;
                    mChosenIndex=mChosenIndexTwin;
                    mChosenIndexTwin=interchangeVble;
            }
        });
        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                definitiveShipList=new ArrayList<>();
                if (definitiveShipList.size()>0)
                    definitiveShipList.clear();
                for (int i=0;i<mShipList.size();i++)
                {
                    if (mShipList.get(i).isIn() && mShipList.get(i).isVisible())
                    {
                        definitiveShipList.add(mShipList.get(i));
                    }
                }
                if (definitiveShipList.size()<4)
                {
                    Toasty.error(getContext(), "There are not 4 ships placed on the board", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Ship ship1=definitiveShipList.get(0);
                    Rect rect1=new Rect();
                    rect1.set((int)ship1.getOriginPoint().x,(int)ship1.getOriginPoint().y,(int)ship1.getFinishPoint().x,(int)ship1.getFinishPoint().y);

                    Ship ship2=definitiveShipList.get(1);
                    Rect rect2=new Rect();
                    rect2.set((int)ship2.getOriginPoint().x,(int)ship2.getOriginPoint().y,(int)ship2.getFinishPoint().x,(int)ship2.getFinishPoint().y);

                    Ship ship3=definitiveShipList.get(2);
                    Rect rect3=new Rect();
                    rect3.set((int)ship3.getOriginPoint().x,(int)ship3.getOriginPoint().y,(int)ship3.getFinishPoint().x,(int)ship3.getFinishPoint().y);

                    Ship ship4=definitiveShipList.get(3);
                    Rect rect4=new Rect();
                    rect4.set((int)ship4.getOriginPoint().x,(int)ship4.getOriginPoint().y,(int)ship4.getFinishPoint().x,(int)ship4.getFinishPoint().y);

                    if (rect1.intersect(rect2) || rect1.intersect(rect3) || rect1.intersect(rect4) || rect2.intersect(rect3)
                            || rect2.intersect(rect4) || rect3.intersect(rect4))
                    {
                        Toasty.warning(getContext(),"Game wont start if a ship steps on another",Toast.LENGTH_SHORT).show();
                        shipsAreSet=false;
                    }
                    else
                    {
                        shipOne.setOnTouchListener(null);
                        shipTwo.setOnTouchListener(null);
                        shipThree.setOnTouchListener(null);
                        shipFour.setOnTouchListener(null);
                        shipTwoB.setOnTouchListener(null);
                        shipThreeB.setOnTouchListener(null);
                        shipFourB.setOnTouchListener(null);
                        mAlignmentButton.setEnabled(false);
                        calculatePositions(definitiveShipList);
                    }
                }
            }
        });
        return view;
    }
    public void calculatePositions(List<Ship> definitiveShipListA)
    {

        float yZero=screenHeight-mBoardStart;
        int checkers=0;
        int col;
        int row;
        int count=0;

        for (int i=0;i<definitiveShipListA.size();i++)
        {
            //Log.i(TAG,"This id is:"+definitiveShipListA.get(i).getId());
            //calculates starting X
            col=(int)(definitiveShipListA.get(i).getOriginPoint().x/screenWidthOneEight);
            //calculates starting Y
            row=(int)((definitiveShipListA.get(i).getOriginPoint().y-mBoardStart)/(float)screenHeightOneEight);
           // SingletonClass.setCoordShipsToZero();
            switch (definitiveShipListA.get(i).getId())
            {
                case R.id.ship_one://oneShip 2131165368
                    checkers=1;
                    ShipCoord shipCoordOne=new ShipCoord(definitiveShipListA.get(i).getId(),getResources().getResourceName(definitiveShipListA.get(i).getId()));
                    shipCoordOne.setColor(getResources().getColor(R.color.black));
                    shipCoordOne.addPosition(row,col);
                    SingletonClass.setShips(shipCoordOne);
                    //next add to the singleton and change add it to the database and change the layout of this fragment similar to the attack panel
                    break;
                case R.id.ship_two://2131165371://twoShip
                    checkers=2;
                    ShipCoord shipCoordTwo=new ShipCoord(definitiveShipListA.get(i).getId(),getResources().getResourceName(definitiveShipListA.get(i).getId()));
                    shipCoordTwo.setColor(getResources().getColor(R.color.blue));
                    shipCoordTwo.addPosition(row,col);
                    SingletonClass.setShips(shipCoordTwo);
                    break;
                case R.id.ship_three://2131165372://threeShip
                    checkers=3;
                    ShipCoord shipCoordThree=new ShipCoord(definitiveShipListA.get(i).getId(),getResources().getResourceName(definitiveShipListA.get(i).getId()));
                    shipCoordThree.setColor(getResources().getColor(R.color.yellow));
                    shipCoordThree.addPosition(row,col);
                    SingletonClass.setShips(shipCoordThree);
                    break;
                case R.id.ship_four: //2131165369//fourShip
                    checkers=4;

                    ShipCoord shipCoordFour=new ShipCoord(definitiveShipListA.get(i).getId(),getResources().getResourceName(definitiveShipListA.get(i).getId()));
                    shipCoordFour.setColor(getResources().getColor(R.color.red));
                    shipCoordFour.addPosition(row,col);
                    SingletonClass.setShips(shipCoordFour);
                    break;
                case R.id.ship_two_b://2131165370://twoShipB
                    checkers=2;
                    ShipCoord shipCoordTwoB=new ShipCoord(definitiveShipListA.get(i).getId(),getResources().getResourceName(definitiveShipListA.get(i).getId()));
                    shipCoordTwoB.setColor(getResources().getColor(R.color.blue));
                    shipCoordTwoB.addPosition(row,col);
                    SingletonClass.setShips(shipCoordTwoB);
                    break;
                case R.id.ship_three_b://2131165366://threeShipB
                    checkers=3;
                    ShipCoord shipCoordThreeB=new ShipCoord(definitiveShipListA.get(i).getId(),getResources().getResourceName(definitiveShipListA.get(i).getId()));
                    shipCoordThreeB.setColor(getResources().getColor(R.color.yellow));
                    shipCoordThreeB.addPosition(row,col);
                    SingletonClass.setShips(shipCoordThreeB);
                    break;
                case R.id.ship_four_b://2131165367://fourShipB
                    checkers=4;
                    ShipCoord shipCoordFourB=new ShipCoord(definitiveShipListA.get(i).getId(),getResources().getResourceName(definitiveShipListA.get(i).getId()));
                    shipCoordFourB.setColor(getResources().getColor(R.color.red));
                    shipCoordFourB.addPosition(row,col);
                    SingletonClass.setShips(shipCoordFourB);
                    break;
                default:break;
            }
        }

        mButtonStart.setVisibility(View.GONE);
        mAlignmentButton.setVisibility(View.GONE);
        configNewView(view,this);
    }

    private void configNewView(View view,Fragment fr) {
        if (SingletonClass.getShipList().size()==4) {
            shipsAreSet=true;
            //since definitiveShip arraylist is set to NULL at start, it shouldnt be possible to NOT SET the ships
            Intent intent = MyFinalBoard.newIntent(getContext());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else
        {
            Toasty.warning(getContext(),"THERE ARE NOT 4 SHIPS ON DATABASE",Toast.LENGTH_SHORT).show();
        }
    }

    ///this class is anonymous and it serves the touch listeners
    private class Handler implements View.OnTouchListener{
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            //if index 0 is touched, alignment button gets disabled
            if (v.getId()==mShipList.get(0).getId())
                mAlignmentButton.setEnabled(false);
            else
                mAlignmentButton.setEnabled(true);

            PointF downPt=new PointF();
            //point where 0,0 coordinates will be placed
            PointF startPt=new PointF();

            for (int i=0;i<mShipList.size();i++)
            {
                if (v.getId()==mShipList.get(i).getId())//checks the chosen index
                {
                    mChosenIndex=i;
                }
            }
            for (int j=0;j<mTwinList.size();j++)//calculate the chosen index's twin
            {
                int[] tempShip=mTwinList.get(j);

                if (tempShip[1]==mChosenIndex)
                    mChosenIndexTwin=tempShip[3];
                if (tempShip[3]==mChosenIndex)
                    mChosenIndexTwin=tempShip[1];
            }
            v.setAlpha(0.5f);
            int action=event.getAction();
            switch(action)
            {
                case MotionEvent.ACTION_DOWN:
                    downPt.x=v.getX();
                    downPt.y=v.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    startPt.set(v.getX(), v.getY());
                    PointF mv = new PointF(event.getX() - downPt.x, event.getY() - downPt.y);
                    //adds a cypher to center the button to the finger once its pressed
                    int additionX = v.getMeasuredWidth() / 2;
                    int additionY = v.getMeasuredHeight() / 2;
                    float maxX = startPt.x + additionX;
                    float maxY = startPt.y + additionY;
                    //ensures the ship wont be placed out of the bounds of the screen
                    if (startPt.x + mv.x - additionX >= 0 && startPt.y + mv.y - additionY >= 0 && startPt.x + mv.x - additionX - maxX <= screenWidth
                            && startPt.x + mv.x - additionX + v.getWidth() <= screenWidth && startPt.y + mv.y - additionY + v.getHeight() <= screenHeight)//allows moving to any place where coords are greater or equal to zero
                    {
                        v.setX(startPt.x + mv.x - additionX);
                        v.setY(startPt.y + mv.y - additionY);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    //rounding X
                    int xColumn = (int) (v.getX() / screenWidthOneEight);
                    int vDif = (int) (v.getX() % screenWidthOneEight);
                    if (vDif > (screenWidthOneEight / 2)) {
                        v.setX(2+v.getX() + ((float)screenWidthOneEight - vDif));
                    }
                    if (vDif < (screenWidthOneEight / 2)) {
                        v.setX(2+v.getX() - vDif);
                    }
                    //rounding y
                    double yRow = Math.round((screenHeight - mBoardStart) / (float) v.getHeight());//says the amount of rows

                    if (v.getY() >= mBoardStart && v.getY() <= screenHeight)//checks whether the ship is inside the board
                    {
                        for (int i=0;i<mShipList.size();i++)
                        {
                            if (mShipList.get(i).getId()==v.getId())
                            {
                                mShipList.get(i).setIn(true);
                                mShipList.get(i).setVisible(true);
                            }
                        }
                         int yDif = (int) ((v.getY() - mBoardStart) % screenHeightOneEight);//calculates after the grid start point and the difference with the grid

                         if (yDif > (screenHeightOneEight / 2))
                            v.setY(1+v.getY() + ((float)screenHeightOneEight - yDif)+1);

                         if (yDif < (screenHeightOneEight / 2))
                             v.setY(1+v.getY() - yDif);

                        //NEED TO DO FLOOR OR CEIL INSTEAD OF ROUND
                         mShipList.get(mChosenIndex).setOriginPoint(new PointF((float)Math.floor(v.getX()),Math.round(v.getY())));
                         mShipList.get(mChosenIndex).setFinishPoint(new PointF((float)Math.floor(v.getX()+v.getMeasuredWidth()),Math.round(v.getY()+v.getMeasuredHeight())));
                    }
                    else
                    {
                        for (int i=0;i<mShipList.size();i++)
                        {
                            if (mShipList.get(i).getId()==v.getId())
                            {
                                mShipList.get(i).setIn(false);
                            }
                        }
                    }
                    v.setAlpha(1.0f);
                    //if not 4, button START GAME WONT WORK
                    break;
                default:break;
            }
            return true;
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!SingletonClass.isAudioOff())
            mMediaPlayer.start();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.about_menu,menu);
        MenuItem bt=menu.getItem(0);
        if (SingletonClass.isAudioOff())
        {
            bt.setChecked(true);
        }
        else
        {
            bt.setChecked(false);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        mMediaPlayer.getAudioSessionId();
        mMediaPlayer.getCurrentPosition();

        switch (item.getItemId()) {
            case R.id.about_item:
                mTextView.setVisibility(View.VISIBLE);

            case R.id.disable_audio:
                item.setChecked(!item.isChecked());
                SingletonClass.setAudioOff(!SingletonClass.isAudioOff());
                if (!SingletonClass.isAudioOff()) {
                    SingletonClass.setAudioOff(false);
                    mMediaPlayer.start();
                }
                else
                {
                    SingletonClass.setAudioOff(true);
                    mMediaPlayer.pause();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onPause()
    {
        super.onPause();
        mMediaPlayer.pause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        // The activity is about to be destroyed.
        mMediaPlayer.stop();
        mMediaPlayer.getAudioSessionId();
        mMediaPlayer.release();
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(MEDIA_PLAYER_SESSION_ID,mMediaPlayer.getAudioSessionId());
        savedInstanceState.putLong(MUSIC_POSITION,(long)mMediaPlayer.getCurrentPosition());
    }
}
