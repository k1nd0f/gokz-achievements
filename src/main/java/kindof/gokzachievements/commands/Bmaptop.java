package kindof.gokzachievements.commands;

import kindof.gokzachievements.Bot;
import kindof.gokzachievements.utils.Util;
import kindof.gokzachievements.kzapi.KzApiUtil;
import kindof.gokzachievements.kzapi.entities.MapTimeEntity;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

import static kindof.gokzachievements.commands.EParameter.*;
import static kindof.gokzachievements.Globals.BOT_PREFIX;
import static kindof.gokzachievements.Globals.BOT_SEPARATOR;
import static kindof.gokzachievements.Globals.STEAM_COMMUNITY_PROFILES_URL;

public class Bmaptop extends AbstractCommand {
    private static final int MAX_FIELDS_ON_EMBED = 25;
    private static final Color EMBED_COLOR = new Color(255, 0, 135, 255);

    public Bmaptop() {
        argQueue = new EParameter[] {
                modes_list_string,
                map_name,
                has_teleports,
                stage,
                limit
        };
    }

    @Override
    public void setArgs(String... args) {
        super.setArgs(args);
        params.put(tickrate, tickrate.getDefaultValue());
        params.computeIfAbsent(has_teleports, EParameter::getDefaultValue);
        params.putIfAbsent(stage, "1");
        params.computeIfAbsent(limit, EParameter::getDefaultValue);
    }

    @Override
    public String getCommandName() {
        return ECommand.bmaptop.name();
    }

    @Override
    public String getCommandDescription() {
        return ">>> **" + BOT_PREFIX + BOT_SEPARATOR + getCommandName() + " `<game-mode>` `<map-name>` `[run-type]` `[b-number]` `[limit]`**\nShows the global bonus leaderboard of the selected map";
    }

    @Override
    public List<MessageEmbed> getResult(User author) {
        Bot bot = Bot.getInstance();
        List<MapTimeEntity> mapTimeEntities = KzApiUtil.httpGetRequestToAPIAndParseToEntities("records/top", params, MapTimeEntity.class);

        if (mapTimeEntities.size() == 0) return null;

        MapTimeEntity mapTimeEntity = mapTimeEntities.get(0);
        if (!mapTimeEntity.getMap_name().equals(params.get(map_name))
                || !mapTimeEntity.getMode().equals(params.get(modes_list_string))) return null;


        List<MessageEmbed> messageEmbeds = new LinkedList<>();
        List<MessageEmbed.Field> fieldList = new LinkedList<>();

        String mapName = params.get(map_name);
        String gameMode = params.get(modes_list_string);
        String title = "Global Bonus Maptop";
        String desc = "Map: **" + mapName + "**" + "\nMode: **" + gameMode + "**";
        String pageString = "";

        int size = mapTimeEntities.size();
        int fieldsOnLastPage = size % MAX_FIELDS_ON_EMBED;
        int pageCount = (size / MAX_FIELDS_ON_EMBED) + (fieldsOnLastPage == 0 ? 0 : 1);

        for (int i = 0; i < pageCount; i++) {
            int jStart = i * MAX_FIELDS_ON_EMBED;
            int jEnd = jStart + (i == pageCount - 1 ? (fieldsOnLastPage == 0 ? MAX_FIELDS_ON_EMBED : fieldsOnLastPage) : MAX_FIELDS_ON_EMBED);

            for (int j = jStart; j < jEnd; j++) {
                mapTimeEntity = mapTimeEntities.get(j);
                fieldList.add(new MessageEmbed.Field(
                        "#" + (j + 1) + " - " + mapTimeEntity.getPoints() + " pts",
                        ">>> Player: **[" + Util.doubleValid(mapTimeEntity.getPlayer_name()) + "](" + STEAM_COMMUNITY_PROFILES_URL + "/" + mapTimeEntity.getSteamid64() + ")**" +
                                "\nTime: **" + KzApiUtil.convertTime(Double.parseDouble(mapTimeEntity.getTime())) + "** [" + mapTimeEntity.getTeleports() + " TPs]",
                        false
                ));
            }

            if (pageCount > 1) pageString = "\nPage: **" + (i + 1) + "**";
            messageEmbeds.add(bot.createMessageEmbed(
                    EMBED_COLOR,
                    title,
                    desc + pageString,
                    fieldList.toArray(new MessageEmbed.Field[0])
            ));
            fieldList.clear();
            title = null;
            desc = "";
        }

        return messageEmbeds;
    }
}
