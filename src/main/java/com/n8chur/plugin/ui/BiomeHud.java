package com.n8chur.plugin.ui;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BiomeHud extends CustomUIHud {

    public static class BiomeInfo {

        @Nonnull
        public Message biomeName;

        @Nonnull
        public Message regionName;

        @Nonnull
        public Message zoneName;

        @Nonnull
        public Message tierName;

        public BiomeInfo(@Nonnull Message biomeName, @Nonnull Message regionName, @Nonnull Message zoneName, @Nonnull Message tierName) {
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
        if (biomeInfo == null) return;

        ui.append("Hud/Biome/BiomeHud.ui");

        ui.set("#TierLabel.TextSpans", biomeInfo.tierName);
        ui.set("#BiomeLabel.TextSpans", biomeInfo.biomeName);
        ui.set("#RegionLabel.TextSpans", Message.join(biomeInfo.regionName, Message.raw(", "), biomeInfo.zoneName).bold(true));
    }

    public void updateHud(BiomeInfo biomeInfo) {
        this.biomeInfo = biomeInfo;
    }
}