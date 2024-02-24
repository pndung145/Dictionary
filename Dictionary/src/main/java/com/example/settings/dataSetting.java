package com.example.settings;

public class dataSetting {
    public static boolean getConfig() {
        return readwriteLocal.getAllConfigs().getBoolean("database-online");
    }

    public static void setConfig(boolean value) {
        readwriteLocal.getAllConfigs().put("database-online", value);
    }
}
