package com.n8chur.plugin.settings;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.math.vector.Vector2i;

public class BiomeDisplayConfig {

    public static final String FILENAME = "config";

    public boolean isDefaultHidden() { return defaultHidden; }

    public BiomeDisplayUserSettingsComponent.HudPosition getDefaultPosition() { return defaultPosition; }

    public Vector2i getDefaultOffset() { return defaultOffset; }

    public BiomeDisplayUserSettingsComponent.HudSize getDefaultSize() { return defaultSize; }

    public boolean getDefaultIsTransparent() { return defaultIsTransparent; }

    private boolean defaultHidden = BiomeDisplayUserSettingsComponent.DEFAULT_IS_HIDDEN;
    private BiomeDisplayUserSettingsComponent.HudPosition defaultPosition = BiomeDisplayUserSettingsComponent.HudPosition.DEFAULT;
    private Vector2i defaultOffset = new Vector2i(BiomeDisplayUserSettingsComponent.DEFAULT_OFFSET);
    private BiomeDisplayUserSettingsComponent.HudSize defaultSize = BiomeDisplayUserSettingsComponent.HudSize.DEFAULT;
    private boolean defaultIsTransparent = BiomeDisplayUserSettingsComponent.DEFAULT_IS_TRANSPARENT;

    public static final BuilderCodec<BiomeDisplayConfig> CODEC =
        BuilderCodec.builder(BiomeDisplayConfig.class, BiomeDisplayConfig::new)
            .append(
                new KeyedCodec<>("DefaultHidden", Codec.BOOLEAN),
                (c, v) -> c.defaultHidden = v,
                c -> c.defaultHidden
            )
            .add()
            .append(
                new KeyedCodec<>("DefaultPosition", BiomeDisplayUserSettingsComponent.HudPosition.CODEC),
                (c, v) -> c.defaultPosition = v,
                c -> c.defaultPosition
            )
            .add()
            .append(
                new KeyedCodec<>("DefaultOffset", Vector2i.CODEC),
                (c, v) -> c.defaultOffset = v,
                c -> c.defaultOffset
            )
            .add()
            .append(
                new KeyedCodec<>("DefaultSize", Codec.STRING),
                (c, v) -> c.defaultSize = BiomeDisplayUserSettingsComponent.HudSize.valueOf(v.toUpperCase()),
                c -> c.defaultSize.toString().toLowerCase()
            )
            .add()
            .append(
                new KeyedCodec<>("DefaultIsTransparent", Codec.BOOLEAN),
                (c, v) -> c.defaultIsTransparent = v,
                c -> c.defaultIsTransparent
            )
            .add()
            .build();
}
