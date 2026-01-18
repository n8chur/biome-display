package com.n8chur.plugin.ui;

import com.buuz135.mhud.MultipleHUD;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
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

    public void updateHud(@Nonnull Player player, @Nonnull PlayerRef playerRef, @Nonnull BiomeHud.BiomeInfo biomeInfo, @Nonnull BiomeDisplayUserSettingsComponent.HudPosition position) {
        boolean isNew = !huds.containsKey(playerRef);
        BiomeHud hud = huds.computeIfAbsent(playerRef, BiomeHud::new);

        if (biomeInfo.equals(hud.getBiomeInfo()) && position == hud.getPosition()) {
            return;
        }

        hud.updateBiomeInfo(biomeInfo);
        hud.updatePosition(position);

        if (isNew) {
            show(player, playerRef, hud);
        } else {
            update(player, playerRef, hud);
        }
    }

    public void hideHud(@Nonnull Player player, @Nonnull PlayerRef playerRef) {
        if (!huds.containsKey(playerRef)) return;

        BiomeHud hud = huds.remove(playerRef);
        if (isMultipleHUDPresent) {
            hideMultipleHUDCustomHUD(player, playerRef);
        } else {
            hud.updateBiomeInfo(null);
            show(player, playerRef, hud);
        }
    }

    public void onPlayerLeave(@Nonnull PlayerRef playerRef) {
        huds.remove(playerRef);
    }

    private void show(@Nonnull Player player, @Nonnull PlayerRef playerRef, @Nonnull CustomUIHud hud) {
        if (isMultipleHUDPresent) {
            setMultipleHUDCustomHUD(player, playerRef, hud);
        } else {
            player.getHudManager().setCustomHud(playerRef, hud);
        }
    }

    private void update(@Nonnull Player player, @Nonnull PlayerRef playerRef, @Nonnull CustomUIHud hud) {
        if (isMultipleHUDPresent) {
            setMultipleHUDCustomHUD(player, playerRef, hud);
        } else {
            hud.show();
        }
    }

    private void setMultipleHUDCustomHUD(@Nonnull Player player, @Nonnull PlayerRef playerRef, @Nonnull CustomUIHud hud) {
        MultipleHUD.getInstance().setCustomHud(player, playerRef, BIOME_DISPLAY_HUD_ID, hud);
    }

    private void hideMultipleHUDCustomHUD(@Nonnull Player player, @Nonnull PlayerRef playerRef) {
        MultipleHUD.getInstance().hideCustomHud(player, playerRef, BIOME_DISPLAY_HUD_ID);
    }
}
