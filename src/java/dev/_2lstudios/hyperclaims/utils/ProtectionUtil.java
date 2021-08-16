package dev._2lstudios.hyperclaims.utils;

import org.bukkit.World;
import java.util.Iterator;
import org.bukkit.util.Vector;
import dev._2lstudios.worldsentinel.region.RegionFlags;
import org.bukkit.Location;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;

import dev._2lstudios.hyperclaims.player.ProtectionPlayer;
import dev._2lstudios.worldsentinel.region.Region;
import java.util.Collection;
import java.util.Collections;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import dev._2lstudios.worldsentinel.region.RegionManager;
import net.milkbowl.vault.economy.Economy;

public class ProtectionUtil {
    private ProtectionUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static void claim(final Economy economy, final RegionManager regionManager, final ProtectionPlayer pPlayer,
            final Player player) {
        final Location position1 = pPlayer.getPosition1();
        final Location position2 = pPlayer.getPosition2();
        if (economy == null) {
            player.sendMessage(ChatColor.RED + "No hay un plugin de economia instalado!");
        } else if (position1 == null || position2 == null) {
            player.sendMessage(ChatColor.RED + "Debes seleccionar la posicion 1 y 2 para claimear!");
        } else {
            pPlayer.fixPositions();
            final double xDifference = Math.abs(position1.getX() - position2.getX());
            final double zDifference = Math.abs(position1.getZ() - position2.getZ());
            if (xDifference >= 5.0 && zDifference >= 5.0) {
                if (xDifference <= 50.0 && zDifference <= 50.0) {
                    final Location[] positions = getCuboidPoints(position1, position2);
                    final Region region = regionManager.createRandomRegion("PW");
                    final RegionFlags flags = region.getFlags();
                    flags.setCollection("extra_flags", Collections.singletonList("ProtectionWands"));
                    final Vector position1Vector = positions[0].toVector();
                    final Vector position2Vector = positions[1].toVector();
                    final String worldName = position1.getWorld().getName();
                    flags.setString("world", worldName);
                    flags.setVector("position1", position1Vector);
                    flags.setVector("position2", position2Vector);
                    for (final Region region2 : region.getRegionsInside()) {
                        if (region != region2) {
                            final RegionFlags flags2 = region2.getFlags();
                            if (!flags2.getCollection("extra_flags").contains("ProtectionWands")
                                    || !flags2.getCollection("owners").isEmpty()) {
                                regionManager.deleteRegion(flags.getString("name"));
                                player.sendMessage(
                                        ChatColor.RED + "El claim esta encima del claim " + region2.getName() + "!");
                                return;
                            }
                            regionManager.deleteRegion(flags2.getString("name"));
                            player.sendMessage(ChatColor.RED + "Se ha eliminado un claim bugueado de la zona!");
                        }
                    }
                    final double area = xDifference * zDifference;
                    final double price = (int) (area / 3.0 * 1000.0) / 1000.0;
                    if (economy.withdrawPlayer((OfflinePlayer) player,
                            price).type == EconomyResponse.ResponseType.SUCCESS) {
                        setDefaultFlags(flags, player.getName());
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                String.format("&aCreaste el claim &b%s por &6$%s&a!", flags.getString("name"), price)));
                    } else {
                        player.sendMessage(String.format(
                                ChatColor.translateAlternateColorCodes('&', "Necesitas %s para claimear esta region!"),
                                price));
                        regionManager.deleteRegion(flags.getString("name"));
                    }
                    pPlayer.setPosition1(null);
                    pPlayer.setPosition2(null);
                } else {
                    player.sendMessage(ChatColor.RED + String.format(
                            "La distancia entre posiciones debe ser menor a 50 bloques! Actual: X: %s Z: %s",
                            xDifference, zDifference));
                }
            } else {
                player.sendMessage(ChatColor.RED
                        + String.format("La distancia entre posiciones debe ser mayor a 5 bloques! Actual: X: %s Z: %s",
                                xDifference, zDifference));
            }
        }
    }

    public static void clearFarPillars(final ProtectionPlayer pPlayer, final Location playerLocation, final int distance) {
        final Collection<Location> pillarMap = pPlayer.getPillarLocations();
        final World playerWorld = playerLocation.getWorld();
        final Iterator<Location> locationIterator = pillarMap.iterator();
        while (locationIterator.hasNext()) {
            final Location location = locationIterator.next();
            if (location.getWorld() != playerWorld || playerLocation.distance(location) > distance) {
                locationIterator.remove();
            }
        }
    }

    public static void setDefaultFlags(final RegionFlags flags, final String ownerName) {
        flags.setBoolean("bow", true);
        flags.setBoolean("enderpearl", true);
        flags.setBoolean("explosions", false);
        flags.setBoolean("fire_spread", false);
        flags.setBoolean("potions", true);
        flags.setBoolean("movement", true);
        flags.setBoolean("pvp", true);
        flags.setBoolean("pve", true);
        flags.setBoolean("evp", true);
        flags.setBoolean("creatures", true);
        flags.setBoolean("entity_damage", true);
        flags.setBoolean("player_damage", true);
        flags.setBoolean("fall_damage", true);
        flags.setBoolean("growing", true);
        flags.setBoolean("interacting", false);
        flags.setBoolean("placing", false);
        flags.setBoolean("breaking", false);
        flags.setCollection("owners", Collections.singletonList(ownerName));
    }

    public static Location[] getCuboidPoints(final Location position1, final Location position2) {
        final Location[] positions = new Location[8];
        positions[0] = position1.clone();
        positions[1] = position2.clone();
        (positions[2] = position1.clone()).setX(position2.getX());
        (positions[3] = position2.clone()).setX(position1.getX());
        (positions[4] = position1.clone()).setY(position2.getY());
        (positions[5] = position2.clone()).setY(position1.getY());
        (positions[6] = positions[2].clone()).setY(position2.getY());
        (positions[7] = positions[3].clone()).setY(position1.getY());
        return positions;
    }
}
