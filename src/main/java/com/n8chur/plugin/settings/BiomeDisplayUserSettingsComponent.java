package com.n8chur.plugin.settings;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class BiomeDisplayUserSettingsComponent implements Component<EntityStore> {

    public static final BuilderCodec<BiomeDisplayUserSettingsComponent> CODEC =
        BuilderCodec.builder(BiomeDisplayUserSettingsComponent.class, BiomeDisplayUserSettingsComponent::new)
            .append(
                new KeyedCodec<>("BiomeDisplayEnabled", Codec.BOOLEAN),
                (c, v) -> c.enabled = v, c -> c.enabled
            )
            .add()
            .append(
                new KeyedCodec<>("BiomeDisplayPositionVertical", Codec.STRING),
                (c, v) -> c.position.vertical = HudPosition.Vertical.valueOf(v), c -> c.position.vertical.toString()
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
                (c, v) -> c.size = HudSize.valueOf(v),
                c -> c.size.toString()
            )
            .add()
            .build();

    private boolean enabled = true;
    private HudPosition position = HudPosition.DEFAULT;
    private HudSize size = HudSize.MEDIUM;

    public BiomeDisplayUserSettingsComponent() {
    }

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

    @Override
    public Component<EntityStore> clone() {
        BiomeDisplayUserSettingsComponent copy = new BiomeDisplayUserSettingsComponent();
        copy.enabled = this.enabled;
        copy.position = new HudPosition(this.position.vertical, this.position.horizontal);
        copy.size = this.size;
        return copy;
    }

    public static class HudPosition {

        public static final HudPosition DEFAULT = new HudPosition(Vertical.BOTTOM, Horizontal.LEFT);
        public Vertical vertical;
        public Horizontal horizontal;

        public HudPosition(Vertical vertical, Horizontal horizontal) {
            this.vertical = vertical;
            this.horizontal = horizontal;
        }

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
            BOTTOM
        }

        public enum Horizontal {
            LEFT,
            CENTER,
            RIGHT
        }
    }

    public enum HudSize {
        SMALL,
        MEDIUM,
        LARGE
    }
}
