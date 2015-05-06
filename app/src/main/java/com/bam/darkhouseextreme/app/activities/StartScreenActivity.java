package com.bam.darkhouseextreme.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.view.WindowManager;
import com.bam.darkhouseextreme.app.R;
import com.bam.darkhouseextreme.app.fragments.StartScreenFragment;
import com.bam.darkhouseextreme.app.utilities.SaveUtility;


public class StartScreenActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (SaveUtility.helper == null) {
            SaveUtility.setHelper(getApplicationContext());
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.startactivity);
        if (savedInstanceState == null) {
            StartScreenFragment fragment = new StartScreenFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.startscreenlayout, fragment, "startScreen")
                    .addToBackStack("StartScreen")
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            final StartScreenFragment fragment = (StartScreenFragment) getSupportFragmentManager().findFragmentByTag("startScreen");
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.back_enter, R.anim.back_exit);
            transaction.replace(R.id.startscreenlayout, fragment);
            transaction.commitAllowingStateLoss();

        }
    }

    @Override
    public void onBackPressed() {
        final StartScreenFragment fragment = (StartScreenFragment) getSupportFragmentManager().findFragmentByTag("startScreen");
        if (fragment.isVisible()) {
            finish();
        } else {

                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.back_enter, R.anim.back_exit);
                    transaction.replace(R.id.startscreenlayout, fragment);
                    transaction.commit();
//                    getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.startscreenlayout, fragment)
//                            .setCustomAnimations(R.anim.enter, R.anim.exit)
//                            .commit();

//            super.onBackPressed();
        }
    }
}
