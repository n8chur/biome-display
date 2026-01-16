package com.n8chur.plugin;

import com.hypixel.hytale.common.plugin.PluginIdentifier;
import com.hypixel.hytale.component.*;
import com.hypixel.hytale.event.EventPriority;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.plugin.PluginBase;
import com.hypixel.hytale.server.core.plugin.PluginManager;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.n8chur.plugin.settings.BiomeDisplayUserSettingsComponent;
import com.n8chur.plugin.ui.BiomeHudProvider;

import javax.annotation.Nonnull;

public class BiomeDisplayPlugin extends JavaPlugin {

    public static boolean isMultipleHUDPresent() { return IS_MULTIPLEHUD_PRESENT; }

    private static boolean IS_MULTIPLEHUD_PRESENT = false;

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    private final BiomeDisplayUserSettingsComponent userSettingsStore = new BiomeDisplayUserSettingsComponent();

    private final BiomeHudProvider hudProvider = new BiomeHudProvider();

    public BiomeDisplayPlugin(@Nonnull JavaPluginInit init) {
        super(init);
        LOGGER.atInfo().log("Initializing " + this.getName() + " version " + this.getManifest().getVersion().toString());
    }

    @Override
    protected void start() {
        PluginBase plugin = PluginManager.get().getPlugin(PluginIdentifier.fromString("Buuz135:MultipleHUD"));
        if (plugin != null) {
            BiomeDisplayPlugin.IS_MULTIPLEHUD_PRESENT = true;
        }
    }

    @Override
    protected void setup() {
        super.setup();

        ComponentRegistryProxy<EntityStore> entityStoreRegistry = this.getEntityStoreRegistry();

        ComponentType<EntityStore, BiomeDisplayUserSettingsComponent> userSettingsComponentType = entityStoreRegistry.registerComponent(
            BiomeDisplayUserSettingsComponent.class,
            "BiomeDisplayUserSettings",
            BiomeDisplayUserSettingsComponent.CODEC
        );

        entityStoreRegistry.registerSystem(new BiomeDisplayHudUpdateSystem(userSettingsComponentType, hudProvider));
        this.getCommandRegistry().registerCommand(new BiomeDisplayCommand(userSettingsComponentType));

        getEventRegistry().registerGlobal(
            EventPriority.EARLY,
            PlayerReadyEvent.class,
            event -> {
                Ref<EntityStore> ref = event.getPlayer().getReference();
                if (ref == null) return;

                Store<EntityStore> store = event.getPlayer().getReference().getStore();
                store.ensureAndGetComponent(ref, userSettingsComponentType);
            }
        );
    }
}