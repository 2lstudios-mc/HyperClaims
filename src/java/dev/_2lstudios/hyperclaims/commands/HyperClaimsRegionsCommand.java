package dev._2lstudios.hyperclaims.commands;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import dev._2lstudios.hyperclaims.player.ProtectionPlayer;
import dev._2lstudios.hyperclaims.player.ProtectionPlayerManager;
import dev._2lstudios.worldsentinel.region.Region;
import dev._2lstudios.worldsentinel.region.RegionManager;

public class HyperClaimsRegionsCommand {
    public HyperClaimsRegionsCommand(final Server server, final RegionManager regionManager,
            final ProtectionPlayerManager pPlayerManager, final String[] args, final Player player) {
        ProtectionPlayer pPlayer;
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
