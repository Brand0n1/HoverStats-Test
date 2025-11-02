package me.brand0n_.hoverstats.Utils.Files;

import me.brand0n_.hoverstats.HoverStats;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class GroupFiles {
    private static final HoverStats plugin = HoverStats.getPlugin(HoverStats.class); // Get this from main
    private FileConfiguration dataConfig = null;
    private File configFile = null;
    private final String fileName = "groups.yml";

    public GroupFiles() {
        // Saves/Initializes the config
        saveDefaultConfig();
    }

    public void reloadConfig() {
        if (!this.configFile.exists()) {
            saveDefaultConfig();
            return;
        }
        if (this.configFile == null) {
            this.configFile = new File(plugin.getDataFolder(), fileName);
        }

        this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);
        InputStream defaultStream = plugin.getResource(fileName);

        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.dataConfig.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getConfig() {
        if (this.dataConfig == null) {
            reloadConfig();
        }
        return this.dataConfig; // Get config
    }

    public void saveConfig() {
        if (this.dataConfig == null || this.configFile == null) {
            return;
        }

        try {
            this.getConfig().save(configFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.configFile, e);
        }
    }

    public void saveDefaultConfig() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), fileName);
        }

        if (!this.configFile.exists()) {
            plugin.saveResource(fileName, false);
        }
    }
}