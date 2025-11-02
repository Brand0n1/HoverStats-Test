package me.brand0n_.hoverstats;

import me.brand0n_.hoverstats.Commands.CommandUtils;
import me.brand0n_.hoverstats.Events.EventUtils;
import me.brand0n_.hoverstats.Utils.Chat.Colors;
import me.brand0n_.hoverstats.Utils.Chat.Messages;
import me.brand0n_.hoverstats.Utils.Chat.Placeholders;
import me.brand0n_.hoverstats.Utils.Files.GroupFiles;
import me.brand0n_.hoverstats.Utils.Updates.ConfigChecker;
import me.brand0n_.hoverstats.Utils.Updates.UpdateChecker;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class HoverStats extends JavaPlugin {
    // Spigot id
    public String resourceID = "100245";

    // Plugin Instance
    public static HoverStats getInstance;

    // Variable
    public boolean usePAPI = getConfig().getBoolean("PAPI hook", true);
    public boolean useGroups = getConfig().getBoolean("Vault hook", true);
    public boolean useHex = false;
    // Get Vault Permissions
    private static Permission perms = null;

    @Override
    public void onEnable() {
        // Save the config
        saveDefaultConfig();
        // Check hex version
        useHex = Colors.isCorrectVersionHex();
        // Check if the config is outdated
        ConfigChecker.checkUpdates();
        ConfigChecker.checkConfig();
        // Setup classes
        setupClasses();
        // Check if SoftDepends on are loaded
        checkSoftDependentPlugins();
        // Create the death message file
        new GroupFiles().saveConfig();
        new GroupFiles().reloadConfig();
        // Check if the plugin needs an update
        UpdateChecker.sendConsoleUpdateMessage();
    }

    private void setupClasses() {
        getInstance = this;
        CommandUtils.init();
        EventUtils.init();
    }

    private void checkSoftDependentPlugins() {
        // Check if PlaceholderAPI is installed
        if (Placeholders.hasPAPI()) {
            Bukkit.getServer().getConsoleSender().sendMessage("[HoverStats] " + Messages.papiHookSuccess());
        } else {
            Bukkit.getServer().getConsoleSender().sendMessage("[HoverStats] " + Messages.papiHookFailed());
            usePAPI = false;
        }
        // Check if Vault is installed
        if (this.getServer().getPluginManager().getPlugin("Vault") != null) {
            Bukkit.getServer().getConsoleSender().sendMessage("[HoverStats] " + Messages.vaultHookSuccess());
            // Setup vault permissions
            setupPermissions();
        } else {
            Bukkit.getServer().getConsoleSender().sendMessage("[HoverStats] " + Messages.vaultHookFailed());
            useGroups = false;
        }
        // Check if Luck Perms is installed
        if (this.getServer().getPluginManager().getPlugin("LPC") != null) {
            // Check if the config is already set to recommended values
            if (!getConfig().getString("Chat Event Priority", "HIGH").equalsIgnoreCase("MONITOR")) {
                // Tell the user they are using luck perms chat and that the plugin will attempt to compensate for this
                Bukkit.getLogger().warning("[HoverStats] Luck Perms Chat (LPC) detected, attempting to fix compatibility issues.");
                // Change the settings for the chat event
                getConfig().set("Chat Event Priority", "MONITOR");
                // Save the changes made to the config
                saveConfig();
                // Attempt to unregister events
                EventUtils.unRegister();
                // Attempt to register events
                EventUtils.init();
                // Tell the user they are using luck perms chat and that the plugin will attempt to compensate for this
                Bukkit.getServer().getConsoleSender().sendMessage(Placeholders.formatPlaceholders("[HoverStats] &aSuccessfully changed the config value to work with &bLPC&a."));
                // Tell the user that they may have to restart the server to see results
                Bukkit.getLogger().severe("[HoverStats] A server restart may be necessary for chat formatting to take place.");
            }
            // Check if the user already has their use formatting set to false
            if (getConfig().getBoolean("Formatting.Chat Formatting.Use Formatting", true) && getConfig().getBoolean("Formatting.Chat Formatting.Override LPC", true)) {
                // Tell the user that internal chat formatting will be disabled in favor of LPC
                Bukkit.getServer().getConsoleSender().sendMessage(Placeholders.formatPlaceholders("[HoverStats] &bLPC &7detected favoring their chat formatter over &bHoverStats &7chat formatter."));
                // Change the config value for use formatting to false since LPC is present
                getConfig().set("Formatting.Chat Formatting.Use Formatting", false);
                // Save the changes made to the config
                saveConfig();
            }
        }
    }

    public void reloadPlugin(CommandSender sender) {
        // Reload the config
        reloadConfig();
        // Save config with comments
        saveDefaultConfig();
        // Save config
        saveConfig();

        // Get the group files
        GroupFiles groupFiles = new GroupFiles();
        // Reload the Group File's config
        groupFiles.reloadConfig();
        // Save the config with comments
        groupFiles.saveDefaultConfig();
        // Save the config
        groupFiles.saveConfig();

        // Uninitialized Events
        EventUtils.unRegister();
        // Reinitialize Events
        EventUtils.init();

        // Send reload message
        Messages.reloaded(sender);
    }

    private void setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = this.getServer().getServicesManager().getRegistration(Permission.class);
        if (rsp != null) {
            perms = rsp.getProvider();
        }
    }

    public Permission getPermissions() {
        return perms;
    }
}
