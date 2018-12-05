package com.juan.android.sinkemall;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by juan on 11/11/17.
 */

public class HallOfFameFragment extends Fragment {

    private MediaPlayer mMediaPlayer;
    private static SingletonClass mSingletonClass;
    private TextView mAboutTextView;
    private TextView mTextView;
    private TextView mTextView1;
    private TextView mTextView2;
    private TextView mTextView3;
    private TextView mTextView4;
    private TextView mTextView5;
    private List<Ranks> mRanksList;

    public static Fragment newFragment(Context context,SingletonClass singletonClass)
    {
        mSingletonClass=singletonClass;
        return new HallOfFameFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mMediaPlayer=MediaPlayer.create(getContext(),R.raw.whisky);
        mMediaPlayer.setVolume(2.0f,2.0f);
        mMediaPlayer.setLooping(true);
        if (!SingletonClass.isAudioOff())
            mMediaPlayer.start();
        mRanksList=new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View v=inflater.inflate(R.layout.hall_of_fame_layout,container,false);
        v.setBackground(getResources().getDrawable(R.drawable.hall_of_fame_background));
        mAboutTextView=v.findViewById(R.id.about_text_view_halloffame);
        mAboutTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mAboutTextView.setVisibility(View.GONE);
            }
        });
        //makes underlined text//
        mTextView=(TextView) v.findViewById(R.id.text_view);
        mTextView.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
//        SpannableString ss=new SpannableString(getResources().getString(R.string.hall_of_fame_string));
//        ss.setSpan(new UnderlineSpan(),0,mTextView.length(), 0);
//        mTextView.setText(ss);

        mTextView1=(TextView) v.findViewById(R.id.text_view_1);
        mTextView2=(TextView) v.findViewById(R.id.text_view_2);
        mTextView3=(TextView) v.findViewById(R.id.text_view_3);
        mTextView4=(TextView) v.findViewById(R.id.text_view_4);
        mTextView5=(TextView) v.findViewById(R.id.text_view_5);
        mRanksList=mSingletonClass.queryRecords(null,null);

        List<String> strings=new ArrayList<>();
        for (int i=0;i<mRanksList.size();i++)
        {
            Ranks ranks=mRanksList.get(i);
            strings.add("Pos: "+ranks.getId()+" NAME: "+ranks.getName()+ " POINTS: "+ranks.getPoints());
        }

        mTextView1.setText(strings.get(0));
        mTextView2.setText(strings.get(1));
        mTextView3.setText(strings.get(2));
        mTextView4.setText(strings.get(3));
        mTextView5.setText(strings.get(4));
        return v;
    }
    @Override
    public void onPause()
    {
        super.onPause();
        mMediaPlayer.pause();
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mMediaPlayer.pause();
        mMediaPlayer.release();
        Fragment fr=MainMenuFragment.newInstance(getContext());
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_menu_linearlayout,fr).commit();
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
        inflater.inflate(R.menu.disable_audio_menu,menu);
        MenuItem bt=menu.getItem(0);
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
            case R.id.about_record_fragment_menutitem:
                mAboutTextView.setVisibility(View.VISIBLE);
                return true;
            case R.id.disable_audio_records:
                item.setChecked(!item.isChecked());

                if (item.isChecked()) {
                    mMediaPlayer.pause();
                    SingletonClass.setAudioOff(true);
                }
                else
                {
                    SingletonClass.setAudioOff(false);
                    mMediaPlayer.getCurrentPosition();
                    mMediaPlayer.start();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
