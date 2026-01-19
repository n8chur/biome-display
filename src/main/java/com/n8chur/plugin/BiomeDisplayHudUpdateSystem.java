package com.n8chur.plugin;


import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.EntityUtils;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.worldgen.biome.Biome;
import com.hypixel.hytale.server.worldgen.chunk.ChunkGenerator;
import com.hypixel.hytale.server.worldgen.chunk.ZoneBiomeResult;
import com.hypixel.hytale.server.worldgen.zone.Zone;
import com.hypixel.hytale.server.worldgen.zone.ZoneDiscoveryConfig;
import com.n8chur.plugin.settings.BiomeDisplayUserSettingsComponent;
import com.n8chur.plugin.ui.BiomeHud;
import com.n8chur.plugin.ui.BiomeHudManager;

import javax.annotation.Nonnull;

public class BiomeDisplayHudUpdateSystem extends EntityTickingSystem<EntityStore> {

    @Nonnull
    private final ComponentType<EntityStore, BiomeDisplayUserSettingsComponent> userSettingsComponentType;

    @Nonnull
    private final BiomeHudManager hudManager;

    @Nonnull
    private final Query<EntityStore> query;

    public BiomeDisplayHudUpdateSystem(
        @Nonnull ComponentType<EntityStore, BiomeDisplayUserSettingsComponent> userSettingsComponentType,
        @Nonnull BiomeHudManager hudManager
    ) {
        this.userSettingsComponentType = userSettingsComponentType;
        this.hudManager = hudManager;
        this.query = Query.and(Player.getComponentType(), PlayerRef.getComponentType());
    }

    @Override
    public void tick(
        float dt, int index, @Nonnull ArchetypeChunk<EntityStore> archetypeChunk,
        @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer
    ) {
        final Holder<EntityStore> holder = EntityUtils.toHolder(index, archetypeChunk);

        // Get player and return if not found
        Player player = holder.getComponent(Player.getComponentType());
        if (player == null) return;

        // Get player's reference and return if not found
        PlayerRef playerRef = holder.getComponent(PlayerRef.getComponentType());
        if (playerRef == null) return;

        Ref<EntityStore> entityRef = archetypeChunk.getReferenceTo(index);

        BiomeDisplayUserSettingsComponent settings = store.getComponent(entityRef, userSettingsComponentType);
        if (settings == null) return;

        if (!settings.getIsEnabled()) {
            hideBiomeHud(player, playerRef);
            return;
        }

        // Get world and hide hud and return if not found
        World world = player.getWorld();
        if (world == null) {
            hideBiomeHud(player, playerRef);
            return;
        }

        // Get world generator and hide hud if not an instance of ChunkGenerator
        if (!(world.getChunkStore().getGenerator() instanceof ChunkGenerator generator)) {
            hideBiomeHud(player, playerRef);
            return;
        }

        // Update biome hud
        updateBiomeHud(generator, player, playerRef, world, settings);
    }

    private void hideBiomeHud(@Nonnull Player player, @Nonnull PlayerRef playerRef) {
        this.hudManager.hideHud(player, playerRef);
    }

    private void updateBiomeHud(
        ChunkGenerator generator,
        Player player,
        PlayerRef playerRef,
        World world,
        BiomeDisplayUserSettingsComponent settings
    ) {
        Vector3d position = playerRef.getTransform().getPosition();
        int seed = (int) world.getWorldConfig().getSeed();
        int x = (int) position.getX();
        int z = (int) position.getZ();
        ZoneBiomeResult result = generator.getZoneBiomeResultAt(seed, x, z);

        Biome biome = result.getBiome();
        Message biomeName = Message.raw(biome.getName());

        Zone zone = result.getZoneResult().getZone();
        Message regionName = getRegionName(zone);

        ZoneDiscoveryConfig discoveryConfig = zone.discoveryConfig();
        Message zoneName = getZoneName(discoveryConfig);

        Message tierName = getTierName(zone);

        BiomeHud.BiomeInfo biomeInfo = new BiomeHud.BiomeInfo(biomeName, regionName, zoneName, tierName);
        this.hudManager.updateHud(player, playerRef, biomeInfo, settings.getPosition());
    }

    private Message getRegionName(Zone zone) {
        String regionNameKey = String.format("server.map.region.%s", zone.name());
        return Message.translation(regionNameKey);
    }

    private Message getZoneName(ZoneDiscoveryConfig discoveryConfig) {
        String zoneNameKey = String.format("server.map.zone.%s", discoveryConfig.zone());
        return Message.translation(zoneNameKey);
    }

    private Message getTierName(Zone zone) {
        String regionNameKey = String.format("server.map.tier.%s", zone.name());
        return Message.translation(regionNameKey);
    }

    @Nonnull
    @Override
    public Query<EntityStore> getQuery() {
        return query;
    }
}
