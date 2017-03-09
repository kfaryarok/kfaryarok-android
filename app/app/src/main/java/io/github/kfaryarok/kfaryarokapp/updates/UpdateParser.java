package io.github.kfaryarok.kfaryarokapp.updates;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Takes the JSON string, parses it and returns an array of updates.
 * For information about how the JSON should be constructed, see JSONDATA.md.
 *
 * @author tbsc on 04/03/2017
 */
public class UpdateParser {

    public static final String GLOBAL_UPDATES_ARRAY = "global_updates";
    public static final String UPDATES_ARRAY = "updates";
    public static final String SHORT_TEXT_STRING = "short_text";
    public static final String LONG_TEXT_STRING = "long_text";
    public static final String CLASSES_ARRAY = "classes";

    public static Update[] parseUpdates(String json) throws JSONException {
        List<Update> updateList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(json);

        JSONArray globalUpdates = jsonObject.getJSONArray(GLOBAL_UPDATES_ARRAY);
        JSONArray updates = jsonObject.getJSONArray(UPDATES_ARRAY);

        for (int i = 0; i < globalUpdates.length(); ++i) {
            // get update and short text
            JSONObject globalUpdate = globalUpdates.getJSONObject(i);
            String shortText = globalUpdate.getString(SHORT_TEXT_STRING);

            // if has long text
            if (globalUpdate.has(LONG_TEXT_STRING)) {
                // add it too
                updateList.add(new UpdateImpl(shortText, globalUpdate.getString(LONG_TEXT_STRING)));
            } else {
                // otherwise only short text
                updateList.add(new UpdateImpl(shortText));
            }
        }

        for (int i = 0; i < updates.length(); ++i) {
            JSONObject update = updates.getJSONObject(i);
            String shortText = update.getString(SHORT_TEXT_STRING);

            // get affected classes
            JSONArray classes = update.getJSONArray(CLASSES_ARRAY);
            List<String> classesArray = new ArrayList<>();
            for (int j = 0; j < classes.length(); ++j) {
                classesArray.add(classes.getString(j));
            }

            if (update.has(LONG_TEXT_STRING)) {
                updateList.add(new UpdateImpl(classesArray, shortText, update.getString(LONG_TEXT_STRING)));
            } else {
                updateList.add(new UpdateImpl(classesArray, shortText));
            }
        }

        return updateList.toArray(new Update[0]);
    }

}
