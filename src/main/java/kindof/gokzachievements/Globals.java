package kindof.gokzachievements;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Globals {

    private static final Properties properties = new Properties();

    static {
        Map<String, String> envVars = System.getenv();
        for (Map.Entry<String, String> entry : envVars.entrySet()) {
            properties.setProperty(entry.getKey(), entry.getValue());
        }
    }

    public static final String BOT_TOKEN                     = properties.getProperty("bot.token");
    public static final String COPYRIGHT_MARK                = properties.getProperty("copyright.mark");
    public static final String WEB_URL                       = properties.getProperty("web.url");
    public static final String GITHUB_URL                    = properties.getProperty("github.url");
    public static final String KZ_API_URL                    = properties.getProperty("kz.api.url");
    public static final String BANS_URL                      = properties.getProperty("bans.url");

    public static final String CHANNEL_NAME                  = "gokz-achievements";
    public static final String CHANNEL_TOPIC                 = "gokz/help - for the command list";
    public static final int CHANNEL_SLOWMODE                 = 5;

    public static final int MESSAGE_DELETE_DELAY             = 10;
    public static final TimeUnit MESSAGE_DELETE_TIME_UNIT    = TimeUnit.SECONDS;

    public static final String STEAM_COMMUNITY_PROFILES_URL  = "https://steamcommunity.com/profiles";

    public static final String MAP_THUMBNAIL_URL             = properties.getProperty("map.thumbnail.url");
    public static final String MAP_THUMBNAIL_FORMAT          = properties.getProperty("map.thumbnail.format");
}
