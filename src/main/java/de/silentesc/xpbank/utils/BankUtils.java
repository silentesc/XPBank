package de.silentesc.xpbank.utils;

import de.silentesc.xpbank.Main;

import java.util.UUID;

public class BankUtils {
    public static void addBalance(UUID uuid, int balance) {
        BankData bankData = Main.getINSTANCE().getBankData();
        int currentBalance = bankData.getData().get(uuid) == null ? 0 : bankData.getData().get(uuid);
        currentBalance += balance;
        bankData.getData().put(uuid, currentBalance);
        bankData.saveBankData();
    }

    public static void removeBalance(UUID uuid, int balance) {
        BankData bankData = Main.getINSTANCE().getBankData();
        int currentBalance = bankData.getData().get(uuid) == null ? 0 : bankData.getData().get(uuid);
        currentBalance -= balance;
        bankData.getData().put(uuid, currentBalance);
        bankData.saveBankData();
    }

    public static void setBalance(UUID uuid, int balance) {
        BankData bankData = Main.getINSTANCE().getBankData();
        bankData.getData().put(uuid, balance);
        bankData.saveBankData();
    }

    public static int getBalance(UUID uuid) {
        BankData bankData = Main.getINSTANCE().getBankData();
        return bankData.getData().get(uuid) == null ? 0 : bankData.getData().get(uuid);
    }
}
