package de.silentesc.xpbank.utils;

import de.silentesc.xpbank.Main;

public class PluginConfig {
    private String prefix;
    private int depositFees;
    private int withdrawFees;
    private int transferFees;

    public PluginConfig() {
        loadConfig();
    }

    public void loadConfig() {
        FileConfig config = new FileConfig(Main.getINSTANCE().getConfigPath());
        prefix = config.getString("prefix") == null ? "" : config.getString("prefix");
        depositFees = config.getInt("fees.deposit.percentage");
        withdrawFees = config.getInt("fees.withdraw.percentage");
        transferFees = config.getInt("fees.transfer.percentage");
    }

    /**
     * Getters
     */

    public String getPrefix() {
        return prefix;
    }

    public int getDepositFees() {
        return depositFees;
    }

    public int getWithdrawFees() {
        return withdrawFees;
    }

    public int getTransferFees() {
        return transferFees;
    }
}
