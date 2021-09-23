package kindof.gokzachievements.commands;

import kindof.gokzachievements.exceptions.PermissionAccessException;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractCommand {
    public static final String PREFIX = "gokz";
    public static final String SEPARATOR = "/";

    protected OutputType outputType;
    protected EParameter[] argQueue;
    protected Map<EParameter, String> params;
    protected Permission[] permissions;

    public AbstractCommand() {
        params = new HashMap<>();
        outputType = OutputType.PUBLIC_CHANNEL;
    }

    protected boolean isAccessible(Member member) {
        if (permissions != null) {
            for (Permission permission : permissions) {
                if (!member.getPermissions().contains(permission)) return false;
            }
        }
        return true;
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

    public abstract List<MessageEmbed> execute(Member author) throws PermissionAccessException;

    public abstract String getCommandName();

    public abstract String getCommandDescription();

    public enum OutputType {
        PUBLIC_CHANNEL,
        PRIVATE_CHANNEL
    }
}
