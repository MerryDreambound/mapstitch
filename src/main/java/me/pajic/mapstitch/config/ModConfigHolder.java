package me.pajic.mapstitch.config;

import me.pajic.mapstitch.MapStitch;

public class ModConfigHolder {
    private static ModConfig CONFIG;

    public static ModConfig options() {
        if (CONFIG == null) init();
        return CONFIG;
    }

    public static void init() {
        CONFIG = ModConfig.load(MapStitch.xplat().configDir().resolve("mapstitch.json").toFile());
    }
}
