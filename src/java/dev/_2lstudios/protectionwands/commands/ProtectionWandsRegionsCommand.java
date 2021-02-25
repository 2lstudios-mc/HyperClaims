// 
// Decompiled by Procyon v0.5.36
// 

package dev._2lstudios.protectionwands.commands;

import dev._2lstudios.worldsentinel.region.Region;
import java.util.Iterator;
import dev._2lstudios.protectionwands.pplayer.PPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import dev._2lstudios.protectionwands.pplayer.PPlayerManager;
import dev._2lstudios.worldsentinel.region.RegionManager;
import org.bukkit.Server;

public class ProtectionWandsRegionsCommand {
    public ProtectionWandsRegionsCommand(final Server server, final RegionManager regionManager,
            final PPlayerManager pPlayerManager, final String[] args, final Player player) {
        PPlayer pPlayer;
        if (args.length > 1) {
            final Player player2 = server.getPlayer(args[1]);
            if (player2 == null) {
                pPlayer = null;
            } else {
                pPlayer = pPlayerManager.getPlayer(player2);
            }
        } else {
            pPlayer = pPlayerManager.getPlayer(player);
        }
        if (pPlayer == null) {
            player.sendMessage(
                    ChatColor.translateAlternateColorCodes('&', "&cEl jugador especificado no esta en linea!"));
        } else {
            final StringBuilder stringBuilder = new StringBuilder(
                    ChatColor.translateAlternateColorCodes('&', "&9Regiones de ")).append(pPlayer.getName())
                            .append(":\n");
            boolean first = true;
            for (final String regionName : pPlayer.getRegions()) {
                final Region region = regionManager.getRegion(regionName);
                if (first) {
                    first = false;
                } else {
                    stringBuilder.append(ChatColor.GRAY.toString()).append(", ");
                }
                stringBuilder.append(ChatColor.GREEN.toString()).append(region.getFlags().getString("name"));
            }
            player.sendMessage(stringBuilder.toString());
        }
    }
}
