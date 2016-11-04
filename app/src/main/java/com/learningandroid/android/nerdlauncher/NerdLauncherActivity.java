package com.learningandroid.android.nerdlauncher;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NerdLauncherActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){
        return NerdLauncherFragment.newInstance();
    }
}
