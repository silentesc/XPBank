package de.silentesc.xpbank.tabcomplete;

import de.silentesc.xpbank.utils.BankUtils;
import de.silentesc.xpbank.utils.XpUtils;
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
    private final List<String> arguments = Arrays.asList("fees", "balance", "deposit", "withdraw", "transfer");

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
        else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("deposit")) {
                result.add(String.valueOf(XpUtils.getXp(player)));
                result.add(String.valueOf(XpUtils.getXp(player) / 2));
            }
            else if (args[0].equalsIgnoreCase("withdraw")) {
                result.add(String.valueOf(BankUtils.getBalance(player.getUniqueId())));
                result.add(String.valueOf(BankUtils.getBalance(player.getUniqueId()) / 2));
            }
            else if (args[0].equalsIgnoreCase("transfer")) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (!p.getDisplayName().equals(player.getDisplayName())) {
                        result.add(p.getDisplayName());
                    }
                }
            }
        }
        else if (args.length == 3 && args[0].equalsIgnoreCase("transfer")) {
            result.add(String.valueOf(BankUtils.getBalance(player.getUniqueId())));
            result.add(String.valueOf(BankUtils.getBalance(player.getUniqueId()) / 2));
        }

        return result;
    }
}
