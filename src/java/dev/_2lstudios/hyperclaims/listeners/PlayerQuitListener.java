package dev._2lstudios.hyperclaims.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import dev._2lstudios.hyperclaims.player.ProtectionPlayerManager;

import org.bukkit.event.Listener;

public class PlayerQuitListener implements Listener {
    private final ProtectionPlayerManager pPlayerManager;

    public PlayerQuitListener(final ProtectionPlayerManager pPlayerManager) {
        this.pPlayerManager = pPlayerManager;
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        this.pPlayerManager.deletePlayer(event.getPlayer());
    }
}
