package com.juan.android.sinkemall;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.TimerTask;


/**
 * Created by juan on 08/11/17.
 */

public class MainMenuFragment extends Fragment {


    private static final String TAG ="MainMenuFragment" ;
    private MediaPlayer mMediaPlayer;
    private final String SESSION_ID="session_id";
    private final String MUSIC_POSSITION="position";
    private Button mNewGameButton;
    private Button mResumeGameButton;
    private Button mHallOfFameButton;
    private TextView mAboutTextView;
    private Activity chooserActivity;
    private SingletonClass mSingletonClass;
    private FragmentTransaction transaction;
    private FragmentManager fm;
    private Bundle savedInstance;
    private View v;
    private static boolean isGameStarted;

    public static Fragment newInstance(Context context)
    {
        return new MainMenuFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        savedInstance=savedInstanceState;
        setHasOptionsMenu(true);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        isGameStarted=false;

        mSingletonClass=SingletonClass.get(getActivity());//it was in HALL OF FAME SECTION

        fm=getActivity().getSupportFragmentManager();

        transaction=fm.beginTransaction();
            mMediaPlayer = MediaPlayer.create(getContext(), R.raw.drums);
            if (savedInstanceState!=null)
            {
                mMediaPlayer.setAudioSessionId(savedInstanceState.getInt(SESSION_ID));
                mMediaPlayer.seekTo((int)savedInstanceState.getLong(MUSIC_POSSITION));
            }
            mMediaPlayer.setLooping(true);
            if (!SingletonClass.isAudioOff())
                mMediaPlayer.start();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        v=inflater.inflate(R.layout.main_menu_fragment_layout,container,false);
        mAboutTextView=v.findViewById(R.id.about_button_final);
        v.setBackground(getResources().getDrawable(R.drawable.main_menu_background));
        mNewGameButton=(Button)v.findViewById(R.id.new_game_button);
        mResumeGameButton=(Button)v.findViewById(R.id.resume_button);
        mHallOfFameButton=(Button)v.findViewById(R.id.hall_of_fame_button);
        FragmentManager fm=getFragmentManager();
        isGameStarted=SingletonClass.isGameStarted();//getIsHeldGame();
        mNewGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (isGameStarted==true)
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setCancelable(false);
                        builder.setTitle("Continue old game? \nNO will start a new game");
                        builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                           resumeGame();
                                    }
                                });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startNewGame();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                    else {

                        startNewGame();
                        isGameStarted=SingletonClass.isGameStarted();
                    }
            }
        });

        mResumeGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isGameStarted==true)
                    resumeGame();
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setCancelable(false);
                    builder.setTitle("No previous games were detected, start new game?");
                    builder.setPositiveButton("YES",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startNewGame();
                        }
                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });
        mHallOfFameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment hallOfFameFragment=HallOfFameFragment.newFragment(getActivity(),mSingletonClass);
                transaction.replace(R.id.main_menu_linearlayout,hallOfFameFragment);
                transaction.addToBackStack("mainMenuFragment");
                transaction.commit();
            }
        });
        return v;
    }

    private void resumeGame()
    {
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                for (float i=1.0f;i>=0.0;i--)
                    v.setAlpha(i);
            }
        };
        Intent intent = MyFinalBoard.newIntent(getContext());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    private void startNewGame()
    {
        SingletonClass.startNewGame();
        Intent intent = MainGameActivity.newIntent(getContext(), mSingletonClass);
        startActivity(intent);
    }
    /////////////////////////////////////////////////////////////////////////////
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mMediaPlayer.stop();
        mMediaPlayer.getAudioSessionId();
        mMediaPlayer.release();
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(SESSION_ID,mMediaPlayer.getAudioSessionId());
        savedInstanceState.putLong(MUSIC_POSSITION,(long)mMediaPlayer.getCurrentPosition());
    }
    @Override
    public void onPause()
    {
        super.onPause();
        mMediaPlayer.pause();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!SingletonClass.isAudioOff())
            mMediaPlayer.start();
        isGameStarted=SingletonClass.isGameStarted();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.about_menu,menu);
        MenuItem bt=menu.getItem(1);
        if (SingletonClass.isAudioOff())
            bt.setChecked(true);
        else
            bt.setChecked(false);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.about_item:
                mAboutTextView.setVisibility(View.VISIBLE);
                mAboutTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mAboutTextView.setVisibility(View.GONE);
                    }
                });
                //Make an TextView visible that takes the whole screen to show the music owners and name blahblah
                return true;
            case R.id.disable_audio:
                item.setChecked(!item.isChecked());
                if (item.isChecked()) {
                    SingletonClass.setAudioOff(true);
                    mMediaPlayer.stop();
                    mMediaPlayer.getAudioSessionId();
                }
                else
                {
                    mMediaPlayer = MediaPlayer.create(getContext(), R.raw.drums);
                    SingletonClass.setAudioOff(false);
                    if (savedInstance!=null)
                    {
                        mMediaPlayer.setAudioSessionId(savedInstance.getInt(SESSION_ID));
                        mMediaPlayer.seekTo((int)savedInstance.getLong(MUSIC_POSSITION));
                    }
                    mMediaPlayer.setLooping(true);
                    mMediaPlayer.start();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
