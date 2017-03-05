package io.github.kfaryarok.kfaryarokapp.util;

import android.content.res.Resources;

/**
 * Various util methods for working with screen sizes.
 *
 * Created by tbsc on 04/03/2017.
 */
public class ScreenUtil {

    /**
     * Converts from pixels to DP.
     * @param px amount in pixels
     * @return amount in DP
     */
    public static float pxToDp(float px) {
        return px / Resources.getSystem().getDisplayMetrics().density;
    }

    /**
     * Converts from DP to pixels.
     * @param dp amount in DP
     * @return amount in pixels
     */
    public static float dpToPx(float dp) {
        return dp * Resources.getSystem().getDisplayMetrics().density;
    }

}
