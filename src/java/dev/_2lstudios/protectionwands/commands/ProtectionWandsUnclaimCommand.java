// 
// Decompiled by Procyon v0.5.36
// 

package dev._2lstudios.protectionwands.commands;

import dev._2lstudios.worldsentinel.region.RegionFlags;
import dev._2lstudios.worldsentinel.region.Region;
import org.bukkit.Location;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import dev._2lstudios.worldsentinel.region.RegionManager;

public class ProtectionWandsUnclaimCommand {
    public ProtectionWandsUnclaimCommand(final RegionManager regionManager, final Player player) {
        final Location location = player.getLocation();
        final Region region = regionManager.getRegionInside(location);
        if (region != null) {
            final RegionFlags flags = region.getFlags();
            if (flags.getCollection("extra_flags").contains("ProtectionWands")
                    && flags.getCollection("owners").contains(player.getName())) {
                if (regionManager.deleteRegion(flags.getString("name"))) {
                    player.sendMessage(ChatColor.GREEN + "Eliminaste el claim correctamente!");
                } else {
                    player.sendMessage(ChatColor.RED + "No estas sobre ningun claim!");
                }
            } else {
                player.sendMessage(ChatColor.RED + "No eres owner de ese claim!");
            }
        } else {
            player.sendMessage(ChatColor.RED + "No estas dentro de ningun claim!");
        }
    }
}
