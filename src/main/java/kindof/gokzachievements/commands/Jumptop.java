package kindof.gokzachievements.commands;

import kindof.gokzachievements.Bot;
import kindof.gokzachievements.utils.Util;
import kindof.gokzachievements.kzapi.KzApiUtil;
import kindof.gokzachievements.kzapi.entities.JumpEntity;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

import static kindof.gokzachievements.commands.EParameter.*;
import static kindof.gokzachievements.Globals.BOT_PREFIX;
import static kindof.gokzachievements.Globals.BOT_SEPARATOR;
import static kindof.gokzachievements.Globals.STEAM_COMMUNITY_PROFILES_URL;

public class Jumptop extends AbstractCommand {
    private static final int MAX_FIELDS_ON_EMBED = 10;
    private static final Color EMBED_COLOR = new Color(255, 0, 135, 255);

    public Jumptop() {
        argQueue = new EParameter[] {
                jumptype,
                is_crouch_bind,
                limit
        };
    }

    @Override
    public void setArgs(String... args) {
        super.setArgs(args);
        if (params.get(jumptype).equals("ladderjump")) {
            String vLimit = params.get(is_crouch_bind);
            params.remove(is_crouch_bind);
            params.put(limit, limit.getApiParameter(vLimit));
        }
        params.computeIfAbsent(limit, EParameter::getDefaultValue);
    }

    @Override
    public String getCommandName() {
        return ECommand.jumptop.name();
    }

    @Override
    public String getCommandDescription() {
        return ">>> **" + BOT_PREFIX + BOT_SEPARATOR + getCommandName() + " `<jump-type>` `[bind/no-bind]` `[limit]`**\nShows the global leaderboard of the selected jump type";
    }

    @Override
    public List<MessageEmbed> execute(User author) {
        Bot bot = Bot.getInstance();
        String jt = params.get(jumptype);
        params.remove(jumptype);
        List<JumpEntity> jumpEntities = KzApiUtil.httpGetRequestToAPIAndParseToEntities("jumpstats/" + jt + "/top", params, JumpEntity.class);
        if (jumpEntities.size() == 0) return null;

        List<MessageEmbed> messageEmbeds = new LinkedList<>();
        List<MessageEmbed.Field> fieldList = new LinkedList<>();

        String title = "Global Jumptop";
        String pageString = "";
        String cjString = "";
        String cj = params.get(is_crouch_bind);
        if (cj != null) cjString = "\nCrouch Jump: **" + cj + "**";
        String desc = "Jumptype: **" + jt + "**" + cjString;

        int size = jumpEntities.size();
        int fieldsOnLastPage = size % MAX_FIELDS_ON_EMBED;
        int pageCount = (size / MAX_FIELDS_ON_EMBED) + (fieldsOnLastPage == 0 ? 0 : 1);

        for (int i = 0; i < pageCount; i++) {
            int jStart = i * MAX_FIELDS_ON_EMBED;
            int jEnd = jStart + (i == pageCount - 1 ? (fieldsOnLastPage == 0 ? MAX_FIELDS_ON_EMBED : fieldsOnLastPage) : MAX_FIELDS_ON_EMBED);

            for (int j = jStart; j < jEnd; j++) {
                JumpEntity jumpEntity = jumpEntities.get(j);
                fieldList.add(new MessageEmbed.Field(
                        "#" + (j + 1),
                        ">>> Player: **[" + Util.doubleValid(jumpEntity.getPlayer_name()) + "](" + STEAM_COMMUNITY_PROFILES_URL + "/" + jumpEntity.getSteamid64() + ")**" +
                                "\nDistance: **" + jumpEntity.getDistance() + "** u." +
                                "\nStrafes: **" + jumpEntity.getStrafe_count() + "**",
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
