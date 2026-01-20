package com.n8chur.plugin.settings;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class BiomeDisplayUserSettingsComponent implements Component<EntityStore> {

    public static boolean DEFAULT_IS_HIDDEN = false;

    private boolean enabled = !DEFAULT_IS_HIDDEN;
    private HudPosition position = HudPosition.DEFAULT;
    private HudSize size = HudSize.DEFAULT;

    public BiomeDisplayUserSettingsComponent() {}

    public boolean getIsEnabled() {
        return this.enabled;
    }

    public void toggleEnabled() { this.enabled = !enabled;  }

    public HudPosition getPosition() { return this.position; }

    public void setPosition(HudPosition position) {
        this.position = position;
    }

    public HudSize getSize() { return this.size; }

    public void setSize(HudSize size) { this.size = size; }

    public void setDefaults(BiomeDisplayConfig cfg) {
        this.enabled = !cfg.isDefaultHidden();
        this.position = cfg.getDefaultPosition();
        this.size = cfg.getDefaultSize();
    }

    @Override
    public Component<EntityStore> clone() {
        BiomeDisplayUserSettingsComponent copy = new BiomeDisplayUserSettingsComponent();
        copy.enabled = this.enabled;
        copy.position = new HudPosition(this.position.vertical, this.position.horizontal);
        copy.size = this.size;
        return copy;
    }

    // TODO: Migrate to non-prefixed keys (how do I migrate?)
    // TODO: use exising CODEC for position instead of vertical/horizontal manually
    public static final BuilderCodec<BiomeDisplayUserSettingsComponent> CODEC =
        BuilderCodec.builder(BiomeDisplayUserSettingsComponent.class, BiomeDisplayUserSettingsComponent::new)
            .append(
                new KeyedCodec<>("BiomeDisplayEnabled", Codec.BOOLEAN),
                (c, v) -> c.enabled = v,
                c -> c.enabled
            )
            .add()
            .append(
                new KeyedCodec<>("BiomeDisplayPositionVertical", Codec.STRING),
                (c, v) -> c.position.vertical = HudPosition.Vertical.valueOf(v),
                c -> c.position.vertical.toString()
            )
            .add()
            .append(
                new KeyedCodec<>("BiomeDisplayPositionHorizontal", Codec.STRING),
                (c, v) -> c.position.horizontal = HudPosition.Horizontal.valueOf(v),
                c -> c.position.horizontal.toString()
            )
            .add()
            .append(
                new KeyedCodec<>("BiomeDisplaySize", Codec.STRING),
                (c, v) -> c.size = HudSize.valueOf(v.toUpperCase()),
                c -> c.size.toString().toLowerCase()
            )
            .add()
            .build();

    public static class HudPosition {

        public static final HudPosition DEFAULT = new HudPosition(Vertical.DEFAULT, Horizontal.DEFAULT);
        public Vertical vertical = Vertical.DEFAULT;
        public Horizontal horizontal = Horizontal.DEFAULT;

        public HudPosition(Vertical vertical, Horizontal horizontal) {
            this.vertical = vertical;
            this.horizontal = horizontal;
        }

        private HudPosition() {}

        public String toString() {
            return vertical.toString().toLowerCase() + " " + horizontal.toString().toLowerCase();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            HudPosition that = (HudPosition) o;

            if (vertical != that.vertical) return false;
            return horizontal == that.horizontal;
        }

        @Override
        public int hashCode() {
            int result = vertical != null ? vertical.hashCode() : 0;
            result = 31 * result + (horizontal != null ? horizontal.hashCode() : 0);
            return result;
        }

        public enum Vertical {
            TOP,
            MIDDLE,
            BOTTOM;

            public static final Vertical DEFAULT = BOTTOM;
        }

        public enum Horizontal {
            LEFT,
            CENTER,
            RIGHT;

            public static final Horizontal DEFAULT = LEFT;
        }

        public static final BuilderCodec<HudPosition> CODEC = BuilderCodec.builder(HudPosition.class, HudPosition::new)
            .append(
                new KeyedCodec<>("Vertical", Codec.STRING),
                (c, v) -> c.vertical = Vertical.valueOf(v.toUpperCase()),
                c -> c.vertical.toString().toLowerCase()
            )
            .add()
            .append(
                new KeyedCodec<>("Horizontal", Codec.STRING),
                (c, v) -> c.horizontal = Horizontal.valueOf(v.toUpperCase()),
                c -> c.horizontal.toString().toLowerCase()
            )
            .add()
            .build();
    }

    public enum HudSize {
        SMALL,
        MEDIUM,
        LARGE;

        public static final HudSize DEFAULT = MEDIUM;
    }
}
