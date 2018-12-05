package com.juan.android.sinkemall;

import android.support.v4.app.Fragment;

public class SinkEmAllActivity extends SingleFragment {
    @Override
    public Fragment createFragment() {
        return SinkEmFragment.newFragment();
    }
}
