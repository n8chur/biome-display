package com.n8chur.plugin.ui;

import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class BiomeHudProvider {

    private final Map<PlayerRef, BiomeHud> huds = new HashMap<>();

    public void updateHud(@Nonnull Player player, @Nonnull PlayerRef playerRef, @Nonnull BiomeHud.BiomeInfo biomeInfo) {
        if (!huds.containsKey(playerRef)) {
            BiomeHud biomeHud = new BiomeHud(playerRef);
            huds.put(playerRef, biomeHud);
            biomeHud.updateHud(biomeInfo);
            player.getHudManager().setCustomHud(playerRef, biomeHud);
        } else {
            BiomeHud biomeHud = huds.get(playerRef);
            biomeHud.updateHud(biomeInfo);
            biomeHud.show();
        }
    }

    public void hideHud(@Nonnull Player player, @Nonnull PlayerRef playerRef) {
        if (!huds.containsKey(playerRef)) return;

        BiomeHud biomeHud = huds.get(playerRef);
        huds.remove(playerRef);
        biomeHud.updateHud(null);
        biomeHud.show();
    }
}
