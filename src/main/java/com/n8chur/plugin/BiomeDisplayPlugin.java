package com.n8chur.plugin;

import com.hypixel.hytale.common.plugin.PluginIdentifier;
import com.hypixel.hytale.component.ComponentRegistryProxy;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.event.EventPriority;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.plugin.PluginBase;
import com.hypixel.hytale.server.core.plugin.PluginManager;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.Config;
import com.hypixel.hytale.server.worldgen.biome.Biome;
import com.n8chur.plugin.settings.BiomeDisplayConfig;
import com.n8chur.plugin.settings.BiomeDisplayUserSettingsComponent;
import com.n8chur.plugin.ui.BiomeHudManager;

import javax.annotation.Nonnull;

public class BiomeDisplayPlugin extends JavaPlugin {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    private final BiomeHudManager hudManager = new BiomeHudManager();

    private final Config<BiomeDisplayConfig> config = this.withConfig(BiomeDisplayConfig.CODEC);

    public BiomeDisplayPlugin(@Nonnull JavaPluginInit init) {
        super(init);

        LOGGER.atInfo()
            .log("Initializing " + this.getName() + " version " + this.getManifest().getVersion().toString());
    }

    @Override
    protected void start() {
        PluginBase plugin = PluginManager.get().getPlugin(PluginIdentifier.fromString("Buuz135:MultipleHUD"));
        if (plugin != null) {
            try {
                Class.forName("com.buuz135.mhud.MultipleHUD");
                this.hudManager.setMultipleHUDPresent(true);
            } catch (ClassNotFoundException e) {
                LOGGER.atSevere().log("MultipleHUD plugin is loaded but the class cannot be accessed!");
            }
        } else {
            LOGGER.atWarning().log("MultipleHUD plugin is not present which may cause compatability issues with other mods that provide custom HUD elements.");
        }
    }

    @Override
    protected void setup() {
        super.setup();

        ComponentRegistryProxy<EntityStore> entityStoreRegistry = this.getEntityStoreRegistry();

        ComponentType<EntityStore, BiomeDisplayUserSettingsComponent> userSettingsComponentType = entityStoreRegistry
            .registerComponent(
                BiomeDisplayUserSettingsComponent.class,
                "BiomeDisplayUserSettings",
                BiomeDisplayUserSettingsComponent.CODEC
            );

        entityStoreRegistry.registerSystem(new BiomeDisplayHudUpdateSystem(userSettingsComponentType, hudManager));
        this.getCommandRegistry().registerCommand(new BiomeDisplayCommand(userSettingsComponentType));

        getEventRegistry().registerGlobal(
            EventPriority.EARLY,
            PlayerReadyEvent.class,
            event -> {
                Player player = event.getPlayer();
                World world = player.getWorld();
                if (world == null) return;

                world.execute(() -> {
                    Ref<EntityStore> ref = player.getReference();
                    if (ref == null) return;

                    Store<EntityStore> store = ref.getStore();
                    BiomeDisplayUserSettingsComponent settings = store.getComponent(ref, userSettingsComponentType);
                    if (settings == null) {
                        settings = store.ensureAndGetComponent(ref, userSettingsComponentType);
                        BiomeDisplayConfig cfg = config.get();
                        settings.setDefaults(cfg);
                    }
                });
            }
        );

        getEventRegistry().registerGlobal(
            EventPriority.NORMAL,
            PlayerDisconnectEvent.class,
            event -> {
                hudManager.onPlayerLeave(event.getPlayerRef());
            }
        );
    }
}