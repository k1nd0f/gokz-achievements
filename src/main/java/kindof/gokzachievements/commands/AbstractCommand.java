package kindof.gokzachievements.commands;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractCommand {

    protected OutputType outputType;
    protected EParameter[] argQueue;
    protected Map<EParameter, String> params;

    public AbstractCommand() {
        params = new HashMap<>();
        outputType = OutputType.PUBLIC_CHANNEL;
    }

    public void setArgs(String... args) {
        params.clear();
        if (argQueue != null && argQueue.length > 0 && args != null) {
            for (int i = 0; i < args.length; i++) {
                EParameter param = argQueue[i];
                String value = param.getApiParameter(args[i]);
                params.put(param, value);
            }
        }
    }

    public OutputType getOutputType() {
        return outputType;
    }

    public abstract String getCommandName();

    public abstract String getCommandDescription();

    public abstract List<MessageEmbed> getResult(User author);

    public enum OutputType {
        PUBLIC_CHANNEL,
        PRIVATE_CHANNEL
    }
}
