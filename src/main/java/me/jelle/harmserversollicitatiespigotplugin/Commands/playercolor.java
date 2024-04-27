package me.jelle.harmserversollicitatiespigotplugin.Commands;

import me.jelle.harmserversollicitatiespigotplugin.HarmserverSollicitatiespigotPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class playercolor implements CommandExecutor {

    private final HarmserverSollicitatiespigotPlugin plugin;

    public playercolor(HarmserverSollicitatiespigotPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)){
            System.out.println("Dit commando kan alleen door spelers uitgevoerd worden!");
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 1){

            if (args[0].equalsIgnoreCase("reload")){
                plugin.reloadConfig();
                return true;
            }


            Player target = Bukkit.getPlayer(args[0]);
            try {
                if (plugin.getDatabaseManager().ifPlayerExistsPlayerColor(target)){

                    player.sendMessage(ChatColor.GREEN + target.getDisplayName() + " heeft de kleur " + plugin.getDatabaseManager().getPlayerColor(target) + " toegewezen!");

                } else {
                    player.sendMessage(ChatColor.RED + "De speler die u bedoelt heeft nog geen kleur toegewezen! Wijs een kleur aan door: /setplayercolor te doen!");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        } else {

            player.sendMessage(ChatColor.RED + "Ongeldige invoer! Gebruik: /playercolor <speler> of /playercolor reload");


        }



        return true;
    }
}
