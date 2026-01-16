package com.n8chur.plugin;

import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.protocol.FormattedMessage;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.AddPlayerToWorldEvent;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.WorldMapTracker;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.universe.world.worldgen.IWorldGen;
import com.hypixel.hytale.server.worldgen.biome.Biome;
import com.hypixel.hytale.server.worldgen.chunk.ChunkGenerator;
import com.hypixel.hytale.server.worldgen.chunk.ZoneBiomeResult;
import com.hypixel.hytale.server.worldgen.zone.Zone;
import com.hypixel.hytale.server.worldgen.zone.ZoneDiscoveryConfig;

import javax.annotation.Nonnull;

public class BiomeDisplayPlugin extends JavaPlugin {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public BiomeDisplayPlugin(@Nonnull JavaPluginInit init) {
        super(init);
        LOGGER.atInfo().log("Initializing " + this.getName() + " version " + this.getManifest().getVersion().toString());
    }

    @Override
    protected void setup() {
        this.getEventRegistry().registerGlobal(AddPlayerToWorldEvent.class, event -> {
            World world = event.getWorld();
            Holder<EntityStore> holder = event.getHolder();
            PlayerRef playerRefComponent = holder.getComponent(PlayerRef.getComponentType());

            if (playerRefComponent == null) {
                LOGGER.atSevere().log("Player reference not found.");
                return;
            }

            IWorldGen worldGen = world.getChunkStore().getGenerator();

            if (worldGen instanceof ChunkGenerator generator) {
                Vector3d position = playerRefComponent.getTransform().getPosition();
                int seed = (int)world.getWorldConfig().getSeed();
                int x = (int)position.getX();
                int z = (int)position.getZ();
                ZoneBiomeResult result = generator.getZoneBiomeResultAt(seed, x, z);
                Zone zone = result.getZoneResult().getZone();
                ZoneDiscoveryConfig discoveryConfig = zone.discoveryConfig();

                Biome biome = result.getBiome();
                String biomeName = biome.getName();

                String regionNameKey = String.format("server.map.region.%s", zone.name());
                Message regionMessage = Message.translation(regionNameKey);
                String regionName = regionMessage.getAnsiMessage();

                String zoneNameKey = String.format("server.map.zone.%s", discoveryConfig.zone());
                Message zoneMessage = Message.translation(zoneNameKey);
                String zoneName = zoneMessage.getAnsiMessage();

                LOGGER.atInfo().log("Biome: " + biomeName + " - " + regionName + " - " + zoneName);
            } else {
                LOGGER.atInfo().log("No biome found.");
            }
        });
    }
}