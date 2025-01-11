package de.silentesc.xpbank.commands;

import de.silentesc.xpbank.utils.BankUtils;
import de.silentesc.xpbank.utils.JavaUtils;
import de.silentesc.xpbank.utils.ShortMessages;
import de.silentesc.xpbank.utils.XpUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BankCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            ShortMessages.notAPlayer(sender);
            return true;
        }

        String[] usages = {
                "/bank balance",
                "/bank deposit <amount>",
                "/bank withdraw <amount>",
                "/bank transfer <player> <amount>"
        };

        if (args.length == 0) {
            ShortMessages.sendMessage(player, "Wrong usage! Here are some examples:");
            player.sendMessage(usages);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "balance" -> balanceLogic(player);
            case "deposit" -> {
                if (!checkArgsLength(args, 2, player, usages)) return true;
                if (!checkStringIsInteger(args[1], player)) return true;
                depositLogic(player, Integer.parseInt(args[1]));
            }
            case "withdraw" -> {
                if (!checkArgsLength(args, 2, player, usages)) return true;
                if (!checkStringIsInteger(args[1], player)) return true;
                withdrawLogic(player, Integer.parseInt(args[1]));
            }
            case "transfer" -> {
                if (!checkArgsLength(args, 3, player, usages)) return true;
                if (!checkStringIsInteger(args[2], player)) return true;
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    ShortMessages.sendMessage(player, "Player does not exist!");
                    return true;
                }
                transferLogic(player, target, Integer.parseInt(args[2]));
            }
            default -> {
                ShortMessages.sendMessage(player, "Wrong usage! Here are some examples:");
                player.sendMessage(usages);
            }
        }

        return true;
    }

    private void balanceLogic(Player player) {
        ShortMessages.sendMessage(player, String.format("Your bank-balance is: §a%d", BankUtils.getBalance(player.getUniqueId())));
        ShortMessages.sendMessage(player, String.format("XP in your xp-bar: §a%d", XpUtils.getXp(player)));
    }

    private void depositLogic(Player player, int amount) {
        if (XpUtils.getXp(player) < amount) {
            ShortMessages.sendMessage(player, "You don't have enough xp to do that!");
            return;
        }
        if (amount < 1) {
            ShortMessages.sendMessage(player, "You cannot deposit any amount smaller that 1.");
            return;
        }
        // TODO Deposit Fees
        XpUtils.removeXp(player, amount);
        BankUtils.addBalance(player.getUniqueId(), amount);
        ShortMessages.sendMessage(player, String.format("You have deposited §a%d §7xp into your bank!", amount));
    }

    private void withdrawLogic(Player player, int amount) {
        if (BankUtils.getBalance(player.getUniqueId()) < amount) {
            ShortMessages.sendMessage(player, "You don't have enough balance to do that!");
            return;
        }
        if (amount < 1) {
            ShortMessages.sendMessage(player, "You cannot withdraw any amount smaller that 1.");
            return;
        }
        // TODO Withdraw Fees
        BankUtils.removeBalance(player.getUniqueId(), amount);
        XpUtils.addXp(player, amount);
        ShortMessages.sendMessage(player, String.format("You have withdrawn §a%d §7xp from your bank!", amount));
    }

    private void transferLogic(Player player, Player target, int amount) {
        if (player == target) {
            ShortMessages.sendMessage(player, "You cannot transfer balance to yourself!");
            return;
        }
        if (BankUtils.getBalance(player.getUniqueId()) < amount) {
            ShortMessages.sendMessage(player, "You don't have enough balance to do that!");
            return;
        }
        if (amount < 1) {
            ShortMessages.sendMessage(player, "You cannot transfer any amount smaller that 1.");
            return;
        }
        // TODO Transfer Fees
        BankUtils.removeBalance(player.getUniqueId(), amount);
        BankUtils.addBalance(target.getUniqueId(), amount);
        ShortMessages.sendMessage(player, String.format("You have transferred a balance of §a%d §7to §e%s§7!", amount, target.getDisplayName()));
        ShortMessages.sendMessage(target, String.format("You have received a balance of §a%d §7from §e%s§7!", amount, player.getDisplayName()));
    }

    /**
     * Private utils
     */

    // Returns true if args length is ok, false otherwise
    private boolean checkArgsLength(String[] args, int requiredArgsLength, Player player, String[] usages) {
        if (args.length != requiredArgsLength) {
            ShortMessages.sendMessage(player, "Wrong usage! Here are some examples:");
            player.sendMessage(usages);
            return false;
        }
        return true;
    }

    // Returns true if string is integer, false otherwise
    private boolean checkStringIsInteger(String string, Player player) {
        if (!JavaUtils.isStringInteger(string)) {
            ShortMessages.sendMessage(player, "The amount has to be an integer!");
            return false;
        }
        return true;
    }
}
