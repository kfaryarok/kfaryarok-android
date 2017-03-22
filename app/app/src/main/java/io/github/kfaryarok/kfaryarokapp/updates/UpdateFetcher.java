package io.github.kfaryarok.kfaryarokapp.updates;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import io.github.kfaryarok.kfaryarokapp.util.PreferenceUtil;
import io.github.kfaryarok.kfaryarokapp.util.TestUtil;

/**
 * Gets updates from a server, and saves the update into a String.
 * Doesn't parse the JSON, only gets it.
 *
 * I (Tbsc) have a personal test server, you can use it for testing if you want:
 * https://tbscdev.xyz/update.json
 * Just put exactly that into the update server devmode preference
 *
 * |---------------------------------------------------------------------------------|
 * | NOTE: The JSON file HAS to be formatted as UTF-8, or Hebrew text won't be shown |
 * | correctly! Found this out after a day of debugging. This is very important!     |
 * |---------------------------------------------------------------------------------|
 *
 * @author tbsc on 04/03/2017
 */
public class UpdateFetcher extends AsyncTask<Context, Void, String> {

    // TODO actual data for fetching
    public static final String DEFAULT_UPDATE_URL = "";

    public static String fetchUpdates(Context ctx) {
        try {
            // TODO debug - remove after default server is setup
            if (buildUrl(ctx) == null)
                return TestUtil.getTestJsonString();
            return getResponseFromHttpUrl(buildUrl(ctx), ctx);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static URL buildUrl(Context ctx) {
        try {
            return new URL(PreferenceUtil.getUpdateServerPreference(ctx));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url, Context ctx) throws IOException {
        if (url == null)
            return "";

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    @Override
    protected String doInBackground(Context... params) {
        return fetchUpdates(params[0]);
    }

}
