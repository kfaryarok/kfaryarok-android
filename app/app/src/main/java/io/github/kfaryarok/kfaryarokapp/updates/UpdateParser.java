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
    public static final String TEXT_STRING = "text";
    public static final String CLASSES_ARRAY = "classes";

    public static Update[] parseUpdates(String json) throws JSONException {
        List<Update> updateList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(json);

        JSONArray globalUpdates = jsonObject.getJSONArray(GLOBAL_UPDATES_ARRAY);
        JSONArray updates = jsonObject.getJSONArray(UPDATES_ARRAY);

        for (int i = 0; i < globalUpdates.length(); ++i) {
            // get update and text
            JSONObject globalUpdate = globalUpdates.getJSONObject(i);
            String text = globalUpdate.getString(TEXT_STRING);
            updateList.add(new UpdateImpl(text));
        }

        for (int i = 0; i < updates.length(); ++i) {
            JSONObject update = updates.getJSONObject(i);
            String text = update.getString(TEXT_STRING);

            // get affected classes
            JSONArray classes = update.getJSONArray(CLASSES_ARRAY);
            List<String> classesArray = new ArrayList<>();
            for (int j = 0; j < classes.length(); ++j) {
                classesArray.add(classes.getString(j));
            }

            updateList.add(new UpdateImpl(classesArray, text));
        }

        return updateList.toArray(new Update[0]);
    }

}
