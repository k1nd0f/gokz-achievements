package kindof.gokzachievements.commands;

import kindof.gokzachievements.Bot;
import kindof.gokzachievements.kzapi.KzApiUtil;
import kindof.gokzachievements.kzapi.entities.MapTimeEntity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

import static kindof.gokzachievements.commands.EParameter.*;
import static kindof.gokzachievements.Globals.STEAM_COMMUNITY_PROFILES_URL;
import static kindof.gokzachievements.Globals.MAP_THUMBNAIL_URL;
import static kindof.gokzachievements.Globals.MAP_THUMBNAIL_FORMAT;

public class Bwr extends AbstractCommand {
    private static final Color EMBED_COLOR = new Color(255, 215, 0, 255);

    public Bwr() {
        argQueue = new EParameter[] {
                modes_list_string,
                map_name,
                has_teleports,
                stage
        };
    }

    @Override
    public void setArgs(String... args) {
        super.setArgs(args);
        params.put(limit, "1");
        params.put(tickrate, tickrate.getDefaultValue());
        params.computeIfAbsent(has_teleports, EParameter::getDefaultValue);
        params.putIfAbsent(stage, "1");
    }

    @Override
    public String getCommandName() {
        return ECommand.bwr.name();
    }

    @Override
    public String getCommandDescription() {
        return ">>> **" + PREFIX + SEPARATOR + getCommandName() + " `<game-mode>` `<map-name>` `[run-type]` `[b-number]`**\nShows the bonus world record of the selected map";
    }

    @Override
    public List<MessageEmbed> execute(Member author) {
        Bot bot = Bot.getInstance();
        List<MessageEmbed> messageEmbeds = new LinkedList<>();
        List<MapTimeEntity> mapTimeEntities = KzApiUtil.httpGetRequestToAPIAndParseToEntities("records/top", params, MapTimeEntity.class);

        if (mapTimeEntities.size() == 0) return null;

        MapTimeEntity mapTimeEntity = mapTimeEntities.get(0);
        if (!mapTimeEntity.getMap_name().equals(params.get(map_name))
                || !mapTimeEntity.getMode().equals(params.get(modes_list_string))) return null;

        String mapName = params.get(map_name);
        messageEmbeds.add(bot.createMessageEmbed(
                EMBED_COLOR,
                "Bonus World Record",
                "Map: **" + mapName + "**" +
                        "\nMode: **" + params.get(modes_list_string) + "**" +
                        "\nBonus: **" + params.get(stage) + "**" +
                        "\n" +
                        "\nPlayer: **[" + mapTimeEntity.getPlayer_name() + "](" + STEAM_COMMUNITY_PROFILES_URL + "/" + mapTimeEntity.getSteamid64() + ")**" +
                        "\nTime: **" + KzApiUtil.convertTime(Double.parseDouble(mapTimeEntity.getTime())) + "** [" + mapTimeEntity.getTeleports() + " TPs]" +
                        "\nServer: **" + mapTimeEntity.getServer_name() + "**",
                MAP_THUMBNAIL_URL + "/" + mapName + MAP_THUMBNAIL_FORMAT
        ));

        return messageEmbeds;
    }
}
