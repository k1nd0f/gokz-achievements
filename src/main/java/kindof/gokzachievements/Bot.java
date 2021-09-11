package kindof.gokzachievements;

import kindof.gokzachievements.commands.AbstractCommand;
import kindof.gokzachievements.commands.ECommand;
import kindof.gokzachievements.exceptions.NoResultFoundException;
import kindof.gokzachievements.exceptions.WrongChannelException;
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

import static kindof.gokzachievements.Globals.*;
import static kindof.gokzachievements.commands.AbstractCommand.*;
import static kindof.gokzachievements.commands.AbstractCommand.OutputType.*;

public class Bot extends ListenerAdapter {
    private static volatile Bot instance;

    private static final Color WARNING_COLOR = Color.RED;

    private static final MessageEmbed NO_RESULT_FOUND_MESSAGE_EMBED = getInstance().createMessageEmbed(
            Color.LIGHT_GRAY,
            "No result found",
            null
    );
    private static final MessageEmbed SENDING_FAILED_MESSAGE_EMBED = getInstance().createMessageEmbed(
            Color.LIGHT_GRAY,
            "Sending failed",
            "Check the discord settings option\n> **`Privacy & Safety -> Allow direct messages from server members`**"
    );
    private static final MessageEmbed WRONG_COMMAND_MESSAGE_EMBED = getInstance().createMessageEmbed(
            WARNING_COLOR,
            null,
            "**You are trying to run the wrong command, you may have made a typo, try again.**"
    );

    private JDA discordAPI;
    private EmbedBuilder embedBuilder;

    private Bot() {
        try {
            discordAPI = JDABuilder.createDefault(BOT_TOKEN)
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
                .setFooter(COPYRIGHT_MARK, botAvatarUrl);

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
        List<TextChannel> textChannels = guild.getTextChannelsByName(CHANNEL_NAME, false);
        if (textChannels.size() == 0) {
            guild.createTextChannel(CHANNEL_NAME)
                    .setSlowmode(CHANNEL_SLOWMODE)
                    .setTopic(CHANNEL_TOPIC)
                    .queue();
        }
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if (event.isFromType(ChannelType.TEXT) && !event.getAuthor().isBot()) {
            Guild guild = event.getGuild();
            MessageChannel messageChannel = event.getChannel();
            Message message = event.getMessage();

            String content = message.getContentRaw();
            String[] commandPrefix = content.split(PREFIX + SEPARATOR);

            if (commandPrefix.length != 1) {
                try {
                    if (!messageChannel.getName().equals(CHANNEL_NAME)) throw new WrongChannelException();

                    String[] commandPostfix = commandPrefix[commandPrefix.length - 1].split(" ");
                    String[] args = Arrays.copyOfRange(commandPostfix, 1, commandPostfix.length);
                    String commandName = commandPostfix[0];
                    ECommand eCommand = ECommand.valueOf(commandName);

                    try {
                        AbstractCommand command = eCommand.getCommand();
                        command.setArgs(args);
                        AbstractCommand.OutputType commandOutputType = command.getOutputType();

                        User author = event.getAuthor();
                        List<MessageEmbed> messageEmbeds = command.execute(author);
                        if (messageEmbeds == null) throw new NoResultFoundException();

                        for (MessageEmbed messageEmbed : messageEmbeds) {
                            if (commandOutputType == PUBLIC_CHANNEL) {
                                messageChannel.sendMessage(messageEmbed).queue();
                            } else /* if (commandOutputType == PRIVATE_CHANNEL) */ {
                                author.openPrivateChannel()
                                        .queue(privateChannel -> privateChannel
                                                .sendMessage(messageEmbed)
                                                .queue(
                                                        sendSuccess -> message.delete().queue(),
                                                        sendFail -> messageChannel.sendMessage(SENDING_FAILED_MESSAGE_EMBED).queue()
                                                )
                                        );
                            }
                        }
                    } catch (NoResultFoundException ignored) {
                        messageChannel.sendMessage(NO_RESULT_FOUND_MESSAGE_EMBED).queue();
                    }
                } catch (WrongChannelException ignored) {
                    messageChannel.sendMessage(createMessageEmbed(
                            WARNING_COLOR,
                            null,
                            "**You're using the wrong channel, try this one " + guild.getTextChannelsByName(CHANNEL_NAME, false).get(0).getAsMention() + "**"
                    )).queue(successMessage -> deleteMessagesAfter(successMessage, message));
                } catch (Exception ignored) {
                    messageChannel.sendMessage(WRONG_COMMAND_MESSAGE_EMBED)
                            .queue(successMessage -> deleteMessagesAfter(successMessage, message));
                }
            }
        }
    }

    private void deleteMessagesAfter(Message... messages) {
        for (Message message : messages) {
            message.delete().queueAfter(MESSAGE_DELETE_DELAY, MESSAGE_DELETE_TIME_UNIT);
        }
    }
}
