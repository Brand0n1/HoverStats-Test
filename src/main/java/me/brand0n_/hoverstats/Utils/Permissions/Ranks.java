package me.brand0n_.hoverstats.Utils.Permissions;

import me.brand0n_.hoverstats.HoverStats;
import me.brand0n_.hoverstats.Utils.Files.GroupFiles;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;

public class Ranks {
    private static final HoverStats plugin = HoverStats.getPlugin(HoverStats.class); // Get this from main

    // Define the keys for the chat message section
    private static final String chatMessageKey = "Chat Messages";
    // Define the keys for the first join message section
    private static final String fistJoinMessageKey = "Join Messages.First Join";
    // Define the keys for the join message section
    private static final String joinMessageKey = "Join Messages.Standard Join";
    // Define the keys for the quit message section
    private static final String quitMessageKey = "Quit Messages";

    // Get a list of chat message groups
    public static List<String> getDefinedChatMessageGroups() {
        // Get the group file
        GroupFiles groupFiles = new GroupFiles();
        // Refresh the group file just in case any changes were made
        groupFiles.reloadConfig();
        // Get the chat message configuration section
        ConfigurationSection chatSection = groupFiles.getConfig().getConfigurationSection(chatMessageKey);
        // Check if the chat section exists
        if (chatSection == null) {
            // Chat section is invalid, go with default
            return List.of("default");
        }
        // Return a list of all the groups defined under the chat message section
        return chatSection.getKeys(false).stream().toList();
    }

    // Get a list of first join message groups
    public static List<String> getDefinedFirstJoinMessageGroups() {
        // Get the group file
        GroupFiles groupFiles = new GroupFiles();
        // Refresh the group file just in case any changes were made
        groupFiles.reloadConfig();
        // Get the first join message configuration section
        ConfigurationSection firstJoinSection = groupFiles.getConfig().getConfigurationSection(fistJoinMessageKey);
        // Check if the first join message section exists
        if (firstJoinSection == null) {
            // Chat section is invalid, go with default
            return List.of("default");
        }
        // Return a list of all the groups defined under the first join message section
        return firstJoinSection.getKeys(false).stream().toList();
    }

    // Get a list of join message groups
    public static List<String> getDefinedJoinMessageGroups() {
        // Get the group file
        GroupFiles groupFiles = new GroupFiles();
        // Refresh the group file just in case any changes were made
        groupFiles.reloadConfig();
        // Get the join message configuration section
        ConfigurationSection joinMessageSection = groupFiles.getConfig().getConfigurationSection(joinMessageKey);
        // Check if the join message section exists
        if (joinMessageSection == null) {
            // Chat section is invalid, go with default
            return List.of("default");
        }
        // Return a list of all the groups defined under the join message section
        return joinMessageSection.getKeys(false).stream().toList();
    }

    // Get a list of quit message groups
    public static List<String> getDefinedQuitMessageGroups() {
        // Get the group file
        GroupFiles groupFiles = new GroupFiles();
        // Refresh the group file just in case any changes were made
        groupFiles.reloadConfig();
        // Get the quit message configuration section
        ConfigurationSection quitMessageSection = groupFiles.getConfig().getConfigurationSection(quitMessageKey);
        // Check if the quit message section exists
        if (quitMessageSection == null) {
            // Quit message section is invalid, go with default
            return List.of("default");
        }
        // Return a list of all the groups defined under the quit message section
        return quitMessageSection.getKeys(false).stream().toList();
    }

    public static String getGroup(CommandSender sender) {
        // Get the student's primary group
        return plugin.getPermissions().getPrimaryGroup((Player) sender);
    }

    public static List<String> getGroups() {
        // Return a list of all the registered groups on the server
        return List.of(plugin.getPermissions().getGroups());
    }

}
