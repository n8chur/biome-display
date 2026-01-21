package com.n8chur.plugin.ui;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.modules.i18n.I18nModule;
import com.hypixel.hytale.server.core.ui.Anchor;
import com.hypixel.hytale.server.core.ui.Value;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.n8chur.plugin.settings.BiomeDisplayUserSettingsComponent;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class BiomeHud extends CustomUIHud {

    @Nullable
    private BiomeInfo biomeInfo;
    @Nonnull
    private BiomeDisplayUserSettingsComponent.HudPosition position = BiomeDisplayUserSettingsComponent.HudPosition.DEFAULT;
    @Nonnull
    private BiomeDisplayUserSettingsComponent.HudSize size = BiomeDisplayUserSettingsComponent.HudSize.MEDIUM;

    private static final Value<String> TIER_LABEL_SMALL_STYLE = Value.ref("Hud/Biome/BiomeHud.ui", "TierLabelSmallStyle");
    private static final Value<String> TIER_LABEL_MEDIUM_STYLE = Value.ref("Hud/Biome/BiomeHud.ui", "TierLabelMediumStyle");
    private static final Value<String> TIER_LABEL_LARGE_STYLE = Value.ref("Hud/Biome/BiomeHud.ui", "TierLabelLargeStyle");

    private static final Value<String> BIOME_LABEL_SMALL_STYLE = Value.ref("Hud/Biome/BiomeHud.ui", "BiomeLabelSmallStyle");
    private static final Value<String> BIOME_LABEL_MEDIUM_STYLE = Value.ref("Hud/Biome/BiomeHud.ui", "BiomeLabelMediumStyle");
    private static final Value<String> BIOME_LABEL_LARGE_STYLE = Value.ref("Hud/Biome/BiomeHud.ui", "BiomeLabelLargeStyle");

    private static final Value<String> REGION_LABEL_SMALL_STYLE = Value.ref("Hud/Biome/BiomeHud.ui", "RegionLabelSmallStyle");
    private static final Value<String> REGION_LABEL_MEDIUM_STYLE = Value.ref("Hud/Biome/BiomeHud.ui", "RegionLabelMediumStyle");
    private static final Value<String> REGION_LABEL_LARGE_STYLE = Value.ref("Hud/Biome/BiomeHud.ui", "RegionLabelLargeStyle");

    private static final Value<Integer> ANCHOR_HEIGHT_SMALL = Value.ref("Hud/Biome/BiomeHud.ui", "AnchorHeightSmall");
    private static final Value<Integer> ANCHOR_HEIGHT_MEDIUM = Value.ref("Hud/Biome/BiomeHud.ui", "AnchorHeightMedium");
    private static final Value<Integer> ANCHOR_HEIGHT_LARGE = Value.ref("Hud/Biome/BiomeHud.ui", "AnchorHeightLarge");

    private static final Value<Integer> ANCHOR_PADDING = Value.ref("Hud/Biome/BiomeHud.ui", "AnchorPadding");

    private boolean isBuilt = false;

    public BiomeHud(@Nonnull PlayerRef playerRef) {
        super(playerRef);
    }

    public void update() {
        this.update(false, new UICommandBuilder());
    }

    public void clear() {
        super.update(true, new UICommandBuilder());

        isBuilt = false;
    }

    @Override
    protected void build(@Nonnull UICommandBuilder ui) {
        ui.append("Hud/Biome/BiomeHud.ui");

        isBuilt = true;

        // While this doesn't seem like it should be required, it seems to resolve an issue with compatability with
        // EyeSpy when network latency is high.
        _update(ui);
    }

    @Override
    public void update(boolean clear, @NonNullDecl UICommandBuilder ui) {
        if (!isBuilt) { return; }

        _update(ui);

        super.update(clear, ui);
    }

    private void _update(@NonNullDecl UICommandBuilder ui) {
        if (this.biomeInfo == null) return;

        ui.set("#TierLabel.Style", getTierLabelStyle());
        ui.set("#TierLabel.TextSpans", this.biomeInfo.getTierName());

        ui.set("#BiomeLabel.Style", getBiomeLabelStyle());
        ui.set("#BiomeLabel.TextSpans", this.biomeInfo.getBiomeName());

        ui.set("#RegionLabel.Style", getRegionLabelStyle());
        ui.set(
            "#RegionLabel.TextSpans",
            Message.join(this.biomeInfo.getRegionName(), Message.raw(", "), this.biomeInfo.getZoneName())
        );

        setPosition(this.position, ui);
    }

    private void setPosition(
        @Nonnull BiomeDisplayUserSettingsComponent.HudPosition position,
        @Nonnull UICommandBuilder ui
    ) {
        Anchor anchor = new Anchor();
        anchor.setHeight(getAnchorHeight());

        switch (position.vertical) {
            case TOP:
                anchor.setTop(ANCHOR_PADDING);
                ui.set("#BiomeHud.LayoutMode", "Top");
                break;
            case MIDDLE:
                ui.set("#BiomeHud.LayoutMode", "Center");
                break;
            case BOTTOM:
                anchor.setBottom(ANCHOR_PADDING);
                ui.set("#BiomeHud.LayoutMode", "Bottom");
                break;
        }

        switch (position.horizontal) {
            case LEFT:
                anchor.setLeft(ANCHOR_PADDING);
                ui.set("#BiomeHud.LayoutMode", "Left");
                break;
            case CENTER:
                ui.set("#BiomeHud.LayoutMode", "Center");
                break;
            case RIGHT:
                anchor.setRight(ANCHOR_PADDING);
                ui.set("#BiomeHud.LayoutMode", "Right");
                break;
        }

        ui.setObject("#BiomeHud.Anchor", anchor);
    }

    private Value<Integer> getAnchorHeight() {
        return switch (this.size) {
            case SMALL -> ANCHOR_HEIGHT_SMALL;
            case MEDIUM -> ANCHOR_HEIGHT_MEDIUM;
            case LARGE -> ANCHOR_HEIGHT_LARGE;
        };
    }

    private Value<String> getTierLabelStyle() {
        return switch (this.size) {
            case SMALL -> TIER_LABEL_SMALL_STYLE;
            case MEDIUM -> TIER_LABEL_MEDIUM_STYLE;
            case LARGE -> TIER_LABEL_LARGE_STYLE;
        };
    }

    private Value<String> getBiomeLabelStyle() {
        return switch (this.size) {
            case SMALL -> BIOME_LABEL_SMALL_STYLE;
            case MEDIUM -> BIOME_LABEL_MEDIUM_STYLE;
            case LARGE -> BIOME_LABEL_LARGE_STYLE;
        };
    }

    private Value<String> getRegionLabelStyle() {
        return switch (this.size) {
            case SMALL -> REGION_LABEL_SMALL_STYLE;
            case MEDIUM -> REGION_LABEL_MEDIUM_STYLE;
            case LARGE -> REGION_LABEL_LARGE_STYLE;
        };
    }

    public void updateBiomeInfo(BiomeInfo biomeInfo) {
        this.biomeInfo = biomeInfo;
    }

    public void updatePosition(BiomeDisplayUserSettingsComponent.HudPosition position) {
        this.position = position;
    }

    public void updateSize(BiomeDisplayUserSettingsComponent.HudSize size) {
        this.size = size;
    }

    @Nullable
    public BiomeInfo getBiomeInfo() {
        return biomeInfo;
    }

    @Nonnull
    public BiomeDisplayUserSettingsComponent.HudPosition getPosition() {
        return position;
    }

    @Nonnull
    public BiomeDisplayUserSettingsComponent.HudSize getSize() {
        return size;
    }

    public static class BiomeInfo {

        private final @Nonnull String biomeNameKey;
        private final @Nonnull String regionNameKey;
        private final @Nonnull String zoneNameKey;
        private final @Nonnull String tierNameKey;

        public BiomeInfo(
            @Nonnull String biomeNameKey,
            @Nonnull String regionNameKey,
            @Nonnull String zoneNameKey,
            @Nonnull String tierNameKey
        ) {
            this.biomeNameKey = biomeNameKey;
            this.regionNameKey = regionNameKey;
            this.zoneNameKey = zoneNameKey;
            this.tierNameKey = tierNameKey;
        }

        @Nonnull
        public Message getRegionName() { return getMessage("server.map.region.%s", regionNameKey);}

        @Nonnull
        public Message getZoneName() { return getMessage("server.map.zone.%s", zoneNameKey); }

        @Nonnull
        public Message getTierName() { return getMessage("server.map.tier.%s", tierNameKey); }

        @Nonnull
        public Message getBiomeName() { return Message.raw(biomeNameKey); }

        public Message getMessage(String formatString, String key) {
            String resolvedKey = String.format(formatString, key);
            if (I18nModule.get().getMessage("en-US", resolvedKey) == null) {
                return Message.raw(key);
            }

            return Message.translation(resolvedKey);
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            BiomeInfo biomeInfo = (BiomeInfo) o;
            return Objects.equals(
                biomeNameKey,
                biomeInfo.biomeNameKey
            ) && Objects.equals(
                regionNameKey,
                biomeInfo.regionNameKey
            ) && Objects.equals(
                zoneNameKey,
                biomeInfo.zoneNameKey
            ) && Objects.equals(
                tierNameKey,
                biomeInfo.tierNameKey
            );
        }

        @Override
        public int hashCode() {
            return Objects.hash(biomeNameKey, regionNameKey, zoneNameKey, tierNameKey);
        }
    }
}
