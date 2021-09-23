package kindof.gokzachievements.commands;

import kindof.gokzachievements.Bot;
import kindof.gokzachievements.exceptions.PermissionAccessException;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

import static kindof.gokzachievements.Globals.WEB_URL;

public class Register extends AbstractCommand {
    private static final Color EMBED_COLOR = Color.CYAN;

    public Register() {
        permissions = new Permission[] { Permission.ADMINISTRATOR };
        outputType = OutputType.PRIVATE_CHANNEL;
    }

    @Override
    public String getCommandName() {
        return ECommand.register.name();
    }

    @Override
    public String getCommandDescription() {
        return ">>> **" + PREFIX + SEPARATOR + getCommandName() + "**\nSends a private message to link your Steam account to Discord";
    }

    @Override
    public List<MessageEmbed> execute(Member author) throws PermissionAccessException {
        if (!isAccessible(author)) throw new PermissionAccessException();

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
