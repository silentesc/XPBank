package de.silentesc.xpbank.commands;

import de.silentesc.xpbank.Main;
import de.silentesc.xpbank.utils.ShortMessages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadConfigCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Main.getINSTANCE().getPluginConfig().loadConfig();
        ShortMessages.sendMessage(sender, "Config has been reloaded.");

        return true;
    }
}
