package com.bam.darkhouseextreme.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import com.bam.darkhouseextreme.app.R;
import com.bam.darkhouseextreme.app.fragments.StartScreenFragment;
import com.bam.darkhouseextreme.app.utilities.SaveUtility;


public class StartScreenActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SaveUtility.setHelper(getApplicationContext());

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.startactivity);
        StartScreenFragment fragment = new StartScreenFragment();
        if (savedInstanceState == null) {
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

    //
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("Test", "got here");
        Log.d("test", String.valueOf(requestCode) + ", " + String.valueOf(resultCode));
        if (resultCode == 1) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.startscreenlayout, getSupportFragmentManager().findFragmentByTag("startScreen"))
                            .commitAllowingStateLoss();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        StartScreenFragment fragment = (StartScreenFragment) getSupportFragmentManager().findFragmentByTag("startScreen");
        if (fragment.isVisible()) {
            finish();
        }
    }
}
