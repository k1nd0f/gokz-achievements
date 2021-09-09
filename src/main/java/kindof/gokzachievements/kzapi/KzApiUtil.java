package kindof.gokzachievements.kzapi;

import kindof.gokzachievements.utils.HttpUtil;
import kindof.gokzachievements.commands.EParameter;
import kindof.gokzachievements.kzapi.entities.BaseEntity;

import java.util.*;

import static kindof.gokzachievements.Globals.KZ_API_URL;

public class KzApiUtil extends HttpUtil {

    public static String httpGetRequestToAPI(String endpoint, Map<EParameter, String> params) {
        return httpGetRequest(getTargetUrlToAPI(KZ_API_URL, endpoint, params));
    }

    public static <T extends BaseEntity> List<T> httpGetRequestToAPIAndParseToEntities(String endpoint, Map<EParameter, String> params, Class<T> classOfT) {
        return httpGetRequestAndParseToEntities(getTargetUrlToAPI(KZ_API_URL, endpoint, params), classOfT);
    }

    public static String convertTime(double seconds) {
        int minutes = (int) (seconds / 60.0);
        seconds = seconds % 60;
        int hours = (int) (minutes / 60.0);
        minutes = minutes % 60;
        seconds = ((int) (seconds * 1000)) / 1000.0;

        String result = "";
        if (hours != 0) {
            result += (hours + ":");
        }
        if (minutes != 0) {
            if (minutes < 10 && hours != 0) result += "0";
            result += (minutes + ":");
        }
        if (seconds < 10 && minutes != 0) {
            result += "0";
        }
        result += seconds;

        return result;
    }

    public static String convertDate(String dateString) {
        String[] splittedDate = dateString.split("T");
        String date = splittedDate[0];
        String time = splittedDate[1];

        splittedDate = date.split("-");
        String year = splittedDate[0];
        String month = splittedDate[1];
        String day = splittedDate[2];

        splittedDate = time.split(":");
        String hours = splittedDate[0];
        String minutes = splittedDate[1];

        if (year.equals("9999")) return "Never";
        return day + "." + month + "." + year + " in " + hours + ":" + minutes;
    }
}
