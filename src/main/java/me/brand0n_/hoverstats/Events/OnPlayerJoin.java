package me.brand0n_.hoverstats.Events;

import me.brand0n_.hoverstats.HoverStats;
import me.brand0n_.hoverstats.Utils.Chat.Colors;
import me.brand0n_.hoverstats.Utils.Chat.Placeholders;
import me.brand0n_.hoverstats.Utils.Files.GroupFiles;
import me.brand0n_.hoverstats.Utils.Hover.HoverUtils;
import me.brand0n_.hoverstats.Utils.Permissions.Permissions;
import me.brand0n_.hoverstats.Utils.Permissions.Ranks;
import me.brand0n_.hoverstats.Utils.Updates.UpdateChecker;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OnPlayerJoin implements EventExecutor, Listener {
    private static final HoverStats plugin = HoverStats.getPlugin(HoverStats.class); // Get this from main

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) {
        this.onPlayerChat((PlayerJoinEvent) event);
    }

    @EventHandler
    public void onPlayerChat(PlayerJoinEvent e) {
        // Send player the clickable message
        UpdateChecker.sendUpdateMessage(e.getPlayer(), "hoverstats.update");
        // Get the player
        Player p = e.getPlayer();

        // Check if the plugin is using its own formatting
        if (!plugin.getConfig().getBoolean("Formatting.Use Join Formatting", true)) {
            // Plugin doesn't want to use its own formatting, return
            return;
        }

        // Check if the player has the permission to run this
        if (!Permissions.hasPermission(p, "hoverstats.join-formatting")) {
            // Player doesn't have the permission to use join formatting, don't send a message
            return;
        }
        
        // Check if the join message is set to null
        if (e.getJoinMessage() == null) {
            // Check if the player has debug messages enabled
            if (plugin.getConfig().getBoolean("Debug Mode", false)) {
                // Print to console that another plugin may have already changed the join message
                Bukkit.getServer().getConsoleSender().sendMessage(Colors.chatColor("[HoverStats]&c The join message is currently set to 'null', this means that another plugin is likely interfering with the join message."));
                // Print to console possible solutions
                Bukkit.getServer().getConsoleSender().sendMessage(Colors.chatColor("[HoverStats]&c Changing the 'Join Event Priority' in the config may fix this issue, other than that contact the support discord."));
            }
            // Another plugin has changed the join  message, more than likely a vanish plugin don't do anything
            return;
        }
        // Set the default rank
        String playerJoinMessageRank = "default";

        // Check if the plugin wants to use groups
        if (plugin.useGroups) {
            // Get the player's group
            String pRank = Ranks.getGroup(p);
            // Get the defined join message groups
            List<String> joinGroups = Ranks.getDefinedJoinMessageGroups();
            // Get the ranks that are defined in the Join Message section
            playerJoinMessageRank = joinGroups.contains(pRank) ? joinGroups.get(joinGroups.indexOf(pRank)) : "default";
        }

        // Get the groups file
        GroupFiles groupsFiles = new GroupFiles();

        // Get the message from config
        String joinMessage = groupsFiles.getConfig().getString("Join Messages.Standard Join." + playerJoinMessageRank + ".Message Format", "&8[&a+&8] &7%displayname%");

        // Set the join message to be nothing
        e.setJoinMessage(null);

        // Check if this is the player's first time joining the server
        if (!p.hasPlayedBefore()) {
            // Set the default rank
            String playerFirstJoinMessageRank = "default";
            // Check if the plugin wants to use groups
            if (plugin.useGroups) {
                // Get the player's group
                String pRank = Ranks.getGroup(p);
                // Get the defined first join message groups
                List<String> fistJoinGroups = Ranks.getDefinedFirstJoinMessageGroups();
                // Get the ranks that are defined in the First Join Message section
                playerFirstJoinMessageRank = fistJoinGroups.contains(pRank) ? fistJoinGroups.get(fistJoinGroups.indexOf(pRank)) : "default";
            }
            // Get the join message format for the first join message
            joinMessage = groupsFiles.getConfig().getString("Join Messages.First Join." + playerFirstJoinMessageRank + ".Others.Message Format", "&7Welcome &b%displayname%&7 to the server!");
        }

        // Format the join message
        joinMessage = Placeholders.addPlaceholders(p, joinMessage);

        // Get the final output message
        TextComponent joinHoverMessage = formatJoinHoverMessage(p, joinMessage, p.hasPlayedBefore(), playerJoinMessageRank);
        TextComponent joinHoverOthersMessage = formatJoinHoverMessageOthers(p, joinMessage, p.hasPlayedBefore(), playerJoinMessageRank);

        // Get a list of all online players
        for (Player targetPlayer : Bukkit.getServer().getOnlinePlayers()) {
            // Check if the target player is the user
            if (targetPlayer.equals(p)) {
                // Send the player who joined a different message than the rest of the players
                p.spigot().sendMessage(joinHoverMessage);
                // Nothing left to do, continue
                continue;
            }
            // Send the target player the join message
            targetPlayer.spigot().sendMessage(joinHoverOthersMessage);
        }
        // Send console the message
        Bukkit.getServer().getConsoleSender().sendMessage(joinMessage);
        // Cancel the event
        e.setJoinMessage(null);
    }

    private TextComponent formatJoinHoverMessage(Player p, String message, boolean isNotFirstJoin, String playerGroup) {
        // Make an empty main message element for future use
        TextComponent mainMessage = new TextComponent();
        // Create the hover event by calling on another method
        TextComponent hoverEvents = HoverUtils.setupHoverFirstJoinSelfMessage(p, message, playerGroup);

        // Check if this is the first join message
        if (isNotFirstJoin) {
            // Create the hover event by calling on another method
            hoverEvents = HoverUtils.setupHoverJoinMessage(p, message, playerGroup);
        }

        // Add the hover message to the final output
        mainMessage.addExtra(hoverEvents);
        // Return the final output
        return mainMessage;
    }

    private TextComponent formatJoinHoverMessageOthers(Player p, String message, boolean isNotFirstJoin, String playerGroup) {
        // Make an empty main message element for future use
        TextComponent mainMessage = new TextComponent();
        // Create the hover event by calling on another method
        TextComponent hoverEvents = HoverUtils.setupHoverFirstJoinOthersMessage(p, message, playerGroup);

        // Check if this is the first join message
        if (isNotFirstJoin) {
            // Create the hover event by calling on another method
            hoverEvents = HoverUtils.setupHoverJoinMessage(p, message, playerGroup);
        }

        // Add the hover message to the final output
        mainMessage.addExtra(hoverEvents);
        // Return the final output
        return mainMessage;
    }
}