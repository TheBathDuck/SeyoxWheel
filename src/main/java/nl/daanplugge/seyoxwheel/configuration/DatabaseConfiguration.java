package nl.daanplugge.seyoxwheel.configuration;

import lombok.Getter;
import nl.daanplugge.seyoxwheel.WheelPlugin;

@Getter
public class DatabaseConfiguration extends ConfigHandler {

    private final String sqlAddress;
    private final int sqlPort;
    private final String sqlDatabase;
    private final String sqlUsername;
    private final String sqlPassword;

    public DatabaseConfiguration() {
        super(WheelPlugin.getInstance().getDataFolder(), "database.yml", WheelPlugin.getInstance().getLogger());

        this.sqlAddress = getNode().node("sql", "address").getString("localhost");
        this.sqlPort = getNode().node("sql", "port").getInt(3306);
        this.sqlDatabase = getNode().node("sql", "database").getString("database");
        this.sqlUsername = getNode().node("sql", "username").getString("admin");
        this.sqlPassword = getNode().node("sql", "password").getString("password");
    }
}
