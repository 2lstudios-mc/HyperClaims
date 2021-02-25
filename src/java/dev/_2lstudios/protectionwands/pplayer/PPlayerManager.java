// 
// Decompiled by Procyon v0.5.36
// 

package dev._2lstudios.protectionwands.pplayer;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.UUID;
import java.util.Map;
import org.bukkit.plugin.Plugin;

public class PPlayerManager {
    private final Plugin plugin;
    private final Map<UUID, PPlayer> protectionPlayerMap;

    public PPlayerManager(final Plugin plugin) {
        this.protectionPlayerMap = new HashMap<UUID, PPlayer>();
        this.plugin = plugin;
    }

    public PPlayer getPlayer(final Player player) {
        final UUID uuid = player.getUniqueId();
        PPlayer pPlayer;
        if (this.protectionPlayerMap.containsKey(uuid)) {
            pPlayer = this.protectionPlayerMap.get(uuid);
        } else {
            pPlayer = new PPlayer(player);
            this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, pPlayer::loadData);
            this.protectionPlayerMap.put(uuid, pPlayer);
        }
        return pPlayer;
    }

    public void deletePlayer(final Player player) {
        final UUID uuid = player.getUniqueId();
        if (this.protectionPlayerMap.containsKey(uuid)) {
            final PPlayer pPlayer = this.protectionPlayerMap.get(uuid);
            this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, pPlayer::saveData);
            this.protectionPlayerMap.remove(uuid);
        }
    }
}
