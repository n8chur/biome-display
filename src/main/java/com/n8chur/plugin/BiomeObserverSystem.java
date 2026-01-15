package com.n8chur.plugin;

import com.hypixel.hytale.component.Archetype;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.DelayedEntitySystem;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
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
import javax.annotation.Nullable;

public class BiomeObserverSystem extends DelayedEntitySystem<EntityStore> {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public BiomeObserverSystem() {
        super(1.0F);
    }

    @Override
    public void tick(
            float dt,
            int index,
            @Nonnull ArchetypeChunk<EntityStore> archetypeChunk,
            @Nonnull Store<EntityStore> store,
            @Nonnull CommandBuffer<EntityStore> commandBuffer
    ) {
        World world = commandBuffer.getExternalData().getWorld();
        IWorldGen worldGen = world.getChunkStore().getGenerator();
        Player playerComponent = archetypeChunk.getComponent(index, Player.getComponentType());

        assert playerComponent != null;

        Ref<EntityStore> ref = archetypeChunk.getReferenceTo(index);
        WorldMapTracker worldMapTracker = playerComponent.getWorldMapTracker();
        if (worldGen instanceof ChunkGenerator generator) {
            TransformComponent transformComponent = archetypeChunk.getComponent(index, TransformComponent.getComponentType());

            assert transformComponent != null;

            Vector3d position = transformComponent.getPosition();
            int seed = (int)world.getWorldConfig().getSeed();
            int x = (int)position.getX();
            int z = (int)position.getZ();
            ZoneBiomeResult result = generator.getZoneBiomeResultAt(seed, x, z);
            Biome biome = result.getBiome();
            Zone zone = result.getZoneResult().getZone();
            ZoneDiscoveryConfig discoveryConfig = zone.discoveryConfig();

            String regionNameKey = discoveryConfig.zone();
            String regionName = Message.translation(String.format("server.map.region.%s", regionNameKey)).getFormattedMessage().toString();

            String zoneNameKey = zone.name();
            String zoneName = Message.translation(String.format("server.map.zone.%s", zoneNameKey)).getFormattedMessage().toString();

            LOGGER.atInfo().log("Biome: " + regionName + " - " + zoneName);
        } else {
            LOGGER.atInfo().log("No biome found.");
        }
    }

    @Nullable
    @Override
    public Query<EntityStore> getQuery() {
        return Archetype.of(Player.getComponentType(), TransformComponent.getComponentType());
    }
}