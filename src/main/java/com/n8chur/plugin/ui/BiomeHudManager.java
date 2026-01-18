package com.n8chur.plugin.ui;

import com.buuz135.mhud.MultipleHUD;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class BiomeHudManager {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    private final Map<PlayerRef, BiomeHud> huds = new HashMap<>();

    private boolean hasLoggedMultipleHUDAccessError = false;
    private boolean isMultipleHUDPresent = false;

    public void setMultipleHUDPresent(boolean multipleHUDPresent) {
        isMultipleHUDPresent = multipleHUDPresent;
    }

    public void updateHud(@Nonnull Player player, @Nonnull PlayerRef playerRef, @Nonnull BiomeHud.BiomeInfo biomeInfo) {
        boolean isNew = !huds.containsKey(playerRef);
        BiomeHud hud = huds.computeIfAbsent(playerRef, BiomeHud::new);

        if (biomeInfo.equals(hud.getBiomeInfo())) {
            return;
        }

        hud.updateHud(biomeInfo);

        if (isNew) {
            show(player, playerRef, hud);
        } else {
            update(player, playerRef, hud);
        }
    }

    public void hideHud(@Nonnull Player player, @Nonnull PlayerRef playerRef) {
        if (!huds.containsKey(playerRef)) return;

        BiomeHud hud = huds.remove(playerRef);
        hud.updateHud(null);
        if (isMultipleHUDPresent) {
            setMultipleHUDCustomHUD(player, playerRef, hud);
            this.hasLoggedMultipleHUDAccessError = false;
        } else {
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
        if (!canAccessMultipleHUD()) {
            if (!this.hasLoggedMultipleHUDAccessError) {
                LOGGER.atSevere().log("Cannot access MultipleHUD even though the plugin is loaded!");
                this.hasLoggedMultipleHUDAccessError = true;
            }
            return;
        }

        MultipleHUD.getInstance().setCustomHud(player, playerRef, "BiomeDisplay_HUD", hud);
    }

    private boolean canAccessMultipleHUD() {
        try {
            Class.forName("com.buuz135.mhud.MultipleHUD");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
