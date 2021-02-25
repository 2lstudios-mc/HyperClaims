// 
// Decompiled by Procyon v0.5.36
// 

package dev._2lstudios.protectionwands.commands;

import dev._2lstudios.worldsentinel.region.RegionFlags;
import dev._2lstudios.worldsentinel.region.Region;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import dev._2lstudios.worldsentinel.region.RegionManager;

public class ProtectionWandsInfoCommand {
    public ProtectionWandsInfoCommand(final String[] args, final RegionManager regionManager, final Player player) {
        Region region;
        if (args.length > 1) {
            region = regionManager.getRegion(args[1]);
        } else {
            region = regionManager.getRegionInside(player.getLocation());
        }
        if (region == null) {
            player.sendMessage(ChatColor.RED + "No estas dentro de ningun claim!");
        } else {
            final RegionFlags flags = region.getFlags();
            if (flags.getCollection("extra_flags").contains("ProtectionWands")) {
                final String green = ChatColor.GREEN.toString();
                final String blue = ChatColor.BLUE.toString();
                final String lineSeparator = System.lineSeparator();
                player.sendMessage((String.valueOf(green) + "Informacion de region " + flags.getString("name") + ":")
                        .concat(String.valueOf(lineSeparator) + blue + "World: " + green + flags.getString("world"))
                        .concat(String.valueOf(lineSeparator) + blue + "Position 1: " + green
                                + flags.getVector("position1"))
                        .concat(String.valueOf(lineSeparator) + blue + "Position 2: " + green
                                + flags.getVector("position2"))
                        .concat(String.valueOf(lineSeparator) + blue + "Owners: " + green
                                + flags.getCollection("owners"))
                        .concat(String.valueOf(lineSeparator) + blue + "Members: " + green
                                + flags.getCollection("members")));
            } else {
                player.sendMessage(ChatColor.RED + "Ese claim no es una region de proteccion!");
            }
        }
    }
}
