package com.n8chur.plugin.ui;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.ui.Anchor;
import com.hypixel.hytale.server.core.ui.Value;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.n8chur.plugin.settings.BiomeDisplayUserSettingsComponent;

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

    @Override
    protected void build(@Nonnull UICommandBuilder ui) {
        if (this.biomeInfo == null) return;

        ui.append("Hud/Biome/BiomeHud.ui");

        // Apply position classes or styles based on the setting
        // Assuming the UI file has classes or we can set styles dynamically
        // Since I don't have the UI file content, I'll assume we can set a class on the root element
        // or just leave this as a placeholder for now if the UI file needs to be updated first.
        // For now, I will just set the text.

        // Ideally, we would do something like:
        // ui.set("#RootElement.Classes", "hud-" + position.name().toLowerCase().replace("_", "-"));

        ui.set("#TierLabel.TextSpans", this.biomeInfo.tierName);
        ui.set("#BiomeLabel.TextSpans", this.biomeInfo.biomeName);
        ui.set("#RegionLabel.TextSpans", Message.join(this.biomeInfo.regionName, Message.raw(", "), this.biomeInfo.zoneName).bold(true));

        setPosition(this.position, ui);
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
