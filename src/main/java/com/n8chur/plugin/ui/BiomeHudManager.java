package com.n8chur.plugin.ui;

import com.buuz135.mhud.MultipleHUD;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.n8chur.plugin.settings.BiomeDisplayUserSettingsComponent;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class BiomeHudManager {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    private static final String BIOME_DISPLAY_HUD_ID = "BiomeDisplay_HUD";

    private final Map<PlayerRef, BiomeHud> huds = new HashMap<>();

    private boolean isMultipleHUDPresent = false;

    public void setMultipleHUDPresent(boolean multipleHUDPresent) {
        isMultipleHUDPresent = multipleHUDPresent;
    }

    public void updateHud(
        @Nonnull Player player,
        @Nonnull PlayerRef playerRef,
        @Nonnull BiomeHud.BiomeInfo biomeInfo,
        @Nonnull BiomeDisplayUserSettingsComponent.HudPosition position,
        @Nonnull BiomeDisplayUserSettingsComponent.HudSize size
    ) {
        boolean isNew = !huds.containsKey(playerRef);
        BiomeHud hud = huds.computeIfAbsent(playerRef, BiomeHud::new);

        hud.updateBiomeInfo(biomeInfo);
        hud.updatePosition(position);
        hud.updateSize(size);

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
        if (!huds.containsKey(playerRef)) return;

        BiomeHud hud = huds.remove(playerRef);
        hud.updateBiomeInfo(null);
        hud.clear();

        if (isMultipleHUDPresent) {
            MultipleHUD.getInstance().hideCustomHud(player, playerRef, BIOME_DISPLAY_HUD_ID);
        }
    }

    public void onPlayerLeave(@Nonnull PlayerRef playerRef) {
        huds.remove(playerRef);
    }
}
