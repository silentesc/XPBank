package de.silentesc.xpbank;

import de.silentesc.xpbank.commands.ReloadConfigCommand;
import de.silentesc.xpbank.utils.PluginConfig;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

public final class Main extends JavaPlugin {

    private static Main INSTANCE;
    private final String configPath = "config.yaml";
    private PluginConfig pluginConfig;

    public Main() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        loadConfig();
        loadCommands();
    }

    private void loadConfig() {
        File configFile = new File(String.format("%s/%s", getDataFolder().getPath(), configPath));
        if (!configFile.exists()) {
            saveResource(configPath, false);
        }
        pluginConfig = new PluginConfig();
    }

    private void loadCommands() {
        Objects.requireNonNull(Bukkit.getPluginCommand("xpbank_reloadcfg")).setExecutor(new ReloadConfigCommand());
    }

    /**
     * Getters
     */

    public static Main getINSTANCE() {
        return INSTANCE;
    }
    public String getConfigPath() {
        return configPath;
    }
    public PluginConfig getPluginConfig() {
        return pluginConfig;
    }
}
