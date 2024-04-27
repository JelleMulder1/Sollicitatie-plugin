package me.jelle.harmserversollicitatiespigotplugin;

import me.jelle.harmserversollicitatiespigotplugin.Commands.playercolor;
import me.jelle.harmserversollicitatiespigotplugin.Commands.removecolor;
import me.jelle.harmserversollicitatiespigotplugin.Commands.setcolor;
import me.jelle.harmserversollicitatiespigotplugin.Database.DatabaseManager;
import me.jelle.harmserversollicitatiespigotplugin.Listeners.ontabcompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class HarmserverSollicitatiespigotPlugin extends JavaPlugin {

    private DatabaseManager databaseManager;
    private FileConfiguration config;
    private Connection connection;

    @Override
    public void onEnable() {
        loadConfig();
        setupDatabase();

        databaseManager = new DatabaseManager(this, connection);

        getCommand("setplayercolor").setExecutor(new setcolor(this));
        getCommand("removeplayercolor").setExecutor(new removecolor(this));
        getCommand("playercolor").setExecutor(new playercolor(this));

        getCommand("setplayercolor").setTabCompleter(new ontabcompleter());

        try {
            databaseManager.createPlayerColorTable();
        } catch (SQLException e) {
            getLogger().severe("Failed to create playerColor table: " + e.getMessage());
        }

        getLogger().info("HarmserverSollicitatiespigotPlugin enabled!");
    }

    @Override
    public void onDisable() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                getLogger().severe("Failed to close database connection: " + e.getMessage());
            }
        }

        getLogger().info("HarmserverSollicitatiespigotPlugin disabled!");
    }

    private void loadConfig() {
        saveDefaultConfig();
        config = getConfig();
    }

    private void setupDatabase() {
        try {
            connection = getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to the database: " + e.getMessage());
        }
    }

    private Connection getConnection() throws SQLException {
        String host = config.getString("storage.mysql-credentials.host");
        int port = config.getInt("storage.mysql-credentials.port");
        String database = config.getString("storage.mysql-credentials.database");
        String username = config.getString("storage.mysql-credentials.username");
        String password = config.getString("storage.mysql-credentials.password");

        String URL = "jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true";
        return DriverManager.getConnection(URL, username, password);
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public FileConfiguration getPluginConfig() {
        return config;
    }
}
