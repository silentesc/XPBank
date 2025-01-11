package de.silentesc.xpbank.utils;

import de.silentesc.xpbank.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class FileConfig extends YamlConfiguration {
    private final String path;

    public FileConfig(String fileName) {
        path = String.format("%s/%s", Main.getINSTANCE().getDataFolder().getPath(), fileName);
        File file = new File(path);
        if (!file.exists()) {
            saveConfig();
        }
        try {
            load(path);
        } catch (InvalidConfigurationException | IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, String.format("Error while loading config inside FileConfig: %s", e));
        }
    }

    public void saveConfig() {
        try {
            save(path);
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, String.format("Error while saving config inside FileConfig: %s", e));
        }
    }
}
