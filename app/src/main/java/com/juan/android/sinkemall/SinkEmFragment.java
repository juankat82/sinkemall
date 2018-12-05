package com.juan.android.sinkemall;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import java.security.SecureRandom;

public class SinkEmFragment extends Fragment {

    private static final String SESSION_ID ="session_id" ;
    private static final String MUSIC_POSSITION ="music_positon" ;
    private ImageView mImageView;
    private TextView mTextViewClickToStart;
    private boolean introIsPlaying=true;
    private View v;
    private SoundPool mSoundPool;
    private int bellId;
    private MediaPlayer mMediaPlayer;


    public static SinkEmFragment newFragment()
    {
        return new SinkEmFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mMediaPlayer = MediaPlayer.create(getContext(), R.raw.uncompressed_intro);

        if (savedInstanceState!=null)
        {
            mMediaPlayer.setAudioSessionId(savedInstanceState.getInt(SESSION_ID));
            mMediaPlayer.seekTo((int)savedInstanceState.getLong(MUSIC_POSSITION));
        }

        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
        mSoundPool=new SoundPool(5, AudioManager.STREAM_MUSIC,0);
    }

    public int soundLoad(int id)
    {
        int resId=0;
        switch(id)
        {
            case 0:
                resId=mSoundPool.load(getContext(),R.raw.uncompressed_intro,1);
                break;
            case 1:
                resId=mSoundPool.load(getContext(),R.raw.bell,1);
                break;
        }

        return resId;

    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {


            v = inflater.inflate(R.layout.startup_animation_layout, container, false);//creates view
            v.setBackgroundColor(getResources().getColor(R.color.black));

            mImageView = (ImageView) v.findViewById(R.id.intro_image_view); //create imageview
            mImageView.setBackgroundColor(getResources().getColor(R.color.black));
            mImageView.setBackgroundResource(R.drawable.ships);//applies drawables to one single object
            mTextViewClickToStart = (TextView) v.findViewById(R.id.click_start_text);
                final AnimationDrawable animationDrawable = (AnimationDrawable) mImageView.getBackground();//gets the compiled drawable
                animationDrawable.start();//starts the animation
                //delayes the fade_in_out for 7 secs
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fade();
                    }
                }, 7000L);

            //delayes the appearing of the CLICK TO START title for 13 secs
            v.postDelayed(new Runnable() {
                @Override
                public void run() {
                    introIsPlaying = false;
                    showClickToStartLetters();
                }
            }, 10000L);

            mTextViewClickToStart.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {

                    bellId = soundLoad(1);
                    MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.bell);
                    mp.start();
                    fadeToMainMenu();
                }
            });
        return v;
    }

    public boolean isIntroPlaying()
    {
        return introIsPlaying;
    }
////////////////////////////////    FADERS    ///////////////////////////////////////
    private void fadeToMainMenu()
    {
        final int fadingDuration=2000;//ms

        mImageView.animate().setDuration(fadingDuration).alpha(1f).setListener(null);
        mTextViewClickToStart.animate().setDuration(fadingDuration).alpha(1f).setListener(null);
        mTextViewClickToStart.animate().setDuration(fadingDuration).alpha(0f).setListener(null);
        mImageView.animate().setDuration(fadingDuration).alpha(0f).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mImageView.setVisibility(View.GONE);
               // v=null;
                mImageView.animate().setDuration(fadingDuration).alpha(1.0f).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                    }
                    @Override
                    public void onAnimationEnd(Animator animator)
                    {
                        super.onAnimationEnd(animator);
                        v.postInvalidate();
                        mSoundPool.stop(bellId);
                        mSoundPool.release();

                        Intent mainMenuIntent = MainMenuActivity.newIntent(getContext());
                        startActivity(mainMenuIntent);
                    }
                });
            }
        });
    }

    private void fade()
    {
          final int durationOfFading=2000;
        //FADE OUT
          mImageView.animate().setInterpolator(new DecelerateInterpolator()).setDuration(durationOfFading).alpha(1f).setListener(null);
          mImageView.animate().setInterpolator(new DecelerateInterpolator()).setDuration(durationOfFading).alpha(0f).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mImageView.setOnClickListener(null);
                    mImageView.setVisibility(View.GONE);
                    mImageView.setBackgroundResource(R.drawable.main);//changes the image but still invisible
                    mImageView.animate().setDuration(durationOfFading).alpha(1f).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            mImageView.setVisibility(View.VISIBLE);
                        }
                    });
                }
          });
    }

    //MAKES THE CLICK TO START LETTERS BLINK IN COLORS
    private void showClickToStartLetters()
    {
        final int[] colorArray= new int[]{Color.GRAY,Color.WHITE,Color.BLACK, Color.BLUE, Color.YELLOW, Color.RED, Color.GREEN};
        mTextViewClickToStart.setVisibility(View.VISIBLE);
        mTextViewClickToStart.postDelayed(new Runnable() {
            @Override
            public void run() {
                SecureRandom sr=new SecureRandom();
                mTextViewClickToStart.setTextColor(colorArray[sr.nextInt(colorArray.length)]);
                showClickToStartLetters();
            }
        },400L);
    }

    @Override
    public void onPause() {
        super.onPause();
        mMediaPlayer.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onDetach()
    {
        super.onDetach();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mMediaPlayer.release();
    }
    @Override
    public void onStop() {
        super.onStop();
        mMediaPlayer.stop();
        mMediaPlayer.getAudioSessionId();
        mMediaPlayer.release();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        if (isIntroPlaying()==true)
        {
            savedInstanceState.putInt(SESSION_ID,mMediaPlayer.getAudioSessionId());
            savedInstanceState.putLong(MUSIC_POSSITION,(long)mMediaPlayer.getCurrentPosition());
        }
    }
}
