package com.bam.darkhouseextreme.app;

import android.content.Context;
import android.graphics.Typeface;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;
import com.bam.darkhouseextreme.app.activities.StartScreenActivity;
import com.bam.darkhouseextreme.app.utilities.Utilities;

/**
 * Created by Chobii on 28/04/15.
 */
public class FontTesting extends ActivityInstrumentationTestCase2<StartScreenActivity>{


    public Context context;
    public TextView txt;

    public FontTesting() {
        super(StartScreenActivity.class);


    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        context = getInstrumentation().getContext();
        txt = new TextView(context);
        txt.setText("Test");

    }

    public void testFont() {
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/MISFITS_.TTF");
        Utilities.setFontForView(txt, font);
        assertEquals(font, txt.getTypeface());
    }
}
