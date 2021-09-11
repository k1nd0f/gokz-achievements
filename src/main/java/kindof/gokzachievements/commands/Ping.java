package kindof.gokzachievements.commands;

import kindof.gokzachievements.Bot;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

import static kindof.gokzachievements.Globals.BOT_PREFIX;
import static kindof.gokzachievements.Globals.BOT_SEPARATOR;

public class Ping extends AbstractCommand {
    private static final Color EMBED_COLOR = Color.GREEN;

    @Override
    public String getCommandName() {
        return ECommand.ping.name();
    }

    @Override
    public String getCommandDescription() {
        return ">>> **" + BOT_PREFIX + BOT_SEPARATOR + getCommandName() + "**\nShows the response latency";
    }

    @Override
    public List<MessageEmbed> execute(User author) {
        Bot bot = Bot.getInstance();
        JDA discordAPI = bot.getDiscordAPI();
        List<MessageEmbed> messageEmbeds = new LinkedList<>();

        messageEmbeds.add(bot.createMessageEmbed(
                EMBED_COLOR,
                null,
                "WebSocket Latency: **" + discordAPI.getGatewayPing() + " ms**" +
                "\nAPI Latency: **" + discordAPI.getRestPing().complete() + " ms**"
        ));


        return messageEmbeds;
    }
}
