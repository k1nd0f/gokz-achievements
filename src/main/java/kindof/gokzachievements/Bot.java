package kindof.gokzachievements;

import kindof.gokzachievements.commands.AbstractCommand;
import kindof.gokzachievements.commands.ECommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

import static kindof.gokzachievements.Globals.BOT_PREFIX;
import static kindof.gokzachievements.Globals.BOT_SEPARATOR;

public class Bot extends ListenerAdapter {
    private static volatile Bot instance;

    private static final MessageEmbed NO_RESULT_FOUND_MESSAGE_EMBED = getInstance().createMessageEmbed(
            Color.LIGHT_GRAY,
            "No result found",
            null
    );
    private static final MessageEmbed SENDING_FAILED_MESSAGE_EMBED = getInstance().createMessageEmbed(
            Color.LIGHT_GRAY,
            "Sending failed",
            "Check the discord settings option:\n> **`Privacy & Safety -> Allow direct messages from server members`**"
    );

    private JDA discordAPI;
    private EmbedBuilder embedBuilder;

    private Bot() {
        try {
            discordAPI = JDABuilder.createDefault(Globals.BOT_TOKEN)
                    .addEventListeners(this)
                    .build()
                    .awaitReady();
        } catch (InterruptedException | LoginException e) {
            e.printStackTrace();
        }

        embedBuilder = new EmbedBuilder();
    }

    public static Bot getInstance() {
        Bot result = instance;
        if (result != null) {
            return result;
        }

        synchronized (Bot.class) {
            if (instance == null) {
                instance = new Bot();
            }
            return instance;
        }
    }

    public JDA getDiscordAPI() {
        return discordAPI;
    }

    public MessageEmbed createMessageEmbed(Color embedColor, String title, String description, String imageUrl, String thumbnailUrl, MessageEmbed.Field... fields) {
        String botAvatarUrl = discordAPI.getSelfUser().getAvatarUrl();
        embedBuilder.setColor(embedColor)
                .setTitle(title)
                .setDescription(description)
                .setImage(imageUrl)
                .setThumbnail(thumbnailUrl)
                .setFooter(Globals.COPYRIGHT_MARK, botAvatarUrl);

        if (fields != null) {
            for (MessageEmbed.Field field : fields) {
                embedBuilder.addField(field);
            }
        }

        MessageEmbed messageEmbed = embedBuilder.build();
        embedBuilder.clear();

        return messageEmbed;
    }

    public MessageEmbed createMessageEmbed(Color embedColor, String title, String description, MessageEmbed.Field... fields) {
        return createMessageEmbed(embedColor, title, description, null, null, fields);
    }

    public MessageEmbed createMessageEmbed(Color embedColor, String title, String description, String imageUrl, MessageEmbed.Field... fields) {
        return createMessageEmbed(embedColor, title, description, imageUrl, null, fields);
    }

    @Override
    public void onGuildJoin(@Nonnull GuildJoinEvent event) {
        Guild guild = event.getGuild();
        List<TextChannel> textChannels = guild.getTextChannelsByName(Globals.CHANNEL_NAME, false);
        if (textChannels.size() == 0) {
            guild.createTextChannel(Globals.CHANNEL_NAME).queue();
        }
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if (event.isFromType(ChannelType.TEXT) && !event.getAuthor().isBot()) {
            MessageChannel messageChannel = event.getChannel();
            if (messageChannel.getName().equals(Globals.CHANNEL_NAME)) {
                String content = event.getMessage().getContentRaw();
                String[] commandPrefix = content.split(BOT_PREFIX + BOT_SEPARATOR);
                try {
                    String[] commandPostfix = commandPrefix[commandPrefix.length - 1].split(" ");
                    String[] args = Arrays.copyOfRange(commandPostfix, 1, commandPostfix.length);
                    String commandName = commandPostfix[0];

                    ECommand eCommand = ECommand.valueOf(commandName);
                    AbstractCommand command = eCommand.getCommand();
                    command.setArgs(args);
                    AbstractCommand.OutputType commandOutputType = command.getOutputType();

                    User author = event.getAuthor();
                    List<MessageEmbed> messageEmbeds = command.execute(author);
                    if (messageEmbeds != null) {
                        for (MessageEmbed messageEmbed : messageEmbeds) {
                            if (commandOutputType == AbstractCommand.OutputType.PUBLIC_CHANNEL) {
                                messageChannel.sendMessage(messageEmbed).queue();
                            } else /* if (commandOutputType == Command.OutputType.PRIVATE_CHANNEL) */ {
                                author.openPrivateChannel()
                                        .complete()
                                        .sendMessage(messageEmbed)
                                        .queue(
                                                success -> {},
                                                throwable -> messageChannel.sendMessage(SENDING_FAILED_MESSAGE_EMBED).queue()
                                        );
                            }
                        }
                    } else {
                        messageChannel.sendMessage(NO_RESULT_FOUND_MESSAGE_EMBED).queue();
                    }
                } catch (Exception ignored) {
                    if (commandPrefix.length != 1) {
                        messageChannel.sendMessage(NO_RESULT_FOUND_MESSAGE_EMBED).queue();
                    }
                }
            }
        }
    }
}
