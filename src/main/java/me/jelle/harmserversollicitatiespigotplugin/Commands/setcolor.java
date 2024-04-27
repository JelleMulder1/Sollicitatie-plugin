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

public class setcolor implements CommandExecutor {

    private final HarmserverSollicitatiespigotPlugin plugin;

    public setcolor(HarmserverSollicitatiespigotPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)){
            System.out.println("Dit commando kan alleen door spelers uitgevoerd worden!");
            return true;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("plugin.setplayercolor")){
            player.sendMessage(ChatColor.RED + "You don't have permission to execute this command!");
            return true;
        }
        if (args.length == 2){
            Player target = Bukkit.getPlayer(args[0]);
            String color = args[1];
            if (!iscolortrue(color)){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cOngeldige kleur! Je kan alleen deze kleuren gebruiken: &cred, &9blue, &eyellow, &agreen, &5purple, white&f, &7gray en &0black"));
                return true;
            }

            try {
                if (plugin.getDatabaseManager().ifPlayerExistsPlayerColor(target)){
                    player.sendMessage(ChatColor.RED + "De speler die u bedoelt heeft al een kleur toegewezen! Gebruik: /removecolor <speler>, om de kleur weer te verwijderen.");
                    return true;

                }
                if (target == null){
                    player.sendMessage(ChatColor.RED + "De speler die u bedoelt bestaat niet!");
                    return true;
                }
                plugin.getDatabaseManager().setPlayerColor(player, color);
                player.sendMessage(ChatColor.GREEN + "U heeft successful de kleur " + color + " toegewezen aan " + ChatColor.YELLOW + target.getDisplayName());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        } else {
            player.sendMessage(ChatColor.RED + "Onjuiste invoer! Gebruik: /setcolor <player> <color>");
        }




        return true;
    }

    public boolean iscolortrue(String color){

        if (!(color.equalsIgnoreCase("red") ||
                color.equalsIgnoreCase("blue") ||
                color.equalsIgnoreCase("yellow") ||
                color.equalsIgnoreCase("green") ||
                color.equalsIgnoreCase("purple") ||
                color.equalsIgnoreCase("white") ||
                color.equalsIgnoreCase("gray") ||
                color.equalsIgnoreCase("black"))){
            return false;

        }
        return true;
    }

}
