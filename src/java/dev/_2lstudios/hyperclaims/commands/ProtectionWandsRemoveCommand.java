package dev._2lstudios.hyperclaims.commands;

import dev._2lstudios.worldsentinel.region.RegionFlags;
import dev._2lstudios.worldsentinel.region.Region;
import org.bukkit.Location;
import org.bukkit.ChatColor;
import java.util.Collection;
import org.bukkit.entity.Player;
import dev._2lstudios.worldsentinel.region.RegionManager;

public class ProtectionWandsRemoveCommand {
    public ProtectionWandsRemoveCommand(final RegionManager regionManager, final String[] args, final String label,
            final Player player) {
        if (args.length > 1) {
            final Location location = player.getLocation();
            final Region region = regionManager.getRegionInside(location);
            if (region != null) {
                final RegionFlags flags = region.getFlags();
                if (flags.getCollection("extra_flags").contains("ProtectionWands")
                        && flags.getCollection("owners").contains(player.getName())) {
                    final Collection<String> members = (Collection<String>) flags.getCollection("members");
                    members.remove(args[1]);
                    flags.setCollection("members", (Collection) members);
                    player.sendMessage(ChatColor.GREEN + "Removiste al jugador del claim!");
                } else {
                    player.sendMessage(ChatColor.RED + "No eres owner de ese claim!");
                }
            } else {
                player.sendMessage(ChatColor.RED + "No estas dentro de ningun claim!");
            }
        } else {
            player.sendMessage(ChatColor.RED + "/" + label + " remove <jugador>!");
        }
    }
}
