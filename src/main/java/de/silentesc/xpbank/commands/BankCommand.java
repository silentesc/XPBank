package de.silentesc.xpbank.commands;

import de.silentesc.xpbank.Main;
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
                "/bank fees",
                "/bank balance",
                "/bank deposit <amount>",
                "/bank withdraw <amount>",
                "/bank transfer <player> <amount>"
        };

        if (args.length == 0) {
            ShortMessages.wrongUsage(player, usages);
            return true;
        }

        // Execute logic functions depending on the args
        // Also does some checking for the args, every logical check is handled in the logic functions
        switch (args[0].toLowerCase()) {
            case "fees" -> feesLogic(player);
            case "balance" -> balanceLogic(player);
            case "deposit" -> {
                if (handleBadArgsLength(args, 2, player, usages)) return true;
                if (handleStringNotInteger(args[1], player)) return true;
                depositLogic(player, Integer.parseInt(args[1]));
            }
            case "withdraw" -> {
                if (handleBadArgsLength(args, 2, player, usages)) return true;
                if (handleStringNotInteger(args[1], player)) return true;
                withdrawLogic(player, Integer.parseInt(args[1]));
            }
            case "transfer" -> {
                if (handleBadArgsLength(args, 3, player, usages)) return true;
                if (handleStringNotInteger(args[2], player)) return true;
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    ShortMessages.sendMessage(player, "§cPlayer does not exist!");
                    return true;
                }
                transferLogic(player, target, Integer.parseInt(args[2]));
            }
            default -> ShortMessages.wrongUsage(player, usages);
        }

        return true;
    }

    // Sends the current fee settings to the player
    private void feesLogic(Player player) {
        int depositFeeFlat = Main.getINSTANCE().getPluginConfig().getDepositFeeFlat();
        int depositFeePercentage = Main.getINSTANCE().getPluginConfig().getDepositFeePercentage();
        int withdrawFeeFlat = Main.getINSTANCE().getPluginConfig().getWithdrawFeeFlat();
        int withdrawFeePercentage = Main.getINSTANCE().getPluginConfig().getWithdrawFeePercentage();
        int transferFeeFlat = Main.getINSTANCE().getPluginConfig().getTransferFeeFlat();
        int transferFeePercentage = Main.getINSTANCE().getPluginConfig().getTransferFeePercentage();
        ShortMessages.sendMessage(player, String.format("§fDeposit Fees: §7%s flat + %s%%", depositFeeFlat, depositFeePercentage));
        ShortMessages.sendMessage(player, String.format("§fWithdraw Fees: §7%s flat + %s%%", withdrawFeeFlat, withdrawFeePercentage));
        ShortMessages.sendMessage(player, String.format("§fTransfer Fees: §7%s flat + %s%%", transferFeeFlat, transferFeePercentage));
    }

    // Sends the bank balance and xp to the player
    private void balanceLogic(Player player) {
        ShortMessages.sendMessage(player, String.format("§fYour bank-balance is: §a%d", BankUtils.getBalance(player.getUniqueId())));
        ShortMessages.sendMessage(player, String.format("§fXP in your xp-bar: §a%d", XpUtils.getXp(player)));
    }

    // Deposits xp in the bank after performing some logical checks
    // Also considers fees
    private void depositLogic(Player player, int amount) {
        if (XpUtils.getXp(player) < amount) {
            ShortMessages.sendMessage(player, "§cYou don't have enough xp to do that!");
            return;
        }
        if (amount < 1) {
            ShortMessages.sendMessage(player, "§cYou cannot deposit any amount smaller that 1.");
            return;
        }

        int feeFlat = Main.getINSTANCE().getPluginConfig().getDepositFeeFlat();
        int feePercentage = Main.getINSTANCE().getPluginConfig().getDepositFeePercentage();
        int fees = calculateFees(amount, feeFlat, feePercentage);
        amount -= fees;

        if (amount < 1) {
            ShortMessages.sendMessage(player, "§cDepositing declined: The amount - fees would be smaller that 0.");
            return;
        }

        XpUtils.removeXp(player, amount + fees);
        BankUtils.addBalance(player.getUniqueId(), amount);
        ShortMessages.sendMessage(player, String.format("§fYou have deposited §a%d §fxp into your bank! §7(%d fees)", amount, fees));
    }

    // Withdraws balance from the bank after performing some logical checks
    // Also considers fees
    private void withdrawLogic(Player player, int amount) {
        if (BankUtils.getBalance(player.getUniqueId()) < amount) {
            ShortMessages.sendMessage(player, "§cYou don't have enough balance to do that!");
            return;
        }
        if (amount < 1) {
            ShortMessages.sendMessage(player, "§cYou cannot withdraw any amount smaller that 1.");
            return;
        }

        int feeFlat = Main.getINSTANCE().getPluginConfig().getWithdrawFeeFlat();
        int feePercentage = Main.getINSTANCE().getPluginConfig().getWithdrawFeePercentage();
        int fees = calculateFees(amount, feeFlat, feePercentage);
        amount -= fees;

        if (amount < 1) {
            ShortMessages.sendMessage(player, "§cWithdraw declined: The amount - fees would be smaller that 0.");
            return;
        }

        BankUtils.removeBalance(player.getUniqueId(), amount + fees);
        XpUtils.addXp(player, amount);
        ShortMessages.sendMessage(player, String.format("§fYou have withdrawn §a%d §fxp from your bank! §7(%d fees)", amount, fees));
    }

    // Transfers balance to another player after performing some logical checks
    // Also considers fees
    private void transferLogic(Player player, Player target, int amount) {
        if (player == target) {
            ShortMessages.sendMessage(player, "§cYou cannot transfer balance to yourself!");
            return;
        }
        if (BankUtils.getBalance(player.getUniqueId()) < amount) {
            ShortMessages.sendMessage(player, "§cYou don't have enough balance to do that!");
            return;
        }
        if (amount < 1) {
            ShortMessages.sendMessage(player, "§cYou cannot transfer any amount smaller that 1.");
            return;
        }

        int feeFlat = Main.getINSTANCE().getPluginConfig().getTransferFeeFlat();
        int feePercentage = Main.getINSTANCE().getPluginConfig().getTransferFeePercentage();
        int fees = calculateFees(amount, feeFlat, feePercentage);
        amount -= fees;

        if (amount < 1) {
            ShortMessages.sendMessage(player, "§cTransaction declined: The amount - fees would be smaller that 0.");
            return;
        }

        BankUtils.removeBalance(player.getUniqueId(), amount + fees);
        BankUtils.addBalance(target.getUniqueId(), amount);
        ShortMessages.sendMessage(player, String.format("§fYou have transferred a balance of §a%d §fto §e%s§f! §7(%d fees)", amount, target.getDisplayName(), fees));
        ShortMessages.sendMessage(target, String.format("§fYou have received a balance of §a%d §ffrom §e%s§f!", amount, player.getDisplayName()));
    }

    /**
     * Private utils
     */

    // Returns true if args length is not matching, false otherwise
    private boolean handleBadArgsLength(String[] args, int requiredArgsLength, Player player, String[] usages) {
        if (args.length != requiredArgsLength) {
            ShortMessages.wrongUsage(player, usages);
            return true;
        }
        return false;
    }

    // Returns true if string is not integer, false otherwise
    private boolean handleStringNotInteger(String string, Player player) {
        if (!JavaUtils.isStringInteger(string)) {
            ShortMessages.sendMessage(player, "§cThe amount has to be an integer!");
            return true;
        }
        return false;
    }

    private int calculateFees(int amount, int feeFlat, int feePercentage) {
        if (feeFlat > amount) {
            return amount;
        }
        return (int) (feeFlat + ((amount - feeFlat) * (feePercentage / 100.0)));
    }
}
