package kindof.gokzachievements.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import kindof.gokzachievements.commands.EParameter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HttpUtil {
    private static final GsonBuilder GSON_BUILDER = new GsonBuilder();
    private static final Gson GSON = GSON_BUILDER.create();

    public static String httpGetRequest(String targetUrl) {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(targetUrl);
            urlConnection = (HttpURLConnection) url.openConnection();

            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_MOVED_PERM || responseCode == HttpURLConnection.HTTP_MOVED_TEMP) {
                String location = urlConnection.getHeaderField("Location");
                url = new URL(location);
                urlConnection = (HttpURLConnection) url.openConnection();
            }

            responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return readFromInputStream(urlConnection.getInputStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return null;
    }

    public static <T> List<T> httpGetRequestAndParseToEntities(String targetUrl, Class<T> classOfT) {
        String jsonString = httpGetRequest(targetUrl);
        return parseJsonString(jsonString, classOfT);
    }

    public static <T> List<T> parseJsonString(String jsonString, Class<T> classOfT) {
        List<T> list = new LinkedList<>();
        JsonArray jsonArray = GSON.fromJson(jsonString, JsonArray.class);
        for (JsonElement jsonElement : jsonArray) {
            T object = GSON.fromJson(jsonElement.toString(), classOfT);
            list.add(object);
        }

        return list;
    }

    public static String getTargetUrlToAPI(String url, String endpoint, Map<EParameter, String> params) {
        StringBuilder targetUrl = new StringBuilder(url + "/" + endpoint);
        if (params != null && params.size() > 0) {
            targetUrl.append("?");
            for (Map.Entry<EParameter, String> entry : params.entrySet()) {
                EParameter param = entry.getKey();
                String name = param.name();
                String value = entry.getValue();

                if (value != null) {
                    targetUrl.append(name).append("=").append(value).append("&");
                }
            }
            targetUrl.deleteCharAt(targetUrl.length() - 1);
        }

        return targetUrl.toString();
    }

    private static String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder result = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String inLine;
            while ((inLine = br.readLine()) != null) {
                result.append(inLine);
            }
        }

        return result.toString();
    }
}
