package nl.daanplugge.seyoxwheel;

import co.aikar.commands.PaperCommandManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import nl.daanplugge.seyoxwheel.commands.WheelCommand;
import nl.daanplugge.seyoxwheel.configuration.DatabaseConfiguration;
import nl.daanplugge.seyoxwheel.database.Database;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

@Getter
public class WheelPlugin extends JavaPlugin {

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private static WheelPlugin instance;

    private Map<Integer, ItemStack> wheelcoinItems;

    private PaperCommandManager commandManager;
    private DatabaseConfiguration databaseConfiguration;
    private Database database;

    @Override
    public void onEnable() {
        setInstance(this);

        databaseConfiguration = new DatabaseConfiguration();
        databaseConfiguration.saveConfiguration();

        database = new Database(databaseConfiguration);
        database.createTable();

        database.getItems().thenAccept(items -> wheelcoinItems = items);

        Bukkit.getServer().getPluginManager().registerEvents(new WheelCommand(), this);
        commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new WheelCommand());
    }
}