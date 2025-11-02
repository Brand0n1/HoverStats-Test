package me.brand0n_.hoverstats.Commands;

import me.brand0n_.hoverstats.HoverStats;
import me.brand0n_.hoverstats.Utils.Permissions.Permissions;
import me.brand0n_.hoverstats.Utils.Permissions.Ranks;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HoverStatsTabHandler implements TabCompleter {
    private static final HoverStats plugin = HoverStats.getPlugin(HoverStats.class); // Get this from main

    public List<String> onTabComplete(final @NotNull CommandSender sender, final @NotNull Command cmd, final @NotNull String label, final String[] args) {
        final List<String> result = new ArrayList<>();

        switch (args.length) {
            case 3: {
                if (args[0].equalsIgnoreCase("whitelist") && args[1].equalsIgnoreCase("group")) {
                    if (Permissions.hasPermission(sender, "hoverstats.whitelist.group")) {
                        // Add all groups to the result list
                        result.addAll(plugin.getConfig().getStringList("Hover Blacklist.Groups"));
                    }
                }
                if (args[0].equalsIgnoreCase("whitelist") && args[1].equalsIgnoreCase("user")) {
                    if (Permissions.hasPermission(sender, "hoverstats.whitelist.user")) {
                        result.addAll(plugin.getConfig().getStringList("Hover Blacklist.Users"));
                    }
                }
                if (args[0].equalsIgnoreCase("blacklist") && args[1].equalsIgnoreCase("group")) {
                    if (Permissions.hasPermission(sender, "hoverstats.blacklist.group")) {
                        // Add all groups to the result list
                        result.addAll(Ranks.getGroups());
                    }
                }
                if (args[0].equalsIgnoreCase("blacklist") && args[1].equalsIgnoreCase("user")) {
                    if (Permissions.hasPermission(sender, "hoverstats.blacklist.user")) {
                        for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
                            result.add(p.getName());
                        }
                    }
                }
                return result;
            }
            case 2: {
                if (args[0].equalsIgnoreCase("whitelist")) {
                    if (Permissions.hasPermission(sender, "hoverstats.whitelist")) {
                        result.add("group");
                        result.add("user");
                    }
                }
                if (args[0].equalsIgnoreCase("blacklist")) {
                    if (Permissions.hasPermission(sender, "hoverstats.blacklist")) {
                        result.add("group");
                        result.add("user");
                    }
                }
                return result;
            }
            case 1: {
                if (Permissions.hasPermission(sender, "hoverstats.reload")) {
                    result.add("reload");
                }
                if (Permissions.hasPermission(sender, "hoverstats.version")) {
                    result.add("version");
                }
                if (Permissions.hasPermission(sender, "hoverstats.whitelist")) {
                    result.add("whitelist");
                }
                if (Permissions.hasPermission(sender, "hoverstats.blacklist")) {
                    result.add("blacklist");
                }
                return result;
            }
            default: {
                if (Permissions.hasPermission(sender, "hoverstats.help")) {
                    result.add("help");
                }
            }
        }
        if (result.isEmpty()) {
            return null;
        }
        return result;
    }
}
