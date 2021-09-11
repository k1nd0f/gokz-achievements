package kindof.gokzachievements.commands;

import kindof.gokzachievements.Bot;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

import static kindof.gokzachievements.Globals.BOT_PREFIX;
import static kindof.gokzachievements.Globals.BOT_SEPARATOR;
import static kindof.gokzachievements.Globals.WEB_URL;

public class Register extends AbstractCommand {
    private static final Color EMBED_COLOR = Color.CYAN;

    public Register() {
        outputType = OutputType.PRIVATE_CHANNEL;
    }

    @Override
    public String getCommandName() {
        return ECommand.register.name();
    }

    @Override
    public String getCommandDescription() {
        return ">>> **" + BOT_PREFIX + BOT_SEPARATOR + getCommandName() + "**\nSends a private message to link your Steam account to Discord";
    }

    @Override
    public List<MessageEmbed> execute(User author) {
        Bot bot = Bot.getInstance();
        List<MessageEmbed> messageEmbeds = new LinkedList<>();

        messageEmbeds.add(bot.createMessageEmbed(
                EMBED_COLOR,
                "Register",
                "**[- Link Steam Account](" + WEB_URL + "/register?discord_id=" + author.getId() + ")**"
        ));

        return messageEmbeds;
    }
}
