package me.brand0n_.hoverstats.Commands;

import me.brand0n_.hoverstats.HoverStats;
import me.brand0n_.hoverstats.Utils.Chat.Messages;
import me.brand0n_.hoverstats.Utils.Permissions.Permissions;
import me.brand0n_.hoverstats.Utils.Permissions.Ranks;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HoverStatsCommand implements CommandExecutor {
    private static final HoverStats plugin = HoverStats.getPlugin(HoverStats.class); // Get this from main

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        // Check if the argument is 1 or greater
        switch (args.length) {
            case 3: {
                // Check if the player is using the whitelist command
                if (args[0].equalsIgnoreCase("whitelist") || args[0].equalsIgnoreCase("white-list") || args[0].equalsIgnoreCase("wl") || args[0].equalsIgnoreCase("whitel") || args[0].equalsIgnoreCase("wlist")) {
                    if (Permissions.hasPermission(sender, "hoverstats.whitelist")) {
                        // Check if the second argument is group or user
                        switch (args[1].toLowerCase()) {
                            case "g", "gs", "groups", "group": {
                                // Add the user to the list of blacklisted groups define in the config
                                List<String> blackListedGroups = plugin.getConfig().getStringList("Hover Blacklist.Groups");
                                // Check if the entered group is already blacklisted
                                if (!blackListedGroups.contains(args[2])) {
                                    plugin.getConfig().set("Hover Blacklist.Groups", blackListedGroups);
                                    // Tell the user that they entered a group that was already blacklisted
                                    Messages.notBlacklisted(sender, "group", args[2]);
                                    // Exit the code, all work is done
                                    return true;
                                }
                                // Add the current defined group to list of blacklisted groups
                                blackListedGroups.remove(args[2]);
                                // Update the blacklisted groups in the config
                                plugin.getConfig().set("Hover Blacklist.Groups", blackListedGroups);
                                // Save the config
                                plugin.saveConfig();
                                // Reload the config
                                plugin.reloadConfig();
                                // Send the user a message saying that they were removed from blacklist
                                Messages.addedWhitelist(sender, "group", args[2]);
                                // Exit the code, all work is done
                                return true;
                            }
                            case "u", "us", "users", "user": {
                                // Add the user to the list of blacklisted users define in the config
                                List<String> blackListedUsers = plugin.getConfig().getStringList("Hover Blacklist.Users");
                                // Check if the entered user is already blacklisted
                                if (!blackListedUsers.contains(args[2])) {
                                    // Tell the user that they entered a user that was already blacklisted
                                    Messages.alreadyWhitelisted(sender, "user", args[2]);
                                    // Exit the code, all work is done
                                    return true;
                                }
                                // Add the current defined user to list of blacklisted users in the config
                                blackListedUsers.remove(args[2]);
                                // Update the blacklisted user in the config
                                plugin.getConfig().set("Hover Blacklist.Users", blackListedUsers);
                                // Save the config
                                plugin.saveConfig();
                                // Reload the config
                                plugin.reloadConfig();
                                // Send the user a message saying that they were removed from blacklist
                                Messages.addedWhitelist(sender, "user", args[2]);
                                // Exit the code, all work is done
                                return true;
                            }
                        }
                    }
                }

                // Check if the player is using the blacklist command
                if (args[0].equalsIgnoreCase("blacklist") || args[0].equalsIgnoreCase("black-list") || args[0].equalsIgnoreCase("bl") || args[0].equalsIgnoreCase("blackl") || args[0].equalsIgnoreCase("blist")) {
                    if (Permissions.hasPermission(sender, "hoverstats.blacklist")) {
                        // Check if the second argument is group or user
                        switch (args[1].toLowerCase()) {
                            case "g", "gs", "groups", "group": {
                                // Add the user to the list of blacklisted groups define in the config
                                List<String> blackListedGroups = plugin.getConfig().getStringList("Hover Blacklist.Groups");
                                // Check if the entered group is a valid group on the server
                                if (!Ranks.getGroups().contains(args[2])) {
                                    // Tell the user that they entered an invalid group
                                    Messages.invalidGroup(sender, args[2]);
                                    // Exit the code, all work is done
                                    return true;
                                }
                                // Check if the entered group is already blacklisted
                                if (blackListedGroups.contains(args[2])) {
                                    // Tell the user that they entered a group that was already blacklisted
                                    Messages.alreadyBlacklisted(sender, "group", args[2]);
                                    // Exit the code, all work is done
                                    return true;
                                }
                                // Add the current defined group to list of blacklisted groups
                                blackListedGroups.add(args[2]);
                                System.out.println(blackListedGroups);
                                // Update the blacklisted groups in the config
                                plugin.getConfig().set("Hover Blacklist.Groups", blackListedGroups);
                                // Save the config
                                plugin.saveConfig();
                                // Reload the config
                                plugin.reloadConfig();
                                // Send the user a message saying that they were added to blacklist
                                Messages.addedBlacklist(sender, "group", args[2]);
                                // Exit the code, all work is done
                                return true;
                            }
                            case "u", "us", "users", "user": {
                                // Add the user to the list of blacklisted users define in the config
                                List<String> blackListedUsers = plugin.getConfig().getStringList("Hover Blacklist.Users");
                                // Set a temporary variable for if the user was found
                                boolean foundUser = false;
                                // Check if the entered user is a valid group on the server
                                for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
                                    // Get the players name
                                    if (player.getName() != null && player.getName().toLowerCase().equalsIgnoreCase(args[2])) {
                                        // Found the user
                                        foundUser = true;
                                        // Exit loop
                                        break;
                                    }
                                }
                                // Check if the use was found
                                if (!foundUser) {
                                    // Tell the user that the player is invalid
                                    Messages.invalidPlayer(sender, args[2]);
                                    // Exit the code, all work is done
                                    return true;
                                }
                                // Check if the entered user is already blacklisted
                                if (blackListedUsers.contains(args[2])) {
                                    // Tell the user that they entered a user that was already blacklisted
                                    Messages.alreadyBlacklisted(sender, "user", args[2]);
                                    // Exit the code, all work is done
                                    return true;
                                }
                                // Add the current defined user to list of blacklisted users in the config
                                blackListedUsers.add(args[2]);
                                // Update the blacklisted user in the config
                                plugin.getConfig().set("Hover Blacklist.Users", blackListedUsers);
                                // Save the config
                                plugin.saveConfig();
                                // Reload the config
                                plugin.reloadConfig();
                                // Send the user a message saying that they were added to blacklist
                                Messages.addedBlacklist(sender, "user", args[2]);
                                // Exit the code, all work is done
                                return true;
                            }
                        }
                    }
                }
            }
            case 2: {
                // Check if the player is using the whitelist command
                if (args[0].equalsIgnoreCase("whitelist") || args[0].equalsIgnoreCase("white-list") || args[0].equalsIgnoreCase("wl") || args[0].equalsIgnoreCase("whitel") || args[0].equalsIgnoreCase("wlist")) {
                    if (Permissions.hasPermission(sender, "hoverstats.whitelist")) {
                        // Check if the second argument is group or user
                        switch (args[1].toLowerCase()) {
                            case "g", "gs", "groups", "group", "u", "us", "users", "user": {
                                // Tell the user that
                                Messages.usageError(sender, label + " " + args[0] + " [group|user] [Group Name|User Name]");
                                // Exit the code, all work is done
                                return true;
                            }
                        }
                    }
                }
                // Check if the player is using the blacklist command
                if (args[0].equalsIgnoreCase("blacklist") || args[0].equalsIgnoreCase("black-list") || args[0].equalsIgnoreCase("bl") || args[0].equalsIgnoreCase("blackl") || args[0].equalsIgnoreCase("blist")) {
                    if (Permissions.hasPermission(sender, "hoverstats.blacklist")) {
                        // Check if the second argument is group or user
                        switch (args[1].toLowerCase()) {
                            case "g", "gs", "groups", "group", "u", "us", "users", "user": {
                                // Tell the user that
                                Messages.usageError(sender, label + " " + args[0] + " [group|user] [Group Name|User Name]");
                                // Exit the code, all work is done
                                return true;
                            }
                        }
                    }
                }
            }
            case 1: {
                if (args[0].equalsIgnoreCase("reload")) {
                    // Check if the player has the permission to reload the plugin
                    if (Permissions.hasPermission(sender, "hoverstats.reload")) {
                        // Reload the plugin
                        plugin.reloadPlugin(sender);
                        // Exit the code, all work is done
                        return true;
                    }
                    // Tell the player they don't have permissions to run this command
                    Messages.noPermissions(sender);
                    return false;
                }
                // Check if the player is trying to get the plugin version
                if (args[0].equalsIgnoreCase("version") || args[0].equalsIgnoreCase("ver") || args[0].equalsIgnoreCase("v")) {
                    if (Permissions.hasPermission(sender, "hoverstats.version")) {
                        // Tell the player what version their on
                        Messages.sendVersionInfo(sender);
                        // Exit the code, all work is done
                        return true;
                    }
                }
                // Check if the player is using the whitelist command
                if (args[0].equalsIgnoreCase("whitelist") || args[0].equalsIgnoreCase("white-list") || args[0].equalsIgnoreCase("wl") || args[0].equalsIgnoreCase("whitel") || args[0].equalsIgnoreCase("wlist")) {
                    if (Permissions.hasPermission(sender, "hoverstats.blacklist")) {
                        // Tell the user that
                        Messages.usageError(sender, label + " " + args[0] + " [group|user] [Group Name|User Name]");
                        // Exit the code, all work is done
                        return true;
                    }
                }
                // Check if the player is using the blacklist command
                if (args[0].equalsIgnoreCase("blacklist") || args[0].equalsIgnoreCase("black-list") || args[0].equalsIgnoreCase("bl") || args[0].equalsIgnoreCase("blackl") || args[0].equalsIgnoreCase("blist")) {
                    if (Permissions.hasPermission(sender, "hoverstats.blacklist")) {
                        // Tell the user that
                        Messages.usageError(sender, label + " " + args[0] + " [group|user] [Group Name|User Name]");
                        // Exit the code, all work is done
                        return true;
                    }
                }
            }
            default: {
                // Check if the player has permission to see the help message
                if (Permissions.hasPermission(sender, "hoverstats.help")) {
                    // Send the player the help message
                    Messages.sendHelpMessage(sender);
                    // Exit the code, all work is done
                    return true;
                }
                // Tell the player they don't have permissions to run this command
                Messages.noPermissions(sender);
                return false;
            }
        }
    }
}
