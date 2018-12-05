package com.juan.android.sinkemall;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juan.android.database.GameOnWrapper;

import org.w3c.dom.Text;

import java.util.zip.Inflater;

/**
 * Created by juan on 15/11/17.
 */

public class MainGameActivity extends AppCompatActivity{

    private static final String MEDIA_PLAYER_SESSION_ID = "media_player_session_id";
    private static final String MUSIC_POSITION = "sound_track_position";
    private static SingletonClass sSingletonClass;
    private static Context mContext;
    private static MediaPlayer mMediaPlayer;
    private ViewPager mViewPager;
    private MyBoard sMyBoard;
    private static AttackBoard sAttackBoard;

    public static Intent newIntent(Context context,SingletonClass singletonClass)
    {
        sSingletonClass=SingletonClass.get(context);
        mContext=context;
        Intent intent=new Intent(context,MainGameActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        return intent;
    }


    public static MediaPlayer getMediaPlayer()
    {
        return mMediaPlayer;
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mMediaPlayer = MediaPlayer.create(mContext, R.raw.game_bgm);//CHANGE THIS!!!!!!!!!!!!!
        mMediaPlayer.setVolume(2.0f, 2.0f);
        mMediaPlayer.setLooping(true);
        sMyBoard= MyBoard.newFragment(mContext);
        if (!SingletonClass.isAudioOff())
            mMediaPlayer.start();
        setContentView(R.layout.main_game_view_pager);

        mViewPager=(ViewPager) findViewById(R.id.main_view_pager);

        FragmentManager fm=getSupportFragmentManager();

        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                    return sMyBoard;
            }
            @Override
            public int getCount() {
                return 1;
            } //return 2
        });
    }

    public static AttackBoard getAttackBoard(){
        return sAttackBoard;
    }
    @Override
    public void onResume()
    {
        super.onResume();
        if (!SingletonClass.isAudioOff())
            mMediaPlayer.start();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mMediaPlayer.pause();
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(MEDIA_PLAYER_SESSION_ID, mMediaPlayer.getAudioSessionId());
        savedInstanceState.putLong(MUSIC_POSITION, (long) mMediaPlayer.getCurrentPosition());
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mMediaPlayer.stop();
        mMediaPlayer.getAudioSessionId();
        mMediaPlayer.release();
        //implement recording on database when app gets killed
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.about_menu, menu);
        MenuItem bt=menu.getItem(1);
        if (SingletonClass.isAudioOff())
            bt.setChecked(true);
        else
            bt.setChecked(false);
        return true;
    }
}
