package com.n8chur.plugin;


import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.EntityUtils;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.universe.world.worldgen.IWorldGen;
import com.hypixel.hytale.server.worldgen.biome.Biome;
import com.hypixel.hytale.server.worldgen.chunk.ChunkGenerator;
import com.hypixel.hytale.server.worldgen.chunk.ZoneBiomeResult;
import com.hypixel.hytale.server.worldgen.zone.Zone;
import com.hypixel.hytale.server.worldgen.zone.ZoneDiscoveryConfig;

import javax.annotation.Nonnull;

public class BlockUpdateSystem extends EntityTickingSystem<EntityStore> {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    @Nonnull
    private final Query<EntityStore> query;

    public BlockUpdateSystem() {
        this.query = Query.and(Player.getComponentType());
    }

    @Override
    public void tick(float dt, int index, @Nonnull ArchetypeChunk<EntityStore> archetypeChunk,
                     @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        final Holder<EntityStore> holder = EntityUtils.toHolder(index, archetypeChunk);

        // Get player and return if not found
        Player player = holder.getComponent(Player.getComponentType());
        if (player == null) return;

        // Get player's reference and return if not found
        PlayerRef playerRef = holder.getComponent(PlayerRef.getComponentType());
        if (playerRef == null) return;

        // Get world and return if not found
        World world = player.getWorld();
        if (world == null) return;

        // Get world generator and log if not an instance of ChunkGenerator
        if (!(world.getChunkStore().getGenerator() instanceof ChunkGenerator generator)) {
            LOGGER.atInfo().log("No biome found.");
            return;
        }

        // Extract biome information
        logBiomeInfo(generator, playerRef, world);
    }

    private void logBiomeInfo(ChunkGenerator generator, PlayerRef playerRef, World world) {
        Vector3d position = playerRef.getTransform().getPosition();
        int seed = (int) world.getWorldConfig().getSeed();
        int x = (int) position.getX();
        int z = (int) position.getZ();

        // Get the biome result from the generator
        ZoneBiomeResult result = generator.getZoneBiomeResultAt(seed, x, z);
        Zone zone = result.getZoneResult().getZone();
        ZoneDiscoveryConfig discoveryConfig = zone.discoveryConfig();

        Biome biome = result.getBiome();

        String biomeName = biome.getName();
        String regionName = getRegionName(zone);
        String zoneName = getZoneName(discoveryConfig);

        LOGGER.atInfo().log("Biome: " + biomeName + " - " + regionName + " - " + zoneName);
    }

    private String getRegionName(Zone zone) {
        String regionNameKey = String.format("server.map.region.%s", zone.name());
        return Message.translation(regionNameKey).getAnsiMessage();
    }

    private String getZoneName(ZoneDiscoveryConfig discoveryConfig) {
        String zoneNameKey = String.format("server.map.zone.%s", discoveryConfig.zone());
        return Message.translation(zoneNameKey).getAnsiMessage();
    }

    @Nonnull
    @Override
    public Query<EntityStore> getQuery() {
        return query;
    }
}
