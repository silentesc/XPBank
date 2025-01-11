package de.silentesc.xpbank.tabcomplete;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BankComplete implements TabCompleter {
    private final List<String> arguments = Arrays.asList("balance", "deposit", "withdraw", "transfer");

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return new ArrayList<>();
        }

        List<String> result = new ArrayList<>();

        if (args.length == 1) {
            for (String arg : arguments) {
                if (arg.toLowerCase().contains(args[0].toLowerCase())) {
                    result.add(arg);
                }
            }
        }
        else if (args.length == 3 && args[0].equalsIgnoreCase("transfer")) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!p.getDisplayName().equals(player.getDisplayName())) {
                    result.add(p.getDisplayName());
                }
            }
        }

        return result;
    }
}