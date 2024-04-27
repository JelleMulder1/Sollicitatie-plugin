package me.jelle.harmserversollicitatiespigotplugin.Commands;

import me.jelle.harmserversollicitatiespigotplugin.HarmserverSollicitatiespigotPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.sql.SQLException;

public class removecolor implements CommandExecutor {
    private final HarmserverSollicitatiespigotPlugin plugin;

    public removecolor(HarmserverSollicitatiespigotPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            System.out.println("Dit commando kan alleen door spelers uitgevoerd worden!");
            return true;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("plugin.removeplayercolor")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to execute this command!");
            return true;
        }
        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);

                try {
                    if (!plugin.getDatabaseManager().ifPlayerExistsPlayerColor(target)) {
                        player.sendMessage(ChatColor.RED + "De speler die u bedoelt heeft nog geen kleur toegewezen! Gebruik /setcolor om een kleur toe te wijzen aan deze speler.");
                        return true;
                    }
                    plugin.getDatabaseManager().removePlayerColor(target);
                    player.sendMessage(ChatColor.GREEN + "Je hebt successful de kleur van " + target.getDisplayName() + " verwijderd!");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

        } else {
            player.sendMessage(ChatColor.RED + "Ongeldige invoer! Gebruik: /removecolor <player>");


        }
        return true;
    }
}