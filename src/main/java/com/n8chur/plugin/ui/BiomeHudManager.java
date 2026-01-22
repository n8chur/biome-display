package com.n8chur.plugin.ui;

import com.buuz135.mhud.MultipleHUD;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.n8chur.plugin.settings.BiomeDisplayUserSettingsComponent;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class BiomeHudManager {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    private static final String BIOME_DISPLAY_HUD_ID = "BiomeDisplay_HUD";

    // Maps player UUIDs to their BiomeHud instances.
    private final ConcurrentHashMap<UUID, BiomeHud> huds = new ConcurrentHashMap<>();

    private boolean isMultipleHUDPresent = false;

    public void setMultipleHUDPresent(boolean multipleHUDPresent) {
        isMultipleHUDPresent = multipleHUDPresent;
    }

    public void updateHud(
        @Nonnull Player player,
        @Nonnull PlayerRef playerRef,
        @Nonnull BiomeHud.BiomeInfo biomeInfo,
        @Nonnull BiomeDisplayUserSettingsComponent.HudPosition position,
        @Nonnull BiomeDisplayUserSettingsComponent.HudSize size,
        @Nonnull boolean isTransparent
    ) {
        UUID uuid = playerRef.getUuid();

        // No need to use special atomic operations here since each player's HUD is only accessed by their own thread.
        // We'll never see the same UUID being accessed from multiple threads at the same time.
        boolean isNew = !huds.containsKey(uuid);
        BiomeHud hud = huds.computeIfAbsent(uuid, k -> new BiomeHud(playerRef));

        if (
            !isNew
            && Objects.equals(hud.getBiomeInfo(), biomeInfo)
            && Objects.equals(hud.getPosition(), position)
            && hud.getSize() == size
            && hud.getIsTransparent() == isTransparent
        ) {
            return;
        }

        hud.updateBiomeInfo(biomeInfo);
        hud.updatePosition(position);
        hud.updateSize(size);
        hud.updateIsTransparent(isTransparent);

        if (isNew) {
            if (isMultipleHUDPresent) {
                MultipleHUD.getInstance().setCustomHud(player, playerRef, BIOME_DISPLAY_HUD_ID, hud);
            } else {
                player.getHudManager().setCustomHud(playerRef, hud);
            }
        } else {
            hud.update();
        }
    }

    public void hideHud(@Nonnull Player player, @Nonnull PlayerRef playerRef) {
        UUID uuid = playerRef.getUuid();

        if (!huds.containsKey(uuid)) return;

        BiomeHud hud = huds.remove(uuid);

        if (isMultipleHUDPresent) {
            MultipleHUD.getInstance().hideCustomHud(player, playerRef, BIOME_DISPLAY_HUD_ID);
        } else {
            hud.clear();
        }
    }

    public void onPlayerLeave(@Nonnull PlayerRef playerRef) {
        huds.remove(playerRef.getUuid());
    }
}
