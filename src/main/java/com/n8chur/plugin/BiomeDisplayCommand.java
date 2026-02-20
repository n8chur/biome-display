package com.n8chur.plugin;

import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector2i;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.ParseResult;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.arguments.types.SingleArgumentType;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.n8chur.plugin.settings.BiomeDisplayUserSettingsComponent;

import javax.annotation.Nonnull;

public class BiomeDisplayCommand extends AbstractCommandCollection {

    public BiomeDisplayCommand(
        @Nonnull ComponentType<EntityStore, BiomeDisplayUserSettingsComponent> userSettingsComponentType
    ) {
        super("biome", "Manage the biome display HUD.");

        this.setPermissionGroup(GameMode.Adventure);

        addSubCommand(new ToggleSubCommand(userSettingsComponentType));
        addSubCommand(new PositionSubCommand(userSettingsComponentType));
        addSubCommand(new OffsetSubCommand(userSettingsComponentType));
        addSubCommand(new SizeSubCommand(userSettingsComponentType));
        addSubCommand(new TransparentSubCommand(userSettingsComponentType));
    }

    private static class ToggleSubCommand extends AbstractPlayerCommand {

        @Nonnull
        private final ComponentType<EntityStore, BiomeDisplayUserSettingsComponent> userSettingsComponentType;

        public ToggleSubCommand(
            @Nonnull ComponentType<EntityStore, BiomeDisplayUserSettingsComponent> userSettingsComponentType
        ) {
            super("toggle", "Toggles the biome display HUD.");

            this.userSettingsComponentType = userSettingsComponentType;

            this.setPermissionGroup(GameMode.Adventure);
        }

        @Override
        protected void execute(
            @Nonnull CommandContext commandContext,
            @Nonnull Store<EntityStore> store,
            @Nonnull Ref<EntityStore> ref,
            @Nonnull PlayerRef playerRef,
            @Nonnull World world
        ) {
            BiomeDisplayUserSettingsComponent settings = store.ensureAndGetComponent(ref, userSettingsComponentType);

            settings.toggleEnabled();
        }
    }

    private static class PositionSubCommand extends AbstractPlayerCommand {

        @Nonnull
        private final ComponentType<EntityStore, BiomeDisplayUserSettingsComponent> userSettingsComponentType;

        private final RequiredArg<String> verticalArg;
        private final RequiredArg<String> horizontalArg;

        public PositionSubCommand(
            @Nonnull ComponentType<EntityStore, BiomeDisplayUserSettingsComponent> userSettingsComponentType
        ) {
            super("position", "The position of the biome display HUD (e.g. \"bottom right\".");

            this.userSettingsComponentType = userSettingsComponentType;

            this.setPermissionGroup(GameMode.Adventure);

            this.verticalArg = withRequiredArg(
                "vertical",
                "Vertical position",
                new SingleArgumentType<String>(
                    "Vertical Position",
                    "Can be \"top\", \"middle\", or \"bottom\"",
                    "\"top\"", "\"middle\"", "\"bottom\""
                ) {
                    public String parse(String input, ParseResult parseResult) {
                        return input;
                    }
                }
            );
            this.horizontalArg = withRequiredArg(
                "horizontal",
                "Horizontal position",
                new SingleArgumentType<String>(
                    "Horizontal Position",
                    "Can be \"left\", \"center\", or \"right\"",
                    "\"left\"", "\"center\"", "\"right\""
                ) {
                    public String parse(String input, ParseResult parseResult) {
                        return input;
                    }
                }
            );
        }

        @Override
        protected void execute(
            @Nonnull CommandContext ctx,
            @Nonnull Store<EntityStore> store,
            @Nonnull Ref<EntityStore> ref,
            @Nonnull PlayerRef playerRef,
            @Nonnull World world
        ) {
            BiomeDisplayUserSettingsComponent settings = store.ensureAndGetComponent(
                ref,
                this.userSettingsComponentType
            );

            String vertical = this.verticalArg.get(ctx).toUpperCase();
            String horizontal = this.horizontalArg.get(ctx).toUpperCase();

            BiomeDisplayUserSettingsComponent.HudPosition.Vertical verticalPosition;
            try {
                verticalPosition = BiomeDisplayUserSettingsComponent.HudPosition.Vertical.valueOf(vertical);
            } catch (IllegalArgumentException e) {
                playerRef.sendMessage(Message.raw("Invalid vertical position. Available: top, middle, bottom"));
                return;
            }

            BiomeDisplayUserSettingsComponent.HudPosition.Horizontal horizontalPosition;
            try {
                horizontalPosition = BiomeDisplayUserSettingsComponent.HudPosition.Horizontal.valueOf(horizontal);
            } catch (IllegalArgumentException e) {
                playerRef.sendMessage(Message.raw("Invalid horizontal position. Available: left, center, right"));
                return;
            }

            BiomeDisplayUserSettingsComponent.HudPosition position = new BiomeDisplayUserSettingsComponent.HudPosition(
                verticalPosition,
                horizontalPosition
            );
            settings.setPosition(position);
            playerRef.sendMessage(Message.raw("HUD position set to " + position));
        }
    }

