package com.n8chur.plugin.ui;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BiomeHud extends CustomUIHud {

    public static class BiomeInfo {

        @Nonnull
        public String biomeName;

        @Nonnull
        public String regionName;

        @Nonnull
        public String zoneName;

        @Nonnull
        public String tierName;

        public BiomeInfo(@Nonnull String biomeName, @Nonnull String regionName, @Nonnull String zoneName, @Nonnull String tierName) {
            this.biomeName = biomeName;
            this.regionName = regionName;
            this.zoneName = zoneName;
            this.tierName = tierName;
        }
    }

    @Nullable
    private BiomeInfo biomeInfo;

    public BiomeHud(@Nonnull PlayerRef playerRef) {
        super(playerRef);
    }

    @Override
    protected void build(@Nonnull UICommandBuilder ui) {
        ui.append("Hud/Biome/BiomeHud.ui");

        if (biomeInfo == null) {
            ui.set("#Tier.TextSpans", Message.raw("Unknown"));
            ui.set("#BiomeLabel.TextSpans", Message.raw("Unknown"));
            ui.set("#RegionLabel.TextSpans", Message.raw("Unknown"));
            ui.set("#ZoneLabel.TextSpans", Message.raw("Unknown"));
            return;
        }

        // TODO: pass original message through
        ui.set("#TierLabel.TextSpans", Message.raw(biomeInfo.tierName));
        ui.set("#BiomeLabel.TextSpans", Message.raw(biomeInfo.biomeName));
        ui.set("#RegionLabel.TextSpans", Message.raw(biomeInfo.regionName));
        ui.set("#ZoneLabel.TextSpans", Message.raw(biomeInfo.zoneName));
    }

    public void updateHud(BiomeInfo biomeInfo) {
        this.biomeInfo = biomeInfo;
        // TODO
    }
}