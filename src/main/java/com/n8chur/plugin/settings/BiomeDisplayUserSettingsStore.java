package com.n8chur.plugin.settings;

import com.hypixel.hytale.server.core.universe.PlayerRef;

import java.util.HashMap;
import java.util.Map;

public class BiomeDisplayUserSettingsStore {

    private final Map<PlayerRef, BiomeDisplayUserSettings> store = new HashMap<>();

    public void setIsEnabled(PlayerRef playerRef, boolean isEnabled) {
        BiomeDisplayUserSettings settings = getOrCreateUserSettings(playerRef);
        settings.isEnabled = isEnabled;
    }

    public boolean getIsEnabled(PlayerRef playerRef) {
        BiomeDisplayUserSettings settings = getOrCreateUserSettings(playerRef);
        return settings.isEnabled;
    }

    private BiomeDisplayUserSettings getOrCreateUserSettings(PlayerRef playerRef) {
        if (!this.store.containsKey(playerRef)) {
            BiomeDisplayUserSettings userSettings = new BiomeDisplayUserSettings();
            this.store.put(playerRef, userSettings);
            return userSettings;
        }

        return this.store.get(playerRef);
    }
}