    private static class OffsetSubCommand extends AbstractPlayerCommand {

        @Nonnull
        private final ComponentType<EntityStore, BiomeDisplayUserSettingsComponent> userSettingsComponentType;

        private final RequiredArg<Integer> xArg;
        private final RequiredArg<Integer> yArg;

        public OffsetSubCommand(
            @Nonnull ComponentType<EntityStore, BiomeDisplayUserSettingsComponent> userSettingsComponentType
        ) {
            super("offset", "Sets a positional offset of the biome display HUD.");

            this.userSettingsComponentType = userSettingsComponentType;

            this.setPermissionGroup(GameMode.Adventure);

            this.xArg = withRequiredArg("x", "Horizontal offset", ArgTypes.INTEGER);
            this.yArg = withRequiredArg("y", "Vertical offset", ArgTypes.INTEGER);
        }

        @Override
        protected void execute(
            @Nonnull CommandContext ctx,
            @Nonnull Store<EntityStore> store,
            @Nonnull Ref<EntityStore> ref,
            @Nonnull PlayerRef playerRef,
            @Nonnull World world
        ) {
            BiomeDisplayUserSettingsComponent settings = store.ensureAndGetComponent(
                ref,
                this.userSettingsComponentType
            );

            int x = this.xArg.get(ctx);
            int y = this.yArg.get(ctx);
            settings.setOffset(new Vector2i(x, y));
            playerRef.sendMessage(Message.raw("HUD offset set to x=" + x + ", y=" + y));
        }
    }

    private static class SizeSubCommand extends AbstractPlayerCommand {

        @Nonnull
        private final ComponentType<EntityStore, BiomeDisplayUserSettingsComponent> userSettingsComponentType;

        private final RequiredArg<String> sizeArg;

        public SizeSubCommand(
            @Nonnull ComponentType<EntityStore, BiomeDisplayUserSettingsComponent> userSettingsComponentType
        ) {
            super("size", "Sets the size of the HUD element.");

            this.userSettingsComponentType = userSettingsComponentType;

            this.setPermissionGroup(GameMode.Adventure);

            this.sizeArg = withRequiredArg(
                "size",
                "HUD size",
                new SingleArgumentType<String>(
                    "HUD size",
                    "Can be \"small\", \"medium\", or \"large\"",
                    "\"small\"", "\"medium\"", "\"large\""
                ) {
                    public String parse(String input, ParseResult parseResult) {
                        return input;
                    }
                }
            );
        }

        @Override
        protected void execute(
            @Nonnull CommandContext ctx,
            @Nonnull Store<EntityStore> store,
            @Nonnull Ref<EntityStore> ref,
            @Nonnull PlayerRef playerRef,
            @Nonnull World world
        ) {
            BiomeDisplayUserSettingsComponent settings = store.ensureAndGetComponent(
                ref,
                this.userSettingsComponentType
            );

            String value = this.sizeArg.get(ctx).toUpperCase();

            BiomeDisplayUserSettingsComponent.HudSize size;
            try {
                size = BiomeDisplayUserSettingsComponent.HudSize.valueOf(value);
            } catch (IllegalArgumentException e) {
                playerRef.sendMessage(Message.raw("Invalid size. Available: small, medium, large"));
                return;
            }

            settings.setSize(size);
        }
    }

    private static class TransparentSubCommand extends AbstractPlayerCommand {

        @Nonnull
        private final ComponentType<EntityStore, BiomeDisplayUserSettingsComponent> userSettingsComponentType;

        private final RequiredArg<Boolean> transparentArg;

        public TransparentSubCommand(
            @Nonnull ComponentType<EntityStore, BiomeDisplayUserSettingsComponent> userSettingsComponentType
        ) {
            super("transparent", "Reduces the opacity of the background of the HUD element.");

            this.userSettingsComponentType = userSettingsComponentType;

            this.setPermissionGroup(GameMode.Adventure);

            this.transparentArg = withRequiredArg(
                "transparent",
                "Whether the HUD background is transparent",
                ArgTypes.BOOLEAN
            );
        }

        @Override
        protected void execute(
            @Nonnull CommandContext ctx,
            @Nonnull Store<EntityStore> store,
            @Nonnull Ref<EntityStore> ref,
            @Nonnull PlayerRef playerRef,
            @Nonnull World world
        ) {
            BiomeDisplayUserSettingsComponent settings = store.ensureAndGetComponent(
                ref,
                this.userSettingsComponentType
            );

            boolean isTransparent = this.transparentArg.get(ctx);
            settings.setIsTransparent(isTransparent);
        }
    }
}
