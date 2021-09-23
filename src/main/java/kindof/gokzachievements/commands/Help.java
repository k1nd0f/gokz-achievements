package kindof.gokzachievements.commands;

import kindof.gokzachievements.Bot;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

import static kindof.gokzachievements.Globals.GITHUB_URL;

public class Help extends AbstractCommand {
    private static final Color EMBED_COLOR = new Color(255, 0, 70, 255);

    @Override
    public String getCommandName() {
        return ECommand.help.name();
    }

    @Override
    public String getCommandDescription() {
        return ">>> **" + PREFIX + SEPARATOR + getCommandName() + "**\nShows a list of all commands";
    }

    @Override
    public List<MessageEmbed> execute(Member author) {
        Bot bot = Bot.getInstance();
        ECommand[] eCommands = ECommand.values();
        List<MessageEmbed> messageEmbeds = new LinkedList<>();
        List<MessageEmbed.Field> fieldList = new LinkedList<>();

        for (ECommand eCommand : eCommands) {
            AbstractCommand command = eCommand.getCommand();
            if (!command.isAccessible(author)) continue;

            String commandName = command.getCommandName();
            String commandDescription = command.getCommandDescription();
            String firstChar = commandName.charAt(0) + "";
            fieldList.add(new MessageEmbed.Field(commandName.replaceFirst(firstChar, firstChar.toUpperCase()), commandDescription, false));
        }

        messageEmbeds.add(bot.createMessageEmbed(
                EMBED_COLOR,
                "List of Commands",
                "[ReadMe.md](" + GITHUB_URL + "#readme)",
                fieldList.toArray(new MessageEmbed.Field[0])
        ));

        return messageEmbeds;
    }
}
