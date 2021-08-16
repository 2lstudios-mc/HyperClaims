package dev._2lstudios.hyperclaims.player;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.UUID;
import java.util.Map;
import org.bukkit.plugin.Plugin;

public class ProtectionPlayerManager {
    private final Plugin plugin;
    private final Map<UUID, ProtectionPlayer> protectionPlayerMap;

    public ProtectionPlayerManager(final Plugin plugin) {
        this.protectionPlayerMap = new HashMap<UUID, ProtectionPlayer>();
        this.plugin = plugin;
    }

    public ProtectionPlayer getPlayer(final Player player) {
        final UUID uuid = player.getUniqueId();
        ProtectionPlayer pPlayer;
        if (this.protectionPlayerMap.containsKey(uuid)) {
            pPlayer = this.protectionPlayerMap.get(uuid);
        } else {
            pPlayer = new ProtectionPlayer(player);
            this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, pPlayer::loadData);
            this.protectionPlayerMap.put(uuid, pPlayer);
        }
        return pPlayer;
    }

    public void deletePlayer(final Player player) {
        final UUID uuid = player.getUniqueId();
        if (this.protectionPlayerMap.containsKey(uuid)) {
            final ProtectionPlayer pPlayer = this.protectionPlayerMap.get(uuid);
            this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, pPlayer::saveData);
            this.protectionPlayerMap.remove(uuid);
        }
    }
}
