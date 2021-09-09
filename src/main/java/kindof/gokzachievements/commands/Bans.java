package kindof.gokzachievements.commands;

import kindof.gokzachievements.Bot;
import kindof.gokzachievements.utils.Util;
import kindof.gokzachievements.kzapi.KzApiUtil;
import kindof.gokzachievements.kzapi.entities.BanEntity;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

import static kindof.gokzachievements.commands.EParameter.*;
import static kindof.gokzachievements.Globals.BOT_PREFIX;
import static kindof.gokzachievements.Globals.BOT_SEPARATOR;
import static kindof.gokzachievements.Globals.BANS_URL;

public class Bans extends AbstractCommand {

    private static final int MAX_FIELDS_ON_EMBED = 10; // Was lowered to exclude the error caused by the number of characters
    private static final Color EMBED_COLOR = Color.BLACK;

    public Bans() {
        argQueue = new EParameter[] {
                limit
        };
    }

    @Override
    public void setArgs(String... args) {
        super.setArgs(args);
        params.computeIfAbsent(limit, EParameter::getDefaultValue);
    }

    @Override
    public String getCommandName() {
        return ECommand.bans.name();
    }

    @Override
    public String getCommandDescription() {
        return ">>> **" + BOT_PREFIX + BOT_SEPARATOR + getCommandName() + " `[limit]`**\nShows last bans from **Global API**";
    }

    @Override
    public List<MessageEmbed> getResult(User author) {
        Bot bot = Bot.getInstance();
        List<BanEntity> banEntities = KzApiUtil.httpGetRequestToAPIAndParseToEntities("bans", params, BanEntity.class);
        if (banEntities.size() == 0) return null;

        List<MessageEmbed> messageEmbeds = new LinkedList<>();
        List<MessageEmbed.Field> fieldList = new LinkedList<>();

        String title = "Recent Bans";
        String pageString = "";
        int size = banEntities.size();
        int fieldsOnLastPage = size % MAX_FIELDS_ON_EMBED;
        int pageCount = (size / MAX_FIELDS_ON_EMBED) + (fieldsOnLastPage == 0 ? 0 : 1);

        for (int i = 0; i < pageCount; i++) {
            int jStart = i * MAX_FIELDS_ON_EMBED;
            int jEnd = jStart + (i == pageCount - 1 ? (fieldsOnLastPage == 0 ? MAX_FIELDS_ON_EMBED : fieldsOnLastPage) : MAX_FIELDS_ON_EMBED);

            for (int j = jStart; j < jEnd; j++) {
                BanEntity banEntity = banEntities.get(j);

                String stats = splitAndCombineStats(banEntity.getStats());
                String expiresDate = KzApiUtil.convertDate(banEntity.getExpires_on());

                fieldList.add(new MessageEmbed.Field(
                        Util.asteriskValid(banEntity.getPlayer_name()),
                        ">>> Reason: **[" + banEntity.getBan_type() + "](" + BANS_URL + "/" + banEntity.getSteam_id() + ")**" +
                                "\nExpires: **" + expiresDate + "**" +
                                "\n" +
                                "\n" + stats,
                        false
                ));
            }

            if (pageCount > 1) pageString = "Page: **" + (i + 1) + "**";
            messageEmbeds.add(bot.createMessageEmbed(
                    EMBED_COLOR,
                    title,
                    pageString,
                    fieldList.toArray(new MessageEmbed.Field[0])
            ));
            fieldList.clear();
            title = null;
        }

        return messageEmbeds;
    }

    private String splitAndCombineStats(String stats) {
        if (stats.isEmpty()) return "";

        String[] splittedStats = Util.asteriskValid(stats.replaceAll(", ", "\n")).split("\n");
        StringBuilder result = new StringBuilder();
        for (String stat : splittedStats) {
            String[] splittedStat = stat.split(": ");
            String statName = splittedStat[0];
            String statData = splittedStat[1];
            stat = statName + ": **" + statData + "**";
            result.append(stat).append("\n");
        }

        int resultLength = result.length();
        if (resultLength > 0) result.deleteCharAt(resultLength - 1);

        return result.toString();
    }
}
