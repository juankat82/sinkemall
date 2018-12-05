package com.juan.android.sinkemall;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.zip.Inflater;

/**
 * Created by juan on 02/01/18.
 */

public class MyFinalBoard extends AppCompatActivity {

    private static Context mContext;
    private static ViewPager mViewPager;
    private MyFinalBoardFragment mMyFinalBoardFragment;
    private AttackBoard mAttackBoard;
    private MediaPlayer mMediaPlayer;
    private final String SESSION_ID="session_id";
    private final String MUSIC_POSSITION="position";
    private final String TAG="MyBoard";

    public static Intent newIntent(Context context)
    {
        mContext=context;
        Intent intent=new Intent(context,MyFinalBoard.class);
        return intent;
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        FragmentManager fm=getSupportFragmentManager();

        mMyFinalBoardFragment=MyFinalBoardFragment.newFragment(mContext);

        mAttackBoard=AttackBoard.newFragment(mContext);

        setContentView(R.layout.main_game_view_pager);
        mMediaPlayer = MediaPlayer.create(mContext, R.raw.game_bgm);
        if (savedInstanceState!=null)
        {
            mMediaPlayer.setAudioSessionId(savedInstanceState.getInt(SESSION_ID));
            mMediaPlayer.seekTo((int)savedInstanceState.getLong(MUSIC_POSSITION));
        }
        mMediaPlayer.setLooping(true);
        if (!SingletonClass.isAudioOff())
            mMediaPlayer.start();

        mViewPager=(ViewPager) findViewById(R.id.main_view_pager);
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                if (position==0)
                    return mMyFinalBoardFragment;
                else {
                    return mAttackBoard;
                }
            }
            @Override
            public int getCount() {
                return 2;
            }
        });
    }

    public static void changeViewPager(int page)
    {
        mViewPager.setCurrentItem(page);
    }
    @Override
    public void onBackPressed()
    {
        if (SingletonClass.isGameStarted())
        {
            SingletonClass.setGameHeld(true);//means will set it to HELD
        }

        Intent intent=MainMenuActivity.newIntent(mContext);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
    }
    @Override
    protected void onStop()
    {
        super.onStop();
    }
    @Override
    protected void onDestroy() {
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
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // show menu when menu button is pressed
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.disable_video_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        mMediaPlayer.getAudioSessionId();
        mMediaPlayer.getCurrentPosition();
        switch (item.getItemId()) {
            case R.id.about_item:
                if (mViewPager.getCurrentItem()==0)
                {
                    MyFinalBoardFragment.showTextAbout(true);
                }
                else
                {
                    mAttackBoard.showTextAbout(true);
                }
                return true;
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
                return true;
            case R.id.disable_video:
                item.setChecked(!item.isChecked());
                SingletonClass.setVideoOff(!SingletonClass.isVideoOff());
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
