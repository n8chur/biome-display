package com.n8chur.plugin.ui;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BiomeHud extends CustomUIHud {

    public record BiomeInfo(@Nonnull Message biomeName, @Nonnull Message regionName, @Nonnull Message zoneName, @Nonnull Message tierName) {
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

    @Nullable
    public BiomeInfo getBiomeInfo() {
        return biomeInfo;
    }
}