package de.silentesc.xpbank.utils;

import de.silentesc.xpbank.Main;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BankData {
    private FileConfig bankDataConfig;
    private final HashMap<UUID, Integer> data;

    public BankData() {
        data = new HashMap<>();
        loadConfig();
    }

    public void loadConfig() {
        bankDataConfig = new FileConfig(Main.getINSTANCE().getBankDataPath());
        for (Map.Entry<String, Object> entry : bankDataConfig.getValues(false).entrySet()) {
            try {
                data.put(UUID.fromString(entry.getKey()), (Integer) entry.getValue());
            }
            catch (ClassCastException ignored) {
                Main.getINSTANCE().getLogger().severe(String.format("Some values inside the %s cannot be loaded. Confirm the correctness of the file to fix this.", Main.getINSTANCE().getBankDataPath()));
            }
        }
    }

    public void saveBankData() {
        for (Map.Entry<UUID, Integer> entry : data.entrySet()) {
            bankDataConfig.set(entry.getKey().toString(), entry.getValue());
        }
        bankDataConfig.saveConfig();
    }

    /**
     * Getters
     */

    public HashMap<UUID, Integer> getData() {
        return data;
    }
}
