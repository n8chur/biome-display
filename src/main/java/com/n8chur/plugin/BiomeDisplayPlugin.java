package com.n8chur.plugin;

import com.hypixel.hytale.common.plugin.PluginIdentifier;
import com.hypixel.hytale.component.*;
import com.hypixel.hytale.event.EventPriority;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.plugin.PluginBase;
import com.hypixel.hytale.server.core.plugin.PluginManager;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.n8chur.plugin.settings.BiomeDisplayUserSettingsComponent;
import com.n8chur.plugin.ui.BiomeHudManager;

import javax.annotation.Nonnull;

public class BiomeDisplayPlugin extends JavaPlugin {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    private final BiomeHudManager hudProvider = new BiomeHudManager();

    public BiomeDisplayPlugin(@Nonnull JavaPluginInit init) {
        super(init);
        LOGGER.atInfo().log("Initializing " + this.getName() + " version " + this.getManifest().getVersion().toString());
    }

    @Override
    protected void start() {
        PluginBase plugin = PluginManager.get().getPlugin(PluginIdentifier.fromString("Buuz135:MultipleHUD"));
        if (plugin != null) {
            try {
                Class.forName("com.buuz135.mhud.MultipleHUD");
                hudProvider.setMultipleHUDPresent(true);
                LOGGER.atInfo().log("MultipleHUD found and accessible.");
            } catch (ClassNotFoundException e) {
                LOGGER.atSevere().log("MultipleHUD plugin is loaded but the class cannot be accessed!");
            }
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

                Store<EntityStore> store = ref.getStore();
                store.ensureAndGetComponent(ref, userSettingsComponentType);
            }
        );

        getEventRegistry().registerGlobal(
            EventPriority.NORMAL,
            PlayerDisconnectEvent.class,
            event -> {
                hudProvider.onPlayerLeave(event.getPlayerRef());
            }
        );
    }
}