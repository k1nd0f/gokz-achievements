package kindof.gokzachievements.commands;

public enum ECommand {
    help(new Help()),
    ping(new Ping()),
    register(new Register()),
    bans(new Bans()),
    wr(new Wr()),
    bwr(new Bwr()),
    maptop(new Maptop()),
    bmaptop(new Bmaptop()),
    jumptop(new Jumptop());

    private AbstractCommand command;

    ECommand(AbstractCommand command) {
        this.command = command;
    }

    public AbstractCommand getCommand() {
        return command;
    }
}
