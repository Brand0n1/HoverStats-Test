package me.brand0n_.hoverstats.Utils.Chat;

import me.brand0n_.hoverstats.HoverStats;
import me.brand0n_.hoverstats.Utils.Updates.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Messages {
    private static final HoverStats plugin = HoverStats.getPlugin(HoverStats.class); // Get this from main

    public static void sendHelpMessage(CommandSender sender) {
        List<String> path = plugin.getConfig().getStringList("Messages.Help");
        for (String MESSAGE : path) {
            if (sender instanceof Player) {
                sender.sendMessage(Placeholders.addPlaceholders((Player) sender, MESSAGE));
                continue;
            }
             Bukkit.getServer().getConsoleSender().sendMessage("[" + plugin.getName() + "]" + Placeholders.formatPlaceholders(MESSAGE));
        }
    }

    public static void reloaded(CommandSender sender) {
        String path = plugin.getConfig().getString("Messages.System Messages.Success.Reloaded", "%success% %pluginname%'s &7config has successfully reloaded!");
        if (sender instanceof Player) {
            sender.sendMessage(Placeholders.addPlaceholders((Player) sender, path));
            return;
        }
        Bukkit.getServer().getConsoleSender().sendMessage(Placeholders.formatPlaceholders(path));
    }

    public static void noPermissions(CommandSender sender) {
        String path = plugin.getConfig().getString("Messages.System Messages.Error.No Permissions", "%error% &7You don't have permission to run this command!");
        if (sender instanceof Player) {
            sender.sendMessage(Placeholders.addPlaceholders((Player) sender, path));
            return;
        }
         Bukkit.getServer().getConsoleSender().sendMessage("[" + plugin.getName() + "]" + Placeholders.formatPlaceholders(path));
    }

    public static String papiHookSuccess() {
        String path = plugin.getConfig().getString("Messages.System Messages.Success.PAPI Hook", "&aFound PlaceholderAPI, hooking into it.");
        return Placeholders.formatPlaceholders(path);
    }
    public static String papiHookFailed() {
        String path = plugin.getConfig().getString("Messages.System Messages.Error.PAPI Hook", "&aCouldn't find PlaceholderAPI, all placeholders from PAPI won't work.");
        return Placeholders.formatPlaceholders(path);
    }

    public static String vaultHookSuccess() {
        String path = plugin.getConfig().getString("Messages.System Messages.Success.Vault Hook", "&aFound Vault, hooking into it.");
        return Placeholders.formatPlaceholders(path);
    }
    public static String vaultHookFailed() {
        String path = plugin.getConfig().getString("Messages.System Messages.Error.Vault Hook", "&cCouldn't find Vault, permission based commands will not work until vault is installed.");
        return Placeholders.formatPlaceholders(path);
    }

    public static void usageError(CommandSender sender, String usage) {
        String path = plugin.getConfig().getString("Messages.System Messages.Error.Usage", "%error% &eUsage Error &8| &7%usage%");
        path = path.replaceAll("%usage%", usage);
        if (sender instanceof Player) {
            sender.sendMessage(Placeholders.addPlaceholders((Player) sender, path));
            return;
        }
        Bukkit.getServer().getConsoleSender().sendMessage("[" + plugin.getName() + "]" + Placeholders.formatPlaceholders(path));
    }

    public static void invalidGroup(CommandSender sender, String group) {
        String path = plugin.getConfig().getString("Messages.System Messages.Error.Invalid Group", "%error% &7The group you entered (&b%group%&7) is invalid! Groups are case sensitive and must be exact, please try again, checking your spelling and cases.");
        path = path.replaceAll("%group%", group);
        if (sender instanceof Player) {
            sender.sendMessage(Placeholders.addPlaceholders((Player) sender, path));
            return;
        }
        Bukkit.getServer().getConsoleSender().sendMessage("[" + plugin.getName() + "]" + Placeholders.formatPlaceholders(path));
    }

    public static void invalidPlayer(CommandSender sender, String player) {
        String path = plugin.getConfig().getString("Messages.System Messages.Error.Invalid Player", "%error% &7The player you entered (&b%player%&7) is invalid! Please try again.");
        path = path.replaceAll("%player%", player);
        if (sender instanceof Player) {
            sender.sendMessage(Placeholders.addPlaceholders((Player) sender, path));
            return;
        }
        Bukkit.getServer().getConsoleSender().sendMessage("[" + plugin.getName() + "]" + Placeholders.formatPlaceholders(path));
    }

    public static void alreadyBlacklisted(CommandSender sender, String option, String input) {
        String path = plugin.getConfig().getString("Messages.System Messages.Error.Already Blacklisted", "%error% &7The %option% you entered (&b%input%&7) is already blacklisted, to remove them please use the /whitelist command.");
        path = path.replaceAll("%option%", option).replaceAll("%input%", input);
        if (sender instanceof Player) {
            sender.sendMessage(Placeholders.addPlaceholders((Player) sender, path));
            return;
        }
        Bukkit.getServer().getConsoleSender().sendMessage("[" + plugin.getName() + "]" + Placeholders.formatPlaceholders(path));
    }

    public static void notBlacklisted(CommandSender sender, String option, String input) {
        String path = plugin.getConfig().getString("Messages.System Messages.Error.Not Blacklisted", "%error% &7The %option% you entered (&b%input%&7) is not blacklisted, to add them please use the /blacklist command.");
        path = path.replaceAll("%option%", option).replaceAll("%input%", input);
        if (sender instanceof Player) {
            sender.sendMessage(Placeholders.addPlaceholders((Player) sender, path));
            return;
        }
        Bukkit.getServer().getConsoleSender().sendMessage("[" + plugin.getName() + "]" + Placeholders.formatPlaceholders(path));
    }

    public static void addedBlacklist(CommandSender sender, String option, String input) {
        String path = plugin.getConfig().getString("Messages.System Messages.Success.Added Blacklisted", "%success% &7Successfully added %option% (&b%input%&7) to the blacklist.");
        path = path.replaceAll("%option%", option).replaceAll("%input%", input);
        if (sender instanceof Player) {
            sender.sendMessage(Placeholders.addPlaceholders((Player) sender, path));
            return;
        }
        Bukkit.getServer().getConsoleSender().sendMessage("[" + plugin.getName() + "]" + Placeholders.formatPlaceholders(path));
    }

    public static void addedWhitelist(CommandSender sender, String option, String input) {
        String path = plugin.getConfig().getString("Messages.System Messages.Success.Added Whitelist", "%success% &7Successfully added %option% (&b%input%&7) to the whitelist.");
        path = path.replaceAll("%option%", option).replaceAll("%input%", input);
        if (sender instanceof Player) {
            sender.sendMessage(Placeholders.addPlaceholders((Player) sender, path));
            return;
        }
        Bukkit.getServer().getConsoleSender().sendMessage("[" + plugin.getName() + "]" + Placeholders.formatPlaceholders(path));
    }

    public static void sendVersionInfo(CommandSender sender) {
        // Define the message being sent
        String message = "\n&b" + plugin.getName() + " &8[&a" + plugin.getDescription().getVersion() + "&8]\n" +
                "&7&l&m⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯&r\n" +
                "  &7Author: &a" + plugin.getDescription().getAuthors().toString().replace("[", "").replace("]", "") + "\n \n" +
                "  &7Config Version: &a" + plugin.getConfig().getString("Version", "Unknown") + "\n" +
                "&7&l&m⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯&r\n" +
                "  &7Latest Update: &9" + UpdateChecker.getLatestUpdateLink() + "\n" +
                "&7&l&m⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯&r\n";
        if (sender instanceof Player) {
            sender.sendMessage(Placeholders.addPlaceholders((Player) sender, message));
            return;
        }
        Bukkit.getServer().getConsoleSender().sendMessage("[" + plugin.getName() + "]" + Placeholders.formatPlaceholders(message));
    }
}
