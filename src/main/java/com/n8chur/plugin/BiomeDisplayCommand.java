package com.n8chur.plugin;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.n8chur.plugin.settings.BiomeDisplayUserSettings;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class BiomeDisplayCommand extends AbstractPlayerCommand {

    private final BiomeDisplayUserSettings userSettings;

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public BiomeDisplayCommand(BiomeDisplayUserSettings userSettings) {
        super("biome", "Toggles the biome info HUD.");

        this.userSettings = userSettings;

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
        boolean wasEnabled = this.userSettings.getIsEnabled(playerRef);
        boolean isEnabled = !wasEnabled;
        this.userSettings.setIsEnabled(playerRef, isEnabled);

        BiomeDisplayCommand.LOGGER.atInfo().log("wasEnabled: " + wasEnabled + ", isEnabled: " + isEnabled);
    }
}
