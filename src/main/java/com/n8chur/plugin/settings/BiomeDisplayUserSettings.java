package com.n8chur.plugin.settings;

import com.hypixel.hytale.server.core.universe.PlayerRef;

import java.util.HashMap;
import java.util.Map;

public class BiomeDisplayUserSettings {

    private static class Settings {

        boolean isEnabled = false;
    }

    private final Map<PlayerRef, Settings> store = new HashMap<>();

    public void setIsEnabled(PlayerRef playerRef, boolean isEnabled) {
        Settings settings = getOrCreateUserSettings(playerRef);
        settings.isEnabled = isEnabled;
    }

    public boolean getIsEnabled(PlayerRef playerRef) {
        Settings settings = getOrCreateUserSettings(playerRef);
        return settings.isEnabled;
    }

    private Settings getOrCreateUserSettings(PlayerRef playerRef) {
        if (!this.store.containsKey(playerRef)) {
            Settings settings = new Settings();
            this.store.put(playerRef, settings);
            return settings;
        }

        return this.store.get(playerRef);
    }
}
