package dev._2lstudios.hyperclaims.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import dev._2lstudios.hyperclaims.player.ProtectionPlayer;
import dev._2lstudios.hyperclaims.player.ProtectionPlayerManager;

public class HyperClaimsMapCommand {
    public HyperClaimsMapCommand(final ProtectionPlayerManager pPlayerManager, final Player player) {
        final ProtectionPlayer pPlayer = pPlayerManager.getPlayer(player);
        pPlayer.setMapEnabled(!pPlayer.isMapEnabled());
        if (pPlayer.isMapEnabled()) {
            player.sendMessage(ChatColor.GREEN + "Habilitaste el visualizador de regiones!");
        } else {
            player.sendMessage(ChatColor.RED + "Deshabilitaste el visualizador de regiones!");
        }
    }
}
