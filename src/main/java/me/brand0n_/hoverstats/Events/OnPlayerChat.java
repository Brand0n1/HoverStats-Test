package me.brand0n_.hoverstats.Events;

import me.brand0n_.hoverstats.HoverStats;
import me.brand0n_.hoverstats.Utils.Chat.Colors;
import me.brand0n_.hoverstats.Utils.Chat.Placeholders;
import me.brand0n_.hoverstats.Utils.Files.GroupFiles;
import me.brand0n_.hoverstats.Utils.Hover.HoverUtils;
import me.brand0n_.hoverstats.Utils.Permissions.Blacklist;
import me.brand0n_.hoverstats.Utils.Permissions.Permissions;
import me.brand0n_.hoverstats.Utils.Permissions.Ranks;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class OnPlayerChat implements EventExecutor, Listener {
    private static final HoverStats plugin = HoverStats.getPlugin(HoverStats.class); // Get this from main

    private boolean hasFinalSpace = false;


    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) {
        this.onPlayerChat((AsyncPlayerChatEvent) event);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        // Get the player
        Player p = e.getPlayer();

        // Check if the user has debug mode on
        if (plugin.getConfig().getBoolean("Debug Mode", false)) {
            // Tell what check this is
            plugin.getLogger().warning("At event Start.");
            // Send a spacer message
            plugin.getLogger().warning("---");
            // Tell what the current chat format is
            plugin.getLogger().warning("Chat Format: "+e.getFormat());
            // Tell what the current chat message is
            plugin.getLogger().warning("Chat Message: "+e.getMessage());
            // Send a spacer message
            plugin.getLogger().warning("---");
            // Check if there is players inside the recipient list.
            plugin.getLogger().warning("Who was receiving this chat message?");
            // Check if there are any recipients of this message
            if (e.getRecipients().isEmpty()) {
                // Send player a message saying no one should receive this message
                plugin.getLogger().warning("No one was receiving this message.");
            } else {
                // Send player recipient list
                plugin.getLogger().warning(e.getRecipients().toString());
            }
            // Send a spacer message
            plugin.getLogger().warning("---");
            // Send a spacer message
            plugin.getLogger().warning("");
        }

        // For plugin compatibility check if the event is cancelled by another plugin.
        if (e.isCancelled()) {
            return;
        }

        // Save the previous formatting
        StringBuilder prevFormat = new StringBuilder(e.getFormat().split("%1\\$s")[0].replaceAll("§", "&"));
        // Check if the string contains a '<'
        if (prevFormat.toString().contains("<")) {
            // Format the previous formatted message
            prevFormat.deleteCharAt(prevFormat.lastIndexOf("<"));
        }
        // Get the groups file
        GroupFiles groupsFiles = new GroupFiles();

        // Set the default rank
        String playerRank = "default";

        // Check if the plugin wants to use groups
        if (plugin.useGroups) {
            // Get the player's group
            String pRank = Ranks.getGroup(p);
            // Get the defined chat message groups
            List<String> chatGroups = Ranks.getDefinedChatMessageGroups();
            // Get the ranks that are defined in the Join Message section
            playerRank = chatGroups.contains(pRank) ? chatGroups.get(chatGroups.indexOf(pRank)) : "default";
        }

        // Check if the plugin is using its own formatting
        if (plugin.getConfig().getBoolean("Formatting.Chat Formatting.Use Formatting", true)) {
            // Get the format from config
            String newFormatting = getNewFormatting(p, groupsFiles.getConfig().getString("Chat Messages."+playerRank+".Message Format", "&7%displayname% &8&l> &7%message%")).replaceAll("(?i)%prev-format%", prevFormat.toString().trim())
                    .replaceAll("%", "{TD-Temp}").replace("{TD-Temp}1$s", "%1$s").replace("{TD-Temp}2$s", "%2$s");

            // Set the chat format to be the new format defined in the config file, ensure if the config wants the previous format that it will be provided
            e.setFormat(Placeholders.addPlaceholders(p, newFormatting));
        }

        // Check if the format is valid
        if (!e.getFormat().contains("%2$s")) {
            // Detected a plugin that is using the wrong type of formatting
            // Update the format to have the proper minecraft message format of %2$s
            // Get the target location
            int targetIndex = e.getFormat().lastIndexOf(e.getMessage());
            // Remove the target message given the target location, and add back the remainder of the message
            e.setFormat(e.getFormat().substring(0, targetIndex) + e.getFormat().substring(targetIndex+e.getMessage().length()));
        }

        // Format chat based on the current chat format
        String format = formatChat(p, e.getFormat());
        // Set the format for the chat
        e.setFormat(format);

        // Format the message
        String message = formatMessage(p, e.getMessage(), format);
        // Set the message that the server will get equal to the newly formatted message
        e.setMessage(message);

        // Check if the user has debug mode on
        if (plugin.getConfig().getBoolean("Debug Mode", false)) {
            // Tell what check this is
            plugin.getLogger().warning("Pre Add all user check.");
            // Send a spacer message
            plugin.getLogger().warning("---");
            // Tell what the current chat format is
            plugin.getLogger().warning("Chat Format: "+e.getFormat());
            // Tell what the current chat message is
            plugin.getLogger().warning("Chat Message: "+e.getMessage());
            // Send a spacer message
            plugin.getLogger().warning("---");
            // Check if there is players inside the recipient list.
            plugin.getLogger().warning("Who was receiving this chat message?");
            // Check if there are any recipients of this message
            if (e.getRecipients().isEmpty()) {
                // Send player a message saying no one should receive this message
                plugin.getLogger().warning("No one was receiving this message.");
            } else {
                // Send player recipient list
                plugin.getLogger().warning(e.getRecipients().toString());
            }
            // Send a spacer message
            plugin.getLogger().warning("---");
            // Send a spacer message
            plugin.getLogger().warning("");
        }

        // Check if there are no recipients
        if (e.getRecipients().isEmpty()) {
            // The list is empty, even the player won't get sent a message? Some plugin must be messing with chat recipients, reset the recipient list
            e.getRecipients().addAll(Bukkit.getOnlinePlayers());
        }

        // Check if the user has debug mode on
        if (plugin.getConfig().getBoolean("Debug Mode", false)) {
            // Tell what check this is
            plugin.getLogger().warning("Post Add all users check");
            // Send a spacer message
            plugin.getLogger().warning("---");
            // Tell what the current chat format is
            plugin.getLogger().warning("Chat Format: "+e.getFormat());
            // Tell what the current chat message is
            plugin.getLogger().warning("Chat Message: "+e.getMessage());
            // Send a spacer message
            plugin.getLogger().warning("---");
            // Check if there is players inside the recipient list.
            plugin.getLogger().warning("Who is receiving this chat message?");
            // Check if there are any recipients of this message
            if (e.getRecipients().isEmpty()) {
                // Send player a message saying no one should receive this message
                plugin.getLogger().warning("No one will receive this message.");
            } else {
                // Send player recipient list
                plugin.getLogger().warning(e.getRecipients().toString());
            }
            // Send a spacer message
            plugin.getLogger().warning("---");
            // Send a spacer message
            plugin.getLogger().warning("");
        }

        // Ensure that the event isn't cancelled
        if (!e.isCancelled()) {
            // Send the player the hover able chat message
            List<Player> playersLeft = sendHoverMessage(p, e.getRecipients(), e.getFormat(), e.getMessage(), playerRank);

            // Remove the user from the list of recipients if they have already been sent the message
            e.getRecipients().removeIf(target -> !playersLeft.contains(target));
        }

        // Check if the user has debug mode on
        if (plugin.getConfig().getBoolean("Debug Mode", false)) {
            // Tell what check this is
            plugin.getLogger().warning("Post Send Message check");
            // Send a spacer message
            plugin.getLogger().warning("---");
            // Tell what the current chat format is
            plugin.getLogger().warning("Chat Format: "+e.getFormat());
            // Tell what the current chat message is
            plugin.getLogger().warning("Chat Message: "+e.getMessage());
            // Send a spacer message
            plugin.getLogger().warning("---");
            // Check if there is players inside the recipient list.
            plugin.getLogger().warning("Who is receiving this chat message?");
            // Check if there are any recipients of this message
            if (e.getRecipients().isEmpty()) {
                // Send player a message saying no one should receive this message
                plugin.getLogger().warning("No one will receive this message.");
            } else {
                // Send player recipient list
                plugin.getLogger().warning(e.getRecipients().toString());
            }
            // Send a spacer message
            plugin.getLogger().warning("---");
            // Send a spacer message
            plugin.getLogger().warning("");
        }
        // Set the proper format for the console to read
        e.setFormat(e.getFormat() + "%2$s");
    }

    private String getNewFormatting(Player p, String format) {
        // Add in the required placeholders to the provided format
        format = format
                // Replace the message placeholder with the default minecraft placeholder
                .replace("%message%", "%2$s")
                // Replace the name placeholder with the players name
                .replace("%name%", p.getName())
                // Replace the displayname placeholder with the default minecraft placeholder
                .replace("%displayname%", "%1$s");
        // Add in color formatting to the given format
        return Placeholders.addPlaceholders(p, format);
    }

    private String formatChat(Player p, String str) {
        // Replace the vanilla formatting with an empty space to make way for the plugin's custom formatting
        str = str.replace("%2$s", "");
        // Replace the vanilla formatting with the players display name
        str = str.replace("%1$s", p.getDisplayName());
        // Check if the string has a space before the message
        hasFinalSpace = String.valueOf(str.charAt(str.length() - 1)).equalsIgnoreCase(" ");
        // Colorize the new formatting
        str = Colors.chatColor(str);
        // Return the updated format after removing any trailing or leading spaces to clean up the output
        return str.trim();
    }

    private String formatMessage(Player p, String str, String format) {
        // Add the final color from the format to the string
        str = Colors.finalChatColor(format) + str;

        // Check if the player is allowed to use hex colors in chat
        if (Permissions.hasPermission(p, "hoverstats.colors.hex")) {
            // Player has access to use hex colors in chat, colors any hex in the message
            str = Colors.addHex(str);
        }

        // Loop through all colors loaded by spigot
        for (ChatColor color : Colors.getAllRegisteredChatColors()) {
            // Check if the string contains a color code
            if (!str.contains("&" + color.getChar())) {
                // String doesn't contain color code, continue to next color
                continue;
            }
            // Check if the player has the permission to use this color
            if (Permissions.hasPermission(p, "hoverstats.colors", color.name().toLowerCase()) || Permissions.hasPermission(p, "hoverstats.colors.*")) {
                // Player has the permission to use this color code, replace the color code with the formatted color
                str = str.replace("&" + color.getChar(), color.toString());
            }
        }
        // Loop through all magic colors loaded
        for (ChatColor magic : Colors.getAllRegisteredMagicColors()) {
            // Check if the string contains a magic code
            if (!str.contains("&" + magic.getChar())) {
                // String doesn't contain magic code, continue to next color
                continue;
            }
            // Check if the player has the permission to use this color
            if (Permissions.hasPermission(p, "hoverstats.magic", magic.name().toLowerCase()) || Permissions.hasPermission(p, "hoverstats.magic.*")) {
                // Player has the permission to use this magic code, replace the magic code with the formatted color
                str = str.replace("&" + magic.getChar(), magic.toString());
            }
        }
        // Replace the string message with the provided string and return it
        return str.replace("%2$s", str);
    }

    private TextComponent formatHoverMessage(Player p, String format, String message, String playerGroup) {
        // Make an empty main message element for future use
        TextComponent mainMessage = new TextComponent();
        // Create the hover event by calling on another method
        TextComponent hoverEvents = HoverUtils.setupHoverChatMessage(p, format, playerGroup);
        // Create a message element that will hold the formatted message
        TextComponent eMessage = new TextComponent(TextComponent.fromLegacyText(message.replace("%2$s", "")));

        // Check if the output had a final space in front of the message
        if (hasFinalSpace) {
            // There is supposed to be a space separating the message and the player name, replace the current message with the newly formatted one with a space
            eMessage = new TextComponent(TextComponent.fromLegacyText(" " + message.replace("%2$s", "")));
        }

        // Add the hover message to the final output
        mainMessage.addExtra(hoverEvents);
        // Add the message to the final output
        mainMessage.addExtra(eMessage);
        // Return the final output
        return mainMessage;
    }

    private List<Player> sendHoverMessage(Player p, Set<Player> recipients, String format, String message, String playerGroup) {
        // Create an empty list of players
        List<Player> playersLeft = new ArrayList<>();

        // Loop through all online players
        for (Player player : recipients) {
            // Set the default target group
            String targetRank = "default";
            // Check if the plugin wants to use groups
            if (plugin.useGroups) {
                // Update the target's group
                targetRank = Ranks.getGroup(p);
            }
            // Format the relative placeholders if any
            format = Placeholders.setRelativePlaceholder(p, player, format.replaceAll("\\{TD-Temp}", "%"));
            // Get the final output message
            TextComponent hoverMessage = formatHoverMessage(p, format.replace("%%", "%"), message, playerGroup);
            // Check if the plugin utilizes permission based messages
            if (plugin.getConfig().getBoolean("Formatting.Chat Formatting.Require Permission", false)) {
                // Check if the player has the required permission
                if (!Permissions.hasPermission(player, "hoverstats.chat.view")) {
                    // Add the player to a list of users that the message was not sent to
                    playersLeft.add(player);
                    // Nothing left to do, continue
                    continue;
                }
            }
            // Check if the target's rank is in the Hover Blacklist
            if (Blacklist.getBlackListGroups().contains(targetRank) || Blacklist.getBlacklistUsers().contains(player.getName())) {
                // Add the player to a list of users that the message was not sent to
                playersLeft.add(player);
                // Nothing left to do, continue
                continue;
            }
            // Send each player the final outputted message
            player.spigot().sendMessage(hoverMessage);
        }
        // Return the list of player's that are supposed to get the original message (pre hover)
        return playersLeft;
    }
}
