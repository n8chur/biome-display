package com.n8chur.plugin.ui;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.ui.Anchor;
import com.hypixel.hytale.server.core.ui.Value;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.n8chur.plugin.settings.BiomeDisplayUserSettingsComponent;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BiomeHud extends CustomUIHud {

    public record BiomeInfo(@Nonnull Message biomeName, @Nonnull Message regionName, @Nonnull Message zoneName, @Nonnull Message tierName) {
    }

    @Nullable
    private BiomeInfo biomeInfo;
    @Nonnull
    private BiomeDisplayUserSettingsComponent.HudPosition position = BiomeDisplayUserSettingsComponent.HudPosition.DEFAULT;

    public BiomeHud(@Nonnull PlayerRef playerRef) {
        super(playerRef);
    }

    public void update() {
        this.update(false, new UICommandBuilder());
    }

    public void clear() {
        this.update(true, new UICommandBuilder());
    }

    @Override
    protected void build(@Nonnull UICommandBuilder ui) {
        ui.append("Hud/Biome/BiomeHud.ui");
    }

    @Override
    public void update(boolean clear, @NonNullDecl UICommandBuilder ui) {
        if (this.biomeInfo != null) {
            ui.set("#TierLabel.TextSpans", this.biomeInfo.tierName);
            ui.set("#BiomeLabel.TextSpans", this.biomeInfo.biomeName);
            ui.set("#RegionLabel.TextSpans", Message.join(this.biomeInfo.regionName, Message.raw(", "), this.biomeInfo.zoneName).bold(true));

            setPosition(this.position, ui);
        }

        super.update(clear, ui);
    }

    private void setPosition(@Nonnull BiomeDisplayUserSettingsComponent.HudPosition position, @Nonnull UICommandBuilder ui) {
        Anchor anchor = new Anchor();
        anchor.setHeight(Value.of(128));

        switch (position.vertical) {
            case TOP:
                anchor.setTop(Value.of(20));
                ui.set("#BiomeHud.LayoutMode", "Top");
                break;
            case MIDDLE:
                ui.set("#BiomeHud.LayoutMode", "Center");
                break;
            case BOTTOM:
                anchor.setBottom(Value.of(20));
                ui.set("#BiomeHud.LayoutMode", "Bottom");
                break;
        }

        switch (position.horizontal) {
            case LEFT:
                anchor.setLeft(Value.of(20));
                ui.set("#BiomeHud.LayoutMode", "Left");
                break;
            case CENTER:
                ui.set("#BiomeHud.LayoutMode", "Center");
                break;
            case RIGHT:
                anchor.setRight(Value.of(20));
                ui.set("#BiomeHud.LayoutMode", "Right");
                break;
        }

        ui.setObject("#BiomeHud.Anchor", anchor);
    }

    public void updateBiomeInfo(BiomeInfo biomeInfo) {
        this.biomeInfo = biomeInfo;
    }

    public void updatePosition(BiomeDisplayUserSettingsComponent.HudPosition position) {
        this.position = position;
    }

    @Nullable
    public BiomeInfo getBiomeInfo() {
        return biomeInfo;
    }

    @Nonnull
    public BiomeDisplayUserSettingsComponent.HudPosition getPosition() {
        return position;
    }
}
