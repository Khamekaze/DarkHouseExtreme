package com.bam.darkhouseextreme.app.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.bam.darkhouseextreme.app.R;
import com.bam.darkhouseextreme.app.fragments.FirstroomFragment;

/**
 * Created by Chobii on 28/04/15.
 */
public class GameActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameactivity);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.gamelayout, new FirstroomFragment(), "firstroom").commit();

    }

    @Override
    public void overridePendingTransition(int enterAnim, int exitAnim) {
        super.overridePendingTransition(enterAnim, exitAnim);
    }
}
