package com.example.settings;

public class cssSetting {
    public static boolean getConfig() {
        return readwriteLocal.getAllConfigs().getBoolean("css-dark");
    }

    public static void setConfig(boolean value) {
        readwriteLocal.getAllConfigs().put("css-dark", value);
    }
}
