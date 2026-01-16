package com.n8chur.plugin;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.n8chur.plugin.settings.BiomeDisplayUserSettingsStore;
import com.n8chur.plugin.ui.BiomeHudProvider;

import javax.annotation.Nonnull;

public class BiomeDisplayPlugin extends JavaPlugin {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    private final BiomeDisplayUserSettingsStore userSettingsStore = new BiomeDisplayUserSettingsStore();

    private final BiomeHudProvider hudProvider = new BiomeHudProvider();

    public BiomeDisplayPlugin(@Nonnull JavaPluginInit init) {
        super(init);
        LOGGER.atInfo().log("Initializing " + this.getName() + " version " + this.getManifest().getVersion().toString());
    }

    @Override
    protected void setup() {
        super.setup();

        this.getEntityStoreRegistry().registerSystem(new BiomeDisplayHudUpdateSystem(userSettingsStore, hudProvider));
    }
}