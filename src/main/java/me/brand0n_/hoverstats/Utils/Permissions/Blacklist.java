package me.brand0n_.hoverstats.Utils.Permissions;

import me.brand0n_.hoverstats.HoverStats;

import java.util.List;

public class Blacklist {
    private static final HoverStats plugin = HoverStats.getPlugin(HoverStats.class); // Get this from main

    // Get the config section for the group blacklist
    private static final String groupBlacklistKey = "Hover Blacklist.Groups";
    // Get the config section for the user blacklist
    private static final String userBlacklistKey = "Hover Blacklist.Users";

    public static List<String> getBlackListGroups() {
        // Return the list of groups that are blacklisted
        return plugin.getConfig().getStringList(groupBlacklistKey);
    }

    public static List<String> getBlacklistUsers() {
        // Return the list of users that are blacklisted
        return plugin.getConfig().getStringList(userBlacklistKey);
    }

}
