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
    public void tick(float dt, int index, @Nonnull ArchetypeChunk<EntityStore> archetypeChunk, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        final Holder<EntityStore> holder = EntityUtils.toHolder(index, archetypeChunk);

        final Player player = holder.getComponent(Player.getComponentType());
        if (player == null) return;

        final PlayerRef playerRef = holder.getComponent(PlayerRef.getComponentType());
        if (playerRef == null) return;

        World world = player.getWorld();
        if (world == null) return;

        IWorldGen worldGen = world.getChunkStore().getGenerator();

        if (worldGen instanceof ChunkGenerator generator) {
            Vector3d position = playerRef.getTransform().getPosition();
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
    }

    @Nonnull
    @Override
    public Query<EntityStore> getQuery() {
        return query;
    }
}
