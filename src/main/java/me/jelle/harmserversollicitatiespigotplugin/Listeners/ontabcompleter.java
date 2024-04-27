package me.jelle.harmserversollicitatiespigotplugin.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class ontabcompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (!(sender instanceof Player)) {
            return completions;
        }

        if (!command.getName().equalsIgnoreCase("setplayercolor")) {
            return completions;
        }

        if (args.length == 1) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                completions.add(player.getName());
            }
        } else if (args.length == 2) {
            List<String> colorOptions = Arrays.asList("Blue", "Red", "Yellow", "Green", "Purple", "White", "Gray", "Black");

            String prefix = args[1].toLowerCase();
            for (String color : colorOptions) {
                if (color.toLowerCase().startsWith(prefix)) {
                    completions.add(color);
                }
            }
        }

        Collections.sort(completions);
        return completions;
    }
}

