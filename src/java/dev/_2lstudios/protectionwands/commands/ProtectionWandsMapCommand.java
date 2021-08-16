package dev._2lstudios.protectionwands.commands;

import dev._2lstudios.protectionwands.pplayer.PPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import dev._2lstudios.protectionwands.pplayer.PPlayerManager;

public class ProtectionWandsMapCommand {
    public ProtectionWandsMapCommand(final PPlayerManager pPlayerManager, final Player player) {
        final PPlayer pPlayer = pPlayerManager.getPlayer(player);
        pPlayer.setMapEnabled(!pPlayer.isMapEnabled());
        if (pPlayer.isMapEnabled()) {
            player.sendMessage(ChatColor.GREEN + "Habilitaste el visualizador de regiones!");
        } else {
            player.sendMessage(ChatColor.RED + "Deshabilitaste el visualizador de regiones!");
        }
    }
}
