package com.n8chur.plugin;

import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.n8chur.plugin.settings.BiomeDisplayUserSettingsComponent;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;

public class BiomeDisplayCommand extends AbstractPlayerCommand {

    @Nonnull
    private final ComponentType<EntityStore, BiomeDisplayUserSettingsComponent> userSettingsComponentType;

    public BiomeDisplayCommand(@Nonnull ComponentType<EntityStore, BiomeDisplayUserSettingsComponent> userSettingsComponentType) {
        super("biome", "Toggles the biome info HUD.");

        this.userSettingsComponentType = userSettingsComponentType;

        this.setPermissionGroup(GameMode.Adventure);
    }

    @Override
    protected void execute(
        @NonNullDecl CommandContext commandContext,
        @NonNullDecl Store<EntityStore> store,
        @NonNullDecl Ref<EntityStore> ref,
        @NonNullDecl PlayerRef playerRef,
        @NonNullDecl World world
    ) {
        BiomeDisplayUserSettingsComponent settings = store.ensureAndGetComponent(ref, userSettingsComponentType);

        settings.toggleEnabled();
    }
}
