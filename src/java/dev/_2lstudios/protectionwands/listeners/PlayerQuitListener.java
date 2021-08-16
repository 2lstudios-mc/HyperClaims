package dev._2lstudios.protectionwands.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import dev._2lstudios.protectionwands.pplayer.PPlayerManager;
import org.bukkit.event.Listener;

public class PlayerQuitListener implements Listener {
    private final PPlayerManager pPlayerManager;

    public PlayerQuitListener(final PPlayerManager pPlayerManager) {
        this.pPlayerManager = pPlayerManager;
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        this.pPlayerManager.deletePlayer(event.getPlayer());
    }
}
