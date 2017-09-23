/*
 * This file is part of kfaryarok-android.
 *
 * kfaryarok-android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * kfaryarok-android is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with kfaryarok-android.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.kfaryarok.kfaryarokapp.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.github.kfaryarok.kfaryarokapp.updates.api.ClassesAffected;
import io.github.kfaryarok.kfaryarokapp.updates.api.Update;
import io.github.kfaryarok.kfaryarokapp.updates.api.UpdateImpl;
import io.github.kfaryarok.kfaryarokapp.updates.UpdateParser;

/**
 * Various utils for testing the app.
 * Mostly for working with parts of the app that aren't there yet, so for
 * providing test data (ie. json-formatted updates).
 *
 * @author tbsc on 05/03/2017
 */
public class TestUtil {

    private static String testJsonString = null;
    private static Update[] testUpdateArray = new Update[0];

    static {
        // instead of creating an array every time it's needed or to create the json, cache everything

        JSONObject testJson = new JSONObject();
        try {
            JSONObject globalUpdate1 = new JSONObject().put(UpdateParser.TEXT_STRING, "טקס יום הזיכרון בשעה 9:30 בחדר האוכל הןןחלח חל רהחילריהח לירקי הרק הרק החרהירחה רחהי חה חרל החר יהרה חיחי חיחי");
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

        testJsonString = testJson.toString();

        testUpdateArray = new Update[] {
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
        return testJsonString;
    }

    /**
     * @return array of useless updates
     */
    public static Update[] getTestUpdateArray() {
        return testUpdateArray;
    }

}
