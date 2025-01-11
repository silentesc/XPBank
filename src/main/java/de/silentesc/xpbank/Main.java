package de.silentesc.xpbank;

import de.silentesc.xpbank.commands.BankCommand;
import de.silentesc.xpbank.commands.ReloadConfigCommand;
import de.silentesc.xpbank.tabcomplete.BankComplete;
import de.silentesc.xpbank.utils.BankData;
import de.silentesc.xpbank.utils.PluginConfig;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

public final class Main extends JavaPlugin {

    private static Main INSTANCE;
    private final String configPath = "config.yaml";
    private PluginConfig pluginConfig;
    private BankData bankData;

    public Main() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        loadConfigs();
        loadCommands();
    }

    // Load config and data from yaml files
    private void loadConfigs() {
        File configFile = new File(String.format("%s/%s", getDataFolder().getPath(), configPath));
        if (!configFile.exists()) {
            saveResource(configPath, false);
        }
        pluginConfig = new PluginConfig();
        bankData = new BankData("bank_data.yaml");
    }

    // Load Commands and it's TabCompleter
    private void loadCommands() {
        Objects.requireNonNull(Bukkit.getPluginCommand("xpbank_reloadcfg")).setExecutor(new ReloadConfigCommand());
        Objects.requireNonNull(Bukkit.getPluginCommand("bank")).setExecutor(new BankCommand());
        Objects.requireNonNull(Bukkit.getPluginCommand("bank")).setTabCompleter(new BankComplete());
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

    public BankData getBankData() {
        return bankData;
    }
}
