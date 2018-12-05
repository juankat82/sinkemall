package com.juan.android.sinkemall;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by juan on 02/11/17.
 */

public class MainMenuActivity extends AppCompatActivity {


    private static Context mContext;

    public static Intent newIntent(Context context)
    {
        Intent intent=new Intent(context,MainMenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mContext=context;
        return intent;
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_layout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Fragment mainMenuFragment=MainMenuFragment.newInstance(mContext);
        FragmentManager fm=getSupportFragmentManager();
        fm.beginTransaction().add(R.id.main_menu_linearlayout, mainMenuFragment).commit();
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

}
