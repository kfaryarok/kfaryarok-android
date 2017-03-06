package io.github.kfaryarok.kfaryarokapp.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.github.kfaryarok.kfaryarokapp.updates.ClassesAffected;
import io.github.kfaryarok.kfaryarokapp.updates.Update;
import io.github.kfaryarok.kfaryarokapp.updates.UpdateImpl;
import io.github.kfaryarok.kfaryarokapp.updates.UpdateParser;

/**
 * Various utils for testing the app.
 * Mostly for working with parts of the app that aren't there yet, so for
 * providing test data (ie. json-formatted updates).
 *
 * @author tbsc on 05/03/2017
 */
public class TestUtil {

    private static String mTestJsonString = null;
    private static Update[] mTestUpdateArray = new Update[0];

    static {
        // instead of creating an array every time it's needed or to create the json, cache everything

        JSONObject testJson = new JSONObject();
        try {
            JSONObject globalUpdate1 = new JSONObject().put(UpdateParser.SHORT_TEXT_STRING, "hi?").put(UpdateParser.LONG_TEXT_STRING, "more hi");
            JSONObject globalUpdate2 = new JSONObject().put(UpdateParser.SHORT_TEXT_STRING, "hi again");
            JSONArray globalUpdates = new JSONArray().put(globalUpdate1).put(globalUpdate2);

            JSONObject update1 = new JSONObject().put(UpdateParser.SHORT_TEXT_STRING, "normal").put(UpdateParser.CLASSES_ARRAY, new JSONArray().put("H4").put("I3")).put(UpdateParser.LONG_TEXT_STRING, "long normal");
            JSONObject update2 = new JSONObject().put(UpdateParser.SHORT_TEXT_STRING, "normal 2").put(UpdateParser.CLASSES_ARRAY, new JSONArray().put("I2").put("K7")).put(UpdateParser.LONG_TEXT_STRING, "long normal 2");
            JSONArray updatesJson = new JSONArray().put(update1).put(update2);

            testJson.put(UpdateParser.GLOBAL_UPDATES_ARRAY, globalUpdates);
            testJson.put(UpdateParser.UPDATES_ARRAY, updatesJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mTestJsonString = testJson.toString();

        mTestUpdateArray = new Update[] {
                new UpdateImpl("שלום", "בדיקה, כי אפילו שאני לא יודע מה אני רוצה לומר אני כותב את זה כאן, ואני רוצה שפשוט תלך החוצה ותצעק על בוטס שהוא רוצה לאכול אותי!"),
                new UpdateImpl("test", "בדיקה, כי אפילו שאני לא יודע מה אני רוצה לומר אני כותב את זה כאן, ואני רוצה שפשוט תלך החוצה ותצעק על בוטס שהוא רוצה לאכול אותי!"),
                new UpdateImpl(new String[] {
                        "H4", "I10", "H5", "I10", "K1", "G5", "G4", "G3", "G2", "K11", "K10", "H3"
                }, "private!", "בדיקה, כי אפילו שאני לא יודע מה אני רוצה לומר אני כותב את זה כאן, ואני רוצה שפשוט תלך החוצה ותצעק על בוטס שהוא רוצה לאכול אותי!"),
                new UpdateImpl(new ClassesAffected("L69"), "test 1"),
                new UpdateImpl("hi"),
                new UpdateImpl("fua")
        };
    }

    /**
     * @return "useless" JSON string for testing parser
     */
    public static String getTestJsonString() {
        return mTestJsonString;
    }

    /**
     * @return array of useless updates
     */
    public static Update[] getTestUpdateArray() {
        return mTestUpdateArray;
    }

}
