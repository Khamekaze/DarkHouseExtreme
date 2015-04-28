package com.bam.darkhouseextreme.app.utilities;

import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Chobii on 28/04/15.
 */
public class Utilities {

    private static final String LOG_DATA = Utilities.class.getSimpleName();

    public static void setFontsForLayout(ViewGroup layout, Typeface fonts) {

        if (fonts != null && layout != null) {
            try {
                for (int i = 0; i < layout.getChildCount(); ++i) {
                    setFontForView(layout.getChildAt(i), fonts);
                }
            } catch (Exception e) {
                Log.d(LOG_DATA, "Not a viable ViewGroup");
            }
        }
    }

    public static void setFontForView(View view, Typeface fonts) {

        if (fonts != null && view != null) {
            try {
                if (view instanceof TextView) {
                    ((TextView) view).setTypeface(fonts);
                } else if (view instanceof ViewGroup) {
                    setFontsForLayout((ViewGroup) view, fonts);
                }
            } catch (Exception e) {
                Log.d(LOG_DATA, "Not a viable View");
            }
        }
    }
}
