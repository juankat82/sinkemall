package com.juan.android.sinkemall;


import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by juan on 26/10/17.
 */

public abstract class SingleFragment extends AppCompatActivity{

    public abstract Fragment createFragment();

    @Override
    public void onCreate(Bundle savedStateInstance)
    {
        super.onCreate(savedStateInstance);
        setContentView(getLayoutResId());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        FragmentManager fm=getSupportFragmentManager();
        Fragment fragm=fm.findFragmentById(R.id.linear_layout_base);
        if (fragm==null)
            fragm=createFragment();

        fm.beginTransaction().add(R.id.linear_layout_base,fragm).commit();
    }
    @LayoutRes
    protected int getLayoutResId()
    {
        return R.layout.base_fragment_layout;
    }
}
