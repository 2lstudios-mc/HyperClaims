package dev._2lstudios.hyperclaims.commands;

import dev._2lstudios.worldsentinel.region.RegionFlags;
import dev._2lstudios.worldsentinel.region.Region;
import org.bukkit.Location;
import org.bukkit.ChatColor;
import java.util.Collection;
import org.bukkit.entity.Player;
import dev._2lstudios.worldsentinel.region.RegionManager;
import org.bukkit.Server;

public class HyperClaimsAddCommand {
    public HyperClaimsAddCommand(final Server server, final RegionManager regionManager, final String[] args,
            final String label, final Player player) {
        if (args.length > 1) {
            final Location location = player.getLocation();
            final Region region = regionManager.getRegionInside(location);
            if (region != null) {
                final RegionFlags flags = region.getFlags();
                if (flags.getCollection("extra_flags").contains("ProtectionWands")) {
                    if (flags.getCollection("owners").contains(player.getName())) {
                        final Collection<String> members = (Collection<String>) flags.getCollection("members");
                        if (members.size() < 5 || player.hasPermission("hyperclaims.limit.infinite")) {
                            final String member = args[1];

                            if (member.length() >= 3) {
                                if (member.length() <= 16) {
                                    if (member.matches("[a-zA-Z0-9_]*")) {
                                        members.add(member);
                                        flags.set("members", members);
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                                "&aAgregaste a &b" + args[1] + "&a como miembro del claim!"));
                                    } else {
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                                "&cEl nick contiene caracteres invalidos!"));
                                    }
                                } else {
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                            "&cEl nick debe ser igual o menor a 16 caracteres!"));
                                }
                            } else {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                        "&cEl nick debe ser mayor o igual a 3 caracteres!"));
                            }
                        } else {
                            player.sendMessage(
                                    ChatColor.translateAlternateColorCodes('&', "&cNo puedes agregar mas miembros! &7("
                                            + members.size() + "/5)\n&eCompra &b&lULTRA&r &epara quitar este limite!"));
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
            player.sendMessage(ChatColor.RED + "/" + label + " add <jugador>!");
        }
    }
}
