package de.silentesc.xpbank.utils;

import de.silentesc.xpbank.Main;
import org.bukkit.command.CommandSender;

public class ShortMessages {
    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(formatMessage(message));
    }

    private static String formatMessage(String message) {
        return String.format("%s%s", Main.getINSTANCE().getPluginConfig().getPrefix(), message);
    }
}
