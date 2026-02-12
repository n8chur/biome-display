package com.n8chur.plugin.ui;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.modules.i18n.I18nModule;
import com.hypixel.hytale.server.core.ui.Anchor;
import com.hypixel.hytale.server.core.ui.Value;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.n8chur.plugin.settings.BiomeDisplayUserSettingsComponent;

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
    @Nonnull
    private boolean isTransparent = BiomeDisplayUserSettingsComponent.DEFAULT_IS_TRANSPARENT;

    private static final Value<String> TIER_LABEL_SMALL_STYLE = Value.ref("Hud/BiomeHud.ui", "BDTierLabelSmallStyle");
    private static final Value<String> TIER_LABEL_MEDIUM_STYLE = Value.ref("Hud/BiomeHud.ui", "BDTierLabelMediumStyle");
    private static final Value<String> TIER_LABEL_LARGE_STYLE = Value.ref("Hud/BiomeHud.ui", "BDTierLabelLargeStyle");

    private static final Value<String> BIOME_LABEL_SMALL_STYLE = Value.ref("Hud/BiomeHud.ui", "BDBiomeLabelSmallStyle");
    private static final Value<String> BIOME_LABEL_MEDIUM_STYLE = Value.ref("Hud/BiomeHud.ui", "BDBiomeLabelMediumStyle");
    private static final Value<String> BIOME_LABEL_LARGE_STYLE = Value.ref("Hud/BiomeHud.ui", "BDBiomeLabelLargeStyle");

    private static final Value<String> REGION_LABEL_SMALL_STYLE = Value.ref("Hud/BiomeHud.ui", "BDRegionLabelSmallStyle");
    private static final Value<String> REGION_LABEL_MEDIUM_STYLE = Value.ref("Hud/BiomeHud.ui", "BDRegionLabelMediumStyle");
    private static final Value<String> REGION_LABEL_LARGE_STYLE = Value.ref("Hud/BiomeHud.ui", "BDRegionLabelLargeStyle");

    private static final Value<Integer> ANCHOR_HEIGHT_SMALL = Value.ref("Hud/BiomeHud.ui", "BDAnchorHeightSmall");
    private static final Value<Integer> ANCHOR_HEIGHT_MEDIUM = Value.ref("Hud/BiomeHud.ui", "BDAnchorHeightMedium");
    private static final Value<Integer> ANCHOR_HEIGHT_LARGE = Value.ref("Hud/BiomeHud.ui", "BDAnchorHeightLarge");

    private static final Value<Integer> ANCHOR_PADDING = Value.ref("Hud/BiomeHud.ui", "BDAnchorPadding");


    private static final Value<String> TOOLTIP_BACKGROUND_STYLE = Value.ref("Hud/BiomeHud.ui", "BDTooltipBackground");
    private static final Value<String> TOOLTIP_BACKGROUND_TRANSPARENT_STYLE = Value.ref("Hud/BiomeHud.ui", "BDTooltipBackgroundTransparent");

    // Attempt to work around a crash that seemed to be caused by updates being performed when the UI was not built.
    // A user reported this crash on CurseForge in a comment:
    //    There's a crash sometimes when leaving a server and rejoining. Says "Crash - Selected element in CustomUI
    //    command was not found. Selector: #TierLabel.Style". I can reliably reproduce this on my hosted server with
    //    just biomedisplay enabled. When you rejoin the box isn't showing
    private boolean isBuilt = false;

    public BiomeHud(@Nonnull PlayerRef playerRef) {
        super(playerRef);
    }

    public void update() {
        if (!isBuilt) { return; }

        UICommandBuilder ui = new UICommandBuilder();

        _update(ui);

        update(false, ui);
    }

    public void clear() {
        this.update(true, new UICommandBuilder());

        isBuilt = false;
    }

    @Override
    protected void build(@Nonnull UICommandBuilder ui) {
        ui.append("Hud/BiomeHud.ui");

        isBuilt = true;

        // While this doesn't seem like it should be required, it seems to resolve an issue with compatability with
        // EyeSpy when network latency is high.
        _update(ui);
    }

    private void _update(@Nonnull UICommandBuilder ui) {
        if (this.biomeInfo == null) return;

        ui.set("#BDTierLabel.Style", getTierLabelStyle());
        ui.set("#BDTierLabel.TextSpans", this.biomeInfo.getTierName());

        ui.set("#BDBiomeLabel.Style", getBiomeLabelStyle());
        ui.set("#BDBiomeLabel.TextSpans", this.biomeInfo.getBiomeName());

        ui.set("#BDRegionLabel.Style", getRegionLabelStyle());
        ui.set(
            "#BDRegionLabel.TextSpans",
            Message.join(this.biomeInfo.getRegionName(), Message.raw(", "), this.biomeInfo.getZoneName())
        );

        setSizeAndPosition(this.position, ui);

        ui.set(
            "#BDContent.Background",
            this.isTransparent ? TOOLTIP_BACKGROUND_TRANSPARENT_STYLE : TOOLTIP_BACKGROUND_STYLE
        );
    }

    private void setSizeAndPosition(
        @Nonnull BiomeDisplayUserSettingsComponent.HudPosition position,
        @Nonnull UICommandBuilder ui
    ) {
        Anchor anchor = new Anchor();
        anchor.setHeight(getAnchorHeight());

        switch (position.vertical) {
            case TOP:
                anchor.setTop(ANCHOR_PADDING);
                ui.set("#BDHud.LayoutMode", "Top");
                break;
            case MIDDLE:
                ui.set("#BDHud.LayoutMode", "Center");
                break;
            case BOTTOM:
                anchor.setBottom(ANCHOR_PADDING);
                ui.set("#BDHud.LayoutMode", "Bottom");
                break;
        }

        switch (position.horizontal) {
            case LEFT:
                anchor.setLeft(ANCHOR_PADDING);
                ui.set("#BDHud.LayoutMode", "Left");
                break;
            case CENTER:
                ui.set("#BDHud.LayoutMode", "Center");
                break;
            case RIGHT:
                anchor.setRight(ANCHOR_PADDING);
                ui.set("#BDHud.LayoutMode", "Right");
                break;
        }

        ui.setObject("#BDHud.Anchor", anchor);
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

    public void updateIsTransparent(boolean isTransparent) { this.isTransparent = isTransparent; }

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

    @Nonnull
    public boolean getIsTransparent() { return isTransparent; }

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
