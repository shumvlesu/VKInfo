package com.hfad.vkinfo.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Scanner;


public class NetworkUtils {
//https://api.vk.com/method/users.get?user_ids=1982860&v=5.89&access_token=bbd39378bbd39378bbd3937876bbbac99cbbbd3bbd39378e75c194c563cd558732dc92c

    private static final String VK_API_BASE_URL = "https://api.vk.com";
    private static final String VK_USERS_GET = "/method/users.get";
    private static final String PARAM_USER_IDS = "user_ids";
    private static final String PARAM_USER_VERSION = "v";
    private static final String PARAM_ACCESS_TOKEN = "access_token";
    private static final String VK_TOKEN = "bbd39378bbd39378bbd3937876bbbac99cbbbd3bbd39378e75c194c563cd558732dc92c";

    public static URL generateURL(String userId) {
        Uri buildUri = Uri.parse(VK_API_BASE_URL + VK_USERS_GET).buildUpon()
                .appendQueryParameter(PARAM_USER_IDS, userId)
                .appendQueryParameter(PARAM_USER_VERSION, "5.89")
                .appendQueryParameter(PARAM_ACCESS_TOKEN, VK_TOKEN)
                .build();

        URL url = null;
        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;

    }


    public static String getResponseFromURL(URL url) throws IOException {

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
        } catch (UnknownHostException e) {
            return null;
        } finally {
            urlConnection.disconnect();
        }


    }

}

