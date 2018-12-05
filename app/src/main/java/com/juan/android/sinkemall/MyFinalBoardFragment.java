package com.juan.android.sinkemall;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by juan on 02/01/18.
 */

public class MyFinalBoardFragment extends Fragment {

    private int[][] mButtons=new int[8][8];
    private static List<Integer> mSelectedIndexes;
    private static List<Button> mButtonList;
    private View v;
    private GridLayout mGridLayout;
    private LinearLayout mLinearLayout;
    public static TextView mTextViewAbout;
    private static Context sContext;

    public static MyFinalBoardFragment newFragment(Context context) {

        Bundle args = new Bundle();
        sContext=context;
        MyFinalBoardFragment fragment = new MyFinalBoardFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mButtonList=new ArrayList<>();
        mSelectedIndexes=new ArrayList<>();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        v=inflater.inflate(R.layout.final_self_board,container,false);
        mGridLayout=v.findViewById(R.id.frame_final_self_board);
        mLinearLayout=v.findViewById(R.id.finale_self_board_linearlayout);
        mTextViewAbout=v.findViewById(R.id.about_button_final_self);
        mTextViewAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setVisibility(View.GONE);
            }
        });
        TextView tv=v.findViewById(R.id.text_view_animation);
        ValueAnimator animator=ObjectAnimator.ofFloat(tv,"textSize",300,0).setDuration(3000);
        animator.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                tv.setVisibility(View.GONE);
                mGridLayout.setVisibility(View.VISIBLE);
                mLinearLayout.setVisibility(View.VISIBLE);
            }
        });

        animator.start();//check the rounds from database. Execute only if 0
        Button button1= v.findViewById(R.id.button1);
        Button button2= v.findViewById(R.id.button2);
        Button button3= v.findViewById(R.id.button3);
        Button button4= v.findViewById(R.id.button4);
        Button button5= v.findViewById(R.id.button5);
        Button button6= v.findViewById(R.id.button6);
        Button button7= v.findViewById(R.id.button7);
        Button button8= v.findViewById(R.id.button8);
        Button button9= v.findViewById(R.id.button9);
        Button button10= v.findViewById(R.id.button10);
        Button button11= v.findViewById(R.id.button11);
        Button button12= v.findViewById(R.id.button12);
        Button button13= v.findViewById(R.id.button13);
        Button button14= v.findViewById(R.id.button14);
        Button button15= v.findViewById(R.id.button15);
        Button button16= v.findViewById(R.id.button16);
        Button button17= v.findViewById(R.id.button17);
        Button button18= v.findViewById(R.id.button18);
        Button button19= v.findViewById(R.id.button19);
        Button button20= v.findViewById(R.id.button20);
        Button button21= v.findViewById(R.id.button21);
        Button button22= v.findViewById(R.id.button22);
        Button button23= v.findViewById(R.id.button23);
        Button button24= v.findViewById(R.id.button24);
        Button button25= v.findViewById(R.id.button25);
        Button button26= v.findViewById(R.id.button26);
        Button button27= v.findViewById(R.id.button27);
        Button button28= v.findViewById(R.id.button28);
        Button button29= v.findViewById(R.id.button29);
        Button button30= v.findViewById(R.id.button30);
        Button button31= v.findViewById(R.id.button31);
        Button button32= v.findViewById(R.id.button32);
        Button button33= v.findViewById(R.id.button33);
        Button button34= v.findViewById(R.id.button34);
        Button button35= v.findViewById(R.id.button35);
        Button button36= v.findViewById(R.id.button36);
        Button button37= v.findViewById(R.id.button37);
        Button button38= v.findViewById(R.id.button38);
        Button button39= v.findViewById(R.id.button39);
        Button button40= v.findViewById(R.id.button40);
        Button button41= v.findViewById(R.id.button41);
        Button button42= v.findViewById(R.id.button42);
        Button button43= v.findViewById(R.id.button43);
        Button button44= v.findViewById(R.id.button44);
        Button button45= v.findViewById(R.id.button45);
        Button button46= v.findViewById(R.id.button46);
        Button button47= v.findViewById(R.id.button47);
        Button button48= v.findViewById(R.id.button48);
        Button button49= v.findViewById(R.id.button49);
        Button button50= v.findViewById(R.id.button50);
        Button button51= v.findViewById(R.id.button51);
        Button button52= v.findViewById(R.id.button52);
        Button button53= v.findViewById(R.id.button53);
        Button button54= v.findViewById(R.id.button54);
        Button button55= v.findViewById(R.id.button55);
        Button button56= v.findViewById(R.id.button56);
        Button button57= v.findViewById(R.id.button57);
        Button button58= v.findViewById(R.id.button58);
        Button button59= v.findViewById(R.id.button59);
        Button button60= v.findViewById(R.id.button60);
        Button button61= v.findViewById(R.id.button61);
        Button button62= v.findViewById(R.id.button62);
        Button button63= v.findViewById(R.id.button63);
        Button button64= v.findViewById(R.id.button64);
        mButtonList.add(button1);
        mButtonList.add(button2);
        mButtonList.add(button3);
        mButtonList.add(button4);
        mButtonList.add(button5);
        mButtonList.add(button6);
        mButtonList.add(button7);
        mButtonList.add(button8);
        mButtonList.add(button9);
        mButtonList.add(button10);
        mButtonList.add(button11);
        mButtonList.add(button12);
        mButtonList.add(button13);
        mButtonList.add(button14);
        mButtonList.add(button15);
        mButtonList.add(button16);
        mButtonList.add(button17);
        mButtonList.add(button18);
        mButtonList.add(button19);
        mButtonList.add(button20);
        mButtonList.add(button21);
        mButtonList.add(button22);
        mButtonList.add(button23);
        mButtonList.add(button24);
        mButtonList.add(button25);
        mButtonList.add(button26);
        mButtonList.add(button27);
        mButtonList.add(button28);
        mButtonList.add(button29);
        mButtonList.add(button30);
        mButtonList.add(button31);
        mButtonList.add(button32);
        mButtonList.add(button33);
        mButtonList.add(button34);
        mButtonList.add(button35);
        mButtonList.add(button36);
        mButtonList.add(button37);
        mButtonList.add(button38);
        mButtonList.add(button39);
        mButtonList.add(button40);
        mButtonList.add(button41);
        mButtonList.add(button42);
        mButtonList.add(button43);
        mButtonList.add(button44);
        mButtonList.add(button45);
        mButtonList.add(button46);
        mButtonList.add(button47);
        mButtonList.add(button48);
        mButtonList.add(button49);
        mButtonList.add(button50);
        mButtonList.add(button51);
        mButtonList.add(button52);
        mButtonList.add(button53);
        mButtonList.add(button54);
        mButtonList.add(button55);
        mButtonList.add(button56);
        mButtonList.add(button57);
        mButtonList.add(button58);
        mButtonList.add(button59);
        mButtonList.add(button60);
        mButtonList.add(button61);
        mButtonList.add(button62);
        mButtonList.add(button63);
        mButtonList.add(button64);


        int val=0;
        for (int i=0;i<mButtons.length;i++)
        {
            for (int j=0;j<mButtons[i].length;j++)
            {
                mButtons[i][j]=val;
                val++;
            }
        }
            //sets the numbers in the buttons
            for (int i=0;i<mButtonList.size();i++)
            {
                mButtonList.get(i).setText(String.valueOf(i+1));
            }

        List<ShipCoord> mShips;
        mShips=SingletonClass.getShipList();//contains the ships the player has set

        if ((mShips.size()!=0) && (!SingletonClass.getComeBack()))
            processShips(mShips,v);
        else
            populateBoard();

        return v;
    }

    private void populateBoard() {
        //request ships from database (singletonclass)
        List<Button> shipList=SingletonClass.getShipsFromDatabase(null,null,mButtonList.size(),v,mButtonList);
        if (shipList.isEmpty())
            Toast.makeText(getContext(),"HAVE YOU PLACED THE SHIPS? ",Toast.LENGTH_SHORT).show();
        else
        {
            for (int i=0;i<shipList.size();i++)
            {
                mButtonList.add(shipList.get(i));
            }

            List<Integer> machFail=SingletonClass.getMachineFailures();
            for (int i=0;i<machFail.size();i++)//length;i++)
                mButtonList.get(machFail.get(i)).setBackgroundResource(R.drawable.ic_hitweb);
        }
    }

    private void processShips(List<ShipCoord> mShips, View v)
    {
        ShipCoord mShip1 = mShips.get(0);
        ShipCoord mShip2 = mShips.get(1);
        ShipCoord mShip3 = mShips.get(2);
        ShipCoord mShip4 = mShips.get(3);

        for (int i = 0; i < mShips.size(); i++) {
            switch (mShips.get(i).getId()) {
                case R.id.ship_one:
                    mSelectedIndexes.add(mShips.get(i).getId());
                    mButtonList.get(mButtons[mShips.get(i).getx1()][mShips.get(i).gety1()]).setBackgroundColor(getResources().getColor(R.color.black));
                    mSelectedIndexes.add(mButtons[mShips.get(i).getx1()][mShips.get(i).gety1()]);
                    mSelectedIndexes.add(getResources().getColor(R.color.black));
                    break;
                case R.id.ship_two:
                    mSelectedIndexes.add(mShips.get(i).getId());
                    mSelectedIndexes.add(mButtons[mShips.get(i).getx1()][mShips.get(i).gety1()]);
                    mSelectedIndexes.add(mButtons[mShips.get(i).getx1()][mShips.get(i).gety1() + 1]);
                    mSelectedIndexes.add(getResources().getColor(R.color.yellow));
                    mButtonList.get(mButtons[mShips.get(i).getx1()][mShips.get(i).gety1()]).setBackgroundColor(getResources().getColor(R.color.yellow));
                    mButtonList.get(mButtons[mShips.get(i).getx1()][mShips.get(i).gety1()+1]).setBackgroundColor(getResources().getColor(R.color.yellow));
                    break;
                case R.id.ship_three:
                    mSelectedIndexes.add(mShips.get(i).getId());
                    mButtonList.get(mButtons[mShips.get(i).getx1()][mShips.get(i).gety1()]).setBackgroundColor(getResources().getColor(R.color.blue));
                    mSelectedIndexes.add(mButtons[mShips.get(i).getx1()][mShips.get(i).gety1()]);
                    mButtonList.get(mButtons[mShips.get(i).getx1()][mShips.get(i).gety1() + 1]).setBackgroundColor(getResources().getColor(R.color.blue));
                    mSelectedIndexes.add(mButtons[mShips.get(i).getx1()][mShips.get(i).gety1() + 1]);
                    mButtonList.get(mButtons[mShips.get(i).getx1()][mShips.get(i).gety1() + 2]).setBackgroundColor(getResources().getColor(R.color.blue));
                    mSelectedIndexes.add(mButtons[mShips.get(i).getx1()][mShips.get(i).gety1() + 2]);
                    mSelectedIndexes.add(getResources().getColor(R.color.blue));
                    break;
                case R.id.ship_four:
                    mSelectedIndexes.add(mShips.get(i).getId());
                    mButtonList.get(mButtons[mShips.get(i).getx1()][mShips.get(i).gety1()]).setBackgroundColor(getResources().getColor(R.color.red));
                    mSelectedIndexes.add(mButtons[mShips.get(i).getx1()][mShips.get(i).gety1()]);
                    mButtonList.get(mButtons[mShips.get(i).getx1()][mShips.get(i).gety1() + 1]).setBackgroundColor(getResources().getColor(R.color.red));
                    mSelectedIndexes.add(mButtons[mShips.get(i).getx1()][mShips.get(i).gety1() + 1]);
                    mButtonList.get(mButtons[mShips.get(i).getx1()][mShips.get(i).gety1() + 2]).setBackgroundColor(getResources().getColor(R.color.red));
                    mSelectedIndexes.add(mButtons[mShips.get(i).getx1()][mShips.get(i).gety1() + 2]);
                    mButtonList.get(mButtons[mShips.get(i).getx1()][mShips.get(i).gety1() + 3]).setBackgroundColor(getResources().getColor(R.color.red));
                    mSelectedIndexes.add(mButtons[mShips.get(i).getx1()][mShips.get(i).gety1() + 3]);
                    mSelectedIndexes.add(getResources().getColor(R.color.red));
                    break;
                case R.id.ship_two_b:
                    mSelectedIndexes.add(mShips.get(i).getId());
                    mButtonList.get(mButtons[mShips.get(i).getx1()][mShips.get(i).gety1()]).setBackgroundColor(getResources().getColor(R.color.yellow));
                    mSelectedIndexes.add(mButtons[mShips.get(i).getx1()][mShips.get(i).gety1()]);
                    mButtonList.get(mButtons[mShips.get(i).getx1() + 1][mShips.get(i).gety1()]).setBackgroundColor(getResources().getColor(R.color.yellow));
                    mSelectedIndexes.add(mButtons[mShips.get(i).getx1() + 1][mShips.get(i).gety1()]);
                    mSelectedIndexes.add(getResources().getColor(R.color.yellow));
                    break;
                case R.id.ship_three_b:
                    mSelectedIndexes.add(mShips.get(i).getId());
                    mButtonList.get(mButtons[mShips.get(i).getx1()][mShips.get(i).gety1()]).setBackgroundColor(getResources().getColor(R.color.blue));
                    mSelectedIndexes.add(mButtons[mShips.get(i).getx1()][mShips.get(i).gety1()]);
                    mButtonList.get(mButtons[mShips.get(i).getx1() + 1][mShips.get(i).gety1()]).setBackgroundColor(getResources().getColor(R.color.blue));
                    mSelectedIndexes.add(mButtons[mShips.get(i).getx1() + 1][mShips.get(i).gety1()]);
                    mButtonList.get(mButtons[mShips.get(i).getx1() + 2][mShips.get(i).gety1()]).setBackgroundColor(getResources().getColor(R.color.blue));
                    mSelectedIndexes.add(mButtons[mShips.get(i).getx1() + 2][mShips.get(i).gety1()]);
                    mSelectedIndexes.add(getResources().getColor(R.color.blue));
                    break;
                case R.id.ship_four_b:
                    mSelectedIndexes.add(mShips.get(i).getId());
                    mButtonList.get(mButtons[mShips.get(i).getx1()][mShips.get(i).gety1()]).setBackgroundColor(getResources().getColor(R.color.red));
                    mSelectedIndexes.add(mButtons[mShips.get(i).getx1()][mShips.get(i).gety1()]);
                    mButtonList.get(mButtons[mShips.get(i).getx1() + 1][mShips.get(i).gety1()]).setBackgroundColor(getResources().getColor(R.color.red));
                    mSelectedIndexes.add(mButtons[mShips.get(i).getx1() + 1][mShips.get(i).gety1()]);
                    mButtonList.get(mButtons[mShips.get(i).getx1() + 2][mShips.get(i).gety1()]).setBackgroundColor(getResources().getColor(R.color.red));
                    mSelectedIndexes.add(mButtons[mShips.get(i).getx1() + 2][mShips.get(i).gety1()]);
                    mButtonList.get(mButtons[mShips.get(i).getx1() + 3][mShips.get(i).gety1()]).setBackgroundColor(getResources().getColor(R.color.red));
                    mSelectedIndexes.add(mButtons[mShips.get(i).getx1() + 3][mShips.get(i).gety1()]);
                    mSelectedIndexes.add(getResources().getColor(R.color.red));
                    break;

            }
        }
            //vble "continue" 0 means this will be starting a game, 1 means will resume
        if (getActivity().getIntent().getBooleanExtra("GAME_IS_ON",false))
            SingletonClass.setShipsOnDatabase(mSelectedIndexes,1);//,v
        else
            SingletonClass.setShipsOnDatabase(mSelectedIndexes,0);//,v
    }
    public static List<Button> retrieveYOUShipsButtons()
    {
        return mButtonList;
    }

    //USE TO HIT SHIP
    public static void hitYOUShipsIndexes(int index, Drawable drawable)//use to KILL ship
    {
        mButtonList.get(index).setBackground(drawable);
    }

    public static void changeYOUShipsIndexes(int[] indexes, Drawable drawable)//use to KILL ship
    {
        int num=-1;
        for (int i=0;i<indexes.length;i++)
        {
            if (indexes[i]!=-1)
            {
                num=indexes[i];
                mButtonList.get(num).setBackground(drawable);
            }
        }
    }
    public static void showTextAbout(boolean opt)
    {
        if (opt==true)
            mTextViewAbout.setVisibility(View.VISIBLE);
    }
    private int getColor(ShipCoord ship)
    {
        int color=0;
        //if its ship1
        if (ship.getPositionsX().size()==1 && ship.getPositionsY().size()==1)
            color= getResources().getColor(R.color.black);

        if (ship.getPositionsY().size()==2 || ship.getPositionsX().size()==2)
            color=getResources().getColor(R.color.blue);

        if (ship.getPositionsX().size()==3 || ship.getPositionsY().size()==3)
            color=getResources().getColor(R.color.yellow);

        if (ship.getPositionsX().size()==4 || ship.getPositionsY().size()==4)
            color=getResources().getColor(R.color.red);
        return color;
    }
}
