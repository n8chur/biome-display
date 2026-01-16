package com.n8chur.plugin.ui;

import com.buuz135.mhud.MultipleHUD;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.n8chur.plugin.BiomeDisplayPlugin;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class BiomeHudProvider {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    private final Map<PlayerRef, BiomeHud> huds = new HashMap<>();

    private boolean hasLoggedMultipleHUDAccessError = false;

    public void updateHud(@Nonnull Player player, @Nonnull PlayerRef playerRef, @Nonnull BiomeHud.BiomeInfo biomeInfo) {
        if (!huds.containsKey(playerRef)) {
            BiomeHud hud = new BiomeHud(playerRef);
            huds.put(playerRef, hud);
            hud.updateHud(biomeInfo);
            update(player, playerRef, hud);
        } else {
            BiomeHud hud = huds.get(playerRef);
            hud.updateHud(biomeInfo);
            show(player, playerRef, hud);
        }
    }

    public void hideHud(@Nonnull Player player, @Nonnull PlayerRef playerRef) {
        if (!huds.containsKey(playerRef)) return;

        BiomeHud hud = huds.get(playerRef);
        huds.remove(playerRef);
        hud.updateHud(null);
        if (BiomeDisplayPlugin.isMultipleHUDPresent()) {
            setMultipleHUDCustomHUD(player, playerRef, hud);
            this.hasLoggedMultipleHUDAccessError = false;
        } else {
            show(player, playerRef, hud);
        }
    }

    private void show(@Nonnull Player player, @Nonnull PlayerRef playerRef, @Nonnull CustomUIHud hud) {
        if (BiomeDisplayPlugin.isMultipleHUDPresent()) {
            setMultipleHUDCustomHUD(player, playerRef, hud);
        } else {
            hud.show();
        }
    }

    private void update(@Nonnull Player player, @Nonnull PlayerRef playerRef, @Nonnull CustomUIHud hud) {
        if (BiomeDisplayPlugin.isMultipleHUDPresent()) {
            setMultipleHUDCustomHUD(player, playerRef, hud);
        } else {
            player.getHudManager().setCustomHud(playerRef, hud);
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
