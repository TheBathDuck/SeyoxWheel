package nl.daanplugge.seyoxwheel.database;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import nl.daanplugge.seyoxwheel.WheelPlugin;
import nl.daanplugge.seyoxwheel.configuration.DatabaseConfiguration;
import nl.daanplugge.seyoxwheel.utils.GsonStack;
import org.bukkit.inventory.ItemStack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Database {

    private final HikariDataSource hikari;
    private final ExecutorService executorService;

    public Database(DatabaseConfiguration config) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("org.mariadb.jdbc.Driver");
        hikariConfig.setJdbcUrl("jdbc:mariadb://" + config.getSqlAddress() + ":" + config.getSqlPort() + "/" + config.getSqlDatabase());
        hikariConfig.setUsername(config.getSqlUsername());
        hikariConfig.setPassword(config.getSqlPassword());
        hikariConfig.setConnectionTestQuery("SELECT 1");
        hikari = new HikariDataSource(hikariConfig);
        executorService = Executors.newCachedThreadPool();
        WheelPlugin.getInstance().getLogger().info("Successfully connected to the database.");
    }

    public CompletableFuture<Void> createTable() {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = hikari.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS wheel_items(" +
                                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                                "item TEXT" +
                                ");"
                );

                statement.execute();
                WheelPlugin.getInstance().getLogger().info("Created database table.");
            } catch (SQLException e) {
                WheelPlugin.getInstance().getLogger().severe("Could not create database tables: " + e.getMessage());
            }
        }, executorService);
    }

    public CompletableFuture<Integer> insertItem(ItemStack item) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = hikari.getConnection()) {

                PreparedStatement statement = connection.prepareStatement("INSERT INTO wheel_items(item) VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS);
                statement.setString(1, GsonStack.itemStackToString(item));
                statement.executeUpdate();

                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            } catch (SQLException e) {
                WheelPlugin.getInstance().getLogger().severe("Could not insert item: " + e.getMessage());
            }

            return -1;
        }, executorService);
    }

    public CompletableFuture<Void> removeItem(int id) {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = hikari.getConnection()) {
                PreparedStatement statement = connection.prepareStatement("DELETE FROM wheel_items WHERE id=?");
                statement.setInt(1, id);
                statement.execute();
            } catch (SQLException e) {
                WheelPlugin.getInstance().getLogger().severe("Couldn't remove item: " + e.getMessage());
            }
        }, executorService);
    }

    public CompletableFuture<Map<Integer, ItemStack>> getItems() {
        Map<Integer, ItemStack> items = new HashMap<>();

        return CompletableFuture.supplyAsync(() -> {

            try (Connection connection = hikari.getConnection()) {

                PreparedStatement statement = connection.prepareStatement("SELECT * FROM wheel_items");
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    try {
                        int id = resultSet.getInt("id");
                        ItemStack itemString = GsonStack.stringToItemStack(resultSet.getString("item"));
                        items.put(id, itemString);
                    } catch (CommandSyntaxException e) {
                        WheelPlugin.getInstance().getLogger().severe("Couldn't parse item: " + resultSet.getInt("id"));
                    }
                }

            } catch (SQLException e) {
                WheelPlugin.getInstance().getLogger().severe("Could not retrieve items: " + e.getMessage());
            }

            return items;
        });

    }

}
