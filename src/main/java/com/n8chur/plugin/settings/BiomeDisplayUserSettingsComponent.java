package com.n8chur.plugin.settings;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class BiomeDisplayUserSettingsComponent implements Component<EntityStore> {

    private boolean enabled = false;

    public BiomeDisplayUserSettingsComponent() {}

    public boolean getIsEnabled() { return this.enabled; }

    public void toggleEnabled() {
        this.enabled = !this.enabled;
    }

    public static final BuilderCodec<BiomeDisplayUserSettingsComponent> CODEC =
        BuilderCodec.builder(BiomeDisplayUserSettingsComponent.class, BiomeDisplayUserSettingsComponent::new)
            .append(new KeyedCodec<>("BiomeDisplayEnabled", Codec.BOOLEAN),
                (c, v) -> c.enabled = v, c -> c.enabled)
            .add()
            .build();

    @Override
    public Component<EntityStore> clone() {
        BiomeDisplayUserSettingsComponent copy = new BiomeDisplayUserSettingsComponent();
        copy.enabled = this.enabled;
        return copy;
    }
}
