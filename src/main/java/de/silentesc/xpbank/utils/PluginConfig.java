package de.silentesc.xpbank.utils;

import de.silentesc.xpbank.Main;

public class PluginConfig {
    private String prefix;
    private int depositFeeFlat;
    private int depositFeePercentage;
    private int withdrawFeeFlat;
    private int withdrawFeePercentage;
    private int transferFeeFlat;
    private int transferFeePercentage;

    public PluginConfig() {
        loadConfig();
    }

    // Load everything from file into variables
    public void loadConfig() {
        FileConfig config = new FileConfig(Main.getINSTANCE().getConfigPath());
        prefix = config.getString("prefix") == null ? "" : config.getString("prefix");
        depositFeeFlat = config.getInt("fees.deposit.flat");
        depositFeePercentage = config.getInt("fees.deposit.percentage");
        withdrawFeeFlat = config.getInt("fees.withdraw.flat");
        withdrawFeePercentage = config.getInt("fees.withdraw.percentage");
        transferFeeFlat = config.getInt("fees.transfer.flat");
        transferFeePercentage = config.getInt("fees.transfer.percentage");
    }

    /**
     * Getters
     */

    public String getPrefix() {
        return prefix;
    }

    public int getDepositFeeFlat() {
        return depositFeeFlat;
    }

    public int getDepositFeePercentage() {
        return depositFeePercentage;
    }

    public int getWithdrawFeeFlat() {
        return withdrawFeeFlat;
    }

    public int getWithdrawFeePercentage() {
        return withdrawFeePercentage;
    }

    public int getTransferFeeFlat() {
        return transferFeeFlat;
    }

    public int getTransferFeePercentage() {
        return transferFeePercentage;
    }
}
