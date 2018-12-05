package com.juan.android.sinkemall;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.security.SecureRandom;
import java.util.List;

/**
 * Created by juan on 20/02/18.
 */

public class RecordEnterActivity extends AppCompatActivity {

    private static Context sContext;
    private int mPosition;
    private long mPoints;
    private MediaPlayer mMediaPlayer;
    private TextView mAboutTextView;
    private TextView mPositionTextView;
    private TextView mFirstLetter;
    private TextView mSecondLetter;
    private TextView mThirdLetter;
    private ImageView mFirstUp;
    private ImageView mSecondUp;
    private ImageView mThirdUp;
    private ImageView mFirstDown;
    private ImageView mSecondDown;
    private ImageView mThirdDown;
    private Button mIntroButton;
    private static final String POSITION="Position";
    private static final String POINTS="Points";

    public static Intent newIntent(Context context)
    {
        Intent intent=new Intent(context,RecordEnterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        sContext=context;
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_enter_fragment_layout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mMediaPlayer = MediaPlayer.create(sContext, R.raw.whisky);
        mMediaPlayer.setLooping(true);
        mAboutTextView=(TextView)findViewById(R.id.about_record_text_view);
        mAboutTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mAboutTextView.setVisibility(View.GONE);
            }
        });
        if (!SingletonClass.isAudioOff())
            mMediaPlayer.start();

        mPosition =getIntent().getIntExtra(POSITION, -1);
        mPoints = getIntent().getLongExtra(POINTS, 0);

        mPositionTextView= (TextView) findViewById(R.id.title_record_screen);
        mPositionTextView.setText(mPositionTextView.getText()+" #"+mPosition+"\nPoints earned: "+mPoints);

        mFirstLetter=(TextView)findViewById(R.id.first_record_letter);
        mSecondLetter=(TextView)findViewById(R.id.second_record_letter);
        mThirdLetter=(TextView)findViewById(R.id.third_record_letter);

        mFirstUp=(ImageView) findViewById(R.id.arrow_up_one);
        mSecondUp=(ImageView)findViewById(R.id.arrow_up_two);
        mThirdUp=(ImageView)findViewById(R.id.arrow_up_three);
        mFirstDown=(ImageView)findViewById(R.id.arrow_down_one);
        mSecondDown=(ImageView)findViewById(R.id.arrow_down_two);
        mThirdDown=(ImageView)findViewById(R.id.arrow_down_three);
        mIntroButton=(Button) findViewById(R.id.intro_record_button);

        //FIRST LETTER
        mFirstUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence seq=mFirstLetter.getText();
                int intCharacter=seq.charAt(0);
                if (intCharacter==90)
                    intCharacter=65;
                else
                    intCharacter++;

                char[] charChar={(char)intCharacter};
                String str=new String(charChar);
                mFirstLetter.setText(str);
            }
        });
        mFirstDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence seq=mFirstLetter.getText();
                int intCharacter=seq.charAt(0);
                if (intCharacter==65)
                    intCharacter=90;
                else
                    intCharacter--;

                char[] charChar={(char)intCharacter};
                String str=new String(charChar);
                mFirstLetter.setText(str);
            }
        });

        //SECOND LETTER
        mSecondUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence seq=mSecondLetter.getText();
                int intCharacter=seq.charAt(0);
                if (intCharacter==90)
                    intCharacter=65;
                else
                    intCharacter++;

                char[] charChar={(char)intCharacter};
                String str=new String(charChar);
                mSecondLetter.setText(str);
            }
        });
        mSecondDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence seq=mSecondLetter.getText();
                int intCharacter=seq.charAt(0);
                if (intCharacter==65)
                    intCharacter=90;
                else
                    intCharacter--;

                char[] charChar={(char)intCharacter};
                String str=new String(charChar);
                mSecondLetter.setText(str);
            }
        });

        //THIRDS LETTER
        mThirdUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence seq=mThirdLetter.getText();
                int intCharacter=seq.charAt(0);
                if (intCharacter==90)
                    intCharacter=65;
                else
                    intCharacter++;

                char[] charChar={(char)intCharacter};
                String str=new String(charChar);
                mThirdLetter.setText(str);
            }
        });
        mThirdDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence seq=mThirdLetter.getText();
                int intCharacter=seq.charAt(0);
                if (intCharacter==65)
                    intCharacter=90;
                else
                    intCharacter--;

                char[] charChar={(char)intCharacter};
                String str=new String(charChar);
                mThirdLetter.setText(str);
            }
        });

        mIntroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SecureRandom sc=new SecureRandom();
                String name=mFirstLetter.getText().toString().concat(mSecondLetter.getText().toString()).concat(mThirdLetter.getText().toString());
                Ranks rank=new Ranks();
                int[] colorList={R.color.black,R.color.blue,R.color.red,R.color.yellow,R.color.white,R.color.pink};
                rank.setId(mPosition);
                rank.setName(name);
                rank.setPoints(mPoints);
                rank.setMachineDefault(false);
                SingletonClass.setNewRecord(rank);    //now enter record in database
                Intent intent=MainMenuActivity.newIntent(sContext);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                CountDownTimer timer=new CountDownTimer(2000,200) {
                    @Override
                    public void onTick(long l) {
                        mPositionTextView.setTextColor(colorList[sc.nextInt(6)]);//changes to a random color then launches main menu
                    }
                    @Override
                    public void onFinish() {
                        startActivity(intent);
                    }
                }.start();
            }
        });
    }
    @Override
    public void onBackPressed()
    {
        //ENTER RECORD DEFAULT AAA
        String name=mFirstLetter.getText().toString().concat(mSecondLetter.getText().toString()).concat(mThirdLetter.getText().toString());
        Ranks rank=new Ranks();
        rank.setId(mPosition);
        rank.setName(name);
        rank.setPoints(mPoints);
        rank.setMachineDefault(false);
        SingletonClass.setNewRecord(rank);
        Intent intent=MainMenuActivity.newIntent(sContext);
        startActivity(intent);
        super.onBackPressed();
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
        mMediaPlayer.stop();
        mMediaPlayer.getAudioSessionId();
        mMediaPlayer.release();
    }
    @Override
    public void onResume()
    {
        super.onResume();
        if (!SingletonClass.isAudioOff())
            mMediaPlayer.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.disable_audio_record_input,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.about_record:
                mAboutTextView.setVisibility(View.VISIBLE);
                return true;
            case R.id.disable_audio_record_input:
                SingletonClass.setAudioOff(!SingletonClass.isAudioOff());
                if (SingletonClass.isAudioOff())
                    item.setChecked(true);
                else
                    item.setChecked(false);
                if (SingletonClass.isAudioOff())
                    mMediaPlayer.pause();
                else
                    mMediaPlayer.start();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
