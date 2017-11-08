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

package io.github.kfaryarok.kfaryarokapp.updates;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import io.github.kfaryarok.kfaryarokapp.util.PreferenceUtil;
import io.github.kfaryarok.kfaryarokapp.util.TestUtil;

/**
 * Gets updates from a server, and saves the update into a String.
 * Doesn't parse the JSON, only gets it.
 *
 * I (Tbsc) have a personal test server, you can use it for testing if you want:
 * https://tbscdev.xyz/update.json
 * There's a testing update file on the project's github.io website:
 * https://kfaryarok.github.io/update.json
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
    public static final String DEFAULT_UPDATE_URL = "https://tbscdev.xyz/update.json";

    public static String fetchUpdates(Context ctx) {
        try {
            // TODO debug - remove after default server is setup
            URL url = buildUrl(ctx);
            if (url == null)
                return TestUtil.getTestJsonString();
            // return downloadUsingScanner(url);
            // return downloadUsingBufferedReader(url);
            return downloadUsingInputStreamReader(url);
        } catch (IOException e) {
            Log.i("UpdateFetcher", "Fetching updates failed; probably no internet access, using cached updates instead (" + e.getMessage() + ")");
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
     * Uses a Scanner to download from the URL to a string, reading the entire file at once.
     * @param url Where to connect to
     * @return Contents of the file at the specified URL, or null
     * @throws IOException If anything wrong happened during connection
     */
    public static String downloadUsingScanner(URL url) throws IOException {
        if (url == null) {
            return "";
        }

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

    /**
     * Uses a BufferedReader to download from the URL to a string, line by line.
     * @param url Where to connect to
     * @return Contents of the file at the specified URL, or null
     * @throws IOException If anything wrong happened during connection
     */
    public static String downloadUsingBufferedReader(URL url) throws IOException {
        if (url == null) {
            return "";
        }

        StringBuilder result = new StringBuilder();

        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String str;
        while ((str = in.readLine()) != null) {
            result.append(str).append("\n");
        }
        in.close();

        return result.toString();
    }

    /**
     * Uses an InputStreamReader to download from the URL to a String, character by character.
     * Taken from developer.android.com.
     * @param url Where to connect to
     * @return Contents of the file at the specified URL, or null
     * @throws IOException If anything wrong happened during connection
     */
    private static String downloadUsingInputStreamReader(URL url) throws IOException {
        if (url == null) {
            return "";
        }

        InputStream stream = null;
        HttpsURLConnection connection = null;
        String result = null;
        try {
            connection = (HttpsURLConnection) url.openConnection();
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(2000);
            connection.setRequestMethod("GET");
            // Already true by default but setting just in case; needs to be true since this request
            // is carrying an input (response) body.
            connection.setDoInput(true);
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }

            // Retrieve the response body as an InputStream.
            stream = connection.getInputStream();
            if (stream != null) {
                // Converts Stream to String.
                Reader reader = new InputStreamReader(stream, "UTF-8");
                int read;
                StringBuilder builder = new StringBuilder();
                while ((read = reader.read()) != -1) {
                    builder.append((char) read);
                }
                result = builder.toString();
            }
        } finally {
            // Close Stream and disconnect HTTPS connection.
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }

    @Override
    protected String doInBackground(Context... params) {
        return fetchUpdates(params[0]);
    }

}
