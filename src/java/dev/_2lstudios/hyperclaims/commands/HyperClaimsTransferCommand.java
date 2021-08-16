package dev._2lstudios.hyperclaims.commands;

import dev._2lstudios.worldsentinel.region.RegionFlags;
import dev._2lstudios.worldsentinel.region.Region;
import org.bukkit.Location;
import org.bukkit.ChatColor;
import java.util.Collection;
import java.util.Collections;
import org.bukkit.entity.Player;
import dev._2lstudios.worldsentinel.region.RegionManager;
import org.bukkit.Server;

public class HyperClaimsTransferCommand {
    public HyperClaimsTransferCommand(final Server server, final RegionManager regionManager, final String[] args,
            final String label, final Player player) {
        if (args.length > 1) {
            final Location location = player.getLocation();
            final Region region = regionManager.getRegionInside(location);
            if (region != null) {
                final RegionFlags flags = region.getFlags();
                if (flags.getCollection("extra_flags").contains("ProtectionWands")) {
                    if (flags.getCollection("owners").contains(player.getName())) {
                        final Player player2 = server.getPlayer(args[1]);
                        if (player2 != null && player2.isOnline()) {
                            flags.setCollection("owners", (Collection) Collections.singleton(args[1]));
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    "&aTransferiste el claim a &b" + args[1] + "&a!"));
                        } else {
                            player.sendMessage(ChatColor.RED + "El jugador especificado no esta en linea!");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "No eres owner de ese claim!");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "La region donde estas parado no es un claim!");
                }
            } else {
                player.sendMessage(ChatColor.RED + "No estas dentro de ningun claim!");
            }
        } else {
            player.sendMessage(ChatColor.RED + "/" + label + " transfer <jugador>!");
        }
    }
}
