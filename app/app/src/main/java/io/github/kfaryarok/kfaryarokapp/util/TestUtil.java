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
            JSONObject globalUpdate1 = new JSONObject().put(UpdateParser.TEXT_STRING, "טקס יום הזיכרון בשעה 9:30 בחדר האוכל הןןחלח חל רהחילריהח לירקי הרק הרק החרהירחה רחהי חה חרל החר יהרה");
            JSONArray globalUpdates = new JSONArray().put(globalUpdate1);

            JSONObject update1 = new JSONObject().put(UpdateParser.TEXT_STRING, "שיעור 7 מבוטל").put(UpdateParser.CLASSES_ARRAY, new JSONArray().put("ח4"));
            JSONObject update2 = new JSONObject().put(UpdateParser.TEXT_STRING, "שיעור 5 עם משה").put(UpdateParser.CLASSES_ARRAY, new JSONArray().put("ט2"));
            JSONObject update3 = new JSONObject().put(UpdateParser.TEXT_STRING, "שיעורים 1-2 מבוטלים").put(UpdateParser.CLASSES_ARRAY, new JSONArray().put("י3"));
            JSONObject update4 = new JSONObject().put(UpdateParser.TEXT_STRING, "שיעור 11 עם בני").put(UpdateParser.CLASSES_ARRAY, new JSONArray().put("יא11"));
            JSONArray updatesJson = new JSONArray().put(update1).put(update2).put(update3).put(update4);

            testJson.put(UpdateParser.GLOBAL_UPDATES_ARRAY, globalUpdates);
            testJson.put(UpdateParser.UPDATES_ARRAY, updatesJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mTestJsonString = testJson.toString();

        mTestUpdateArray = new Update[] {
                new UpdateImpl("שלום"),
                new UpdateImpl("test"),
                new UpdateImpl(new String[] {
                        "H4", "I10", "H5", "I10", "K1", "G5", "G4", "G3", "G2", "K11", "K10", "H3"
                }, "private!"),
                new UpdateImpl("J3", "כלום!"),
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
