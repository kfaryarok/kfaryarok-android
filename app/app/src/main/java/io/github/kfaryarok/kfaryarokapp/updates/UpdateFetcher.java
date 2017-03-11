package io.github.kfaryarok.kfaryarokapp.updates;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Gets updates from a server, and saves the update into a String.
 * Doesn't parse the JSON, only gets it.
 *
 * @author tbsc on 04/03/2017
 */
public class UpdateFetcher {

    // TODO actual data for fetching
    public static final String UPDATE_SERVER = "";
    public static final String UPDATE_PATH = "/update.json";

    public static String fetchUpdates() {
        try {
            return getResponseFromHttpUrl(buildUrl());
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static URL buildUrl() {
        Uri uri = Uri.parse(UPDATE_SERVER).buildUpon()
                .appendPath(UPDATE_PATH).build();

        try {
            return new URL(uri.toString());
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
    public static String getResponseFromHttpUrl(URL url) throws IOException {
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
}
