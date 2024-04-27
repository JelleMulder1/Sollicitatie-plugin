package me.jelle.harmserversollicitatiespigotplugin.Database;

import com.mysql.cj.protocol.Resultset;
import me.jelle.harmserversollicitatiespigotplugin.HarmserverSollicitatiespigotPlugin;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManager {

    private final HarmserverSollicitatiespigotPlugin plugin;
    private final Connection connection;

    private File DataFile;
    private YamlConfiguration DataYamlconfig;


    public DatabaseManager(HarmserverSollicitatiespigotPlugin plugin, Connection connection) {
        this.plugin = plugin;
        this.connection = connection;
    }




    public void createPluginDB() throws SQLException {
        String query = "CREATE DATABASE IF NOT EXISTS plugin";
        try(PreparedStatement statement = connection.prepareStatement(query)){
            statement.executeUpdate();
        }

    }


    public void createPlayerColorTable() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS playerColor (UUID VARCHAR(255) PRIMARY KEY, name VARCHAR(255), color VARCHAR(255))";
        try(PreparedStatement statement = connection.prepareStatement(query)){
            statement.executeUpdate();
        }

    }
    public void setPlayerColor(Player player, String color) throws SQLException {
        String query = "INSERT INTO playerColor (UUID, name, color) VALUES (?, ?, ?)";
        try(PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, String.valueOf(player.getUniqueId()));
            statement.setString(2, player.getName());
            statement.setString(3, color);
            statement.executeUpdate();
        }



    }
    public void removePlayerColor(Player player) throws SQLException{
        String query = ("DELETE from playerColor WHERE UUID = ?");
        try (PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, player.getUniqueId().toString());
            statement.executeUpdate();

        }


    }
    public boolean ifPlayerExistsPlayerColor(Player player) throws SQLException {
        String query = "SELECT COUNT(*) FROM playerColor WHERE UUID = ?";
        try(PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, player.getUniqueId().toString());
            try(ResultSet resultset = statement.executeQuery()){
                if (resultset.next()){
                    int count = resultset.getInt(1);
                    return count > 0;
                }
            }

        }



        return false;
    }

    public String getPlayerColor(Player player) throws SQLException{
        String query = "SELECT color FROM playerColor WHERE UUID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, player.getUniqueId().toString());
            try (ResultSet resultSet = statement.executeQuery()){
                if (resultSet.next()){
                    return resultSet.getString("color");

                }
            }
        }


        return null;
    }

    public void transferDataToDatabase() throws SQLException {
        for (String key : DataYamlconfig.getKeys(false)) {
            String color = DataYamlconfig.getString(key + ".color");
            if (color != null) {
                Player player = plugin.getServer().getPlayerExact(key);
                if (player != null) {
                    setPlayerColor(player, color);
                }
            }
        }
    }




}
