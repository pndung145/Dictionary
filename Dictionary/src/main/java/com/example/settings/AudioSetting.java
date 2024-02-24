package com.example.settings;

public class AudioSetting {
    public static boolean getConfig() {
        return readwriteLocal.getAllConfigs().getBoolean("play-audio-online");
    }

    public static void setConfig() {
        InternetConnect internetConnect = new InternetConnect();
        if (InternetConnect.isInternetAvailable()) {
            readwriteLocal.getAllConfigs().put("play-audio-online", true);
        } else {
            readwriteLocal.getAllConfigs().put("play-audio-online", false);
        }
    }
}
