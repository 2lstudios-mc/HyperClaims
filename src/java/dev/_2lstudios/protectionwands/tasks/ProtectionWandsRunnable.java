// 
// Decompiled by Procyon v0.5.36
// 

package dev._2lstudios.protectionwands.tasks;

import java.lang.reflect.InvocationTargetException;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.ChunkCoordIntPair;
import com.comphenix.protocol.PacketType;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;
import dev._2lstudios.worldsentinel.region.RegionFlags;
import dev._2lstudios.worldsentinel.region.Region;
import org.bukkit.World;
import dev._2lstudios.protectionwands.pplayer.PPlayer;
import java.util.Iterator;
import org.bukkit.Location;
import java.util.HashSet;
import dev._2lstudios.protectionwands.utils.ProtectionUtil;
import com.comphenix.protocol.wrappers.MultiBlockChangeInfo;
import java.util.Collection;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import com.comphenix.protocol.ProtocolLibrary;
import org.bukkit.Material;
import java.util.HashMap;
import dev._2lstudios.worldsentinel.region.RegionManager;
import dev._2lstudios.protectionwands.pplayer.PPlayerManager;
import org.bukkit.Server;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import java.util.Map;

public class ProtectionWandsRunnable implements Runnable {
    private final Map<Integer, WrappedBlockData> firstMaterials;
    private final WrappedBlockData secondMaterial;
    private final ProtocolManager protocolManager;
    private final Server server;
    private final PPlayerManager pPlayerManager;
    private final RegionManager regionManager;
    private final int alternate;
    private final int distance;

    public ProtectionWandsRunnable(final Server server, final PPlayerManager pPlayerManager,
            final RegionManager regionManager) {
        this.firstMaterials = new HashMap<Integer, WrappedBlockData>();
        this.secondMaterial = WrappedBlockData.createData(Material.getMaterial("AIR"), 0);
        this.protocolManager = ProtocolLibrary.getProtocolManager();
        this.server = server;
        this.pPlayerManager = pPlayerManager;
        this.regionManager = regionManager;
        this.alternate = 6;
        this.distance = 80;
        for (int i = 0; i <= 15; ++i) {
            this.firstMaterials.put(i, WrappedBlockData.createData(Material.getMaterial("STAINED_CLAY"), i));
        }
    }

    @Override
    public void run() {
        try {
            for (final Player player : this.server.getOnlinePlayers()) {
                final PPlayer pPlayer = this.pPlayerManager.getPlayer(player);
                if (pPlayer == null) {
                    return;
                }
                final Map<Chunk, Collection<MultiBlockChangeInfo>> blocksToChangeMap = new HashMap<Chunk, Collection<MultiBlockChangeInfo>>();
                if (pPlayer.isMapEnabled()) {
                    final World world = player.getWorld();
                    final Location playerLocation = player.getLocation();
                    final Collection<String> nearRegions = (Collection<String>) this.regionManager
                            .getNearRegions(playerLocation, this.distance);
                    ProtectionUtil.clearFarPillars(pPlayer, playerLocation, this.distance);
                    for (final String regionName : nearRegions) {
                        final Region region = this.regionManager.getRegion(regionName);
                        final RegionFlags flags = region.getFlags();
                        final Vector vector1 = flags.getVector("position1");
                        final Vector vector2 = flags.getVector("position2");
                        if (vector1 != null && vector2 != null
                                && flags.getCollection("extra_flags").contains("ProtectionWands")) {
                            final Collection<Location> blocksToChange = new HashSet<Location>();
                            final Location[] locations = { vector1.toLocation(world), vector2.toLocation(world),
                                    vector1.toLocation(world), null };
                            locations[2].setX(vector2.getX());
                            (locations[3] = vector1.toLocation(world)).setZ(vector2.getZ());
                            final WrappedBlockData firstMaterial = this.firstMaterials
                                    .get(this.generateColorId(locations[0], locations[1]));
                            for (int y = 0; y < world.getMaxHeight(); y += this.alternate) {
                                Location[] array;
                                for (int length = (array = locations).length, i = 0; i < length; ++i) {
                                    final Location location = array[i];
                                    location.setY((double) y);
                                    if (playerLocation.distance(location) < this.distance) {
                                        final String uuid = String.valueOf(location.getX()) + String.valueOf(y)
                                                + location.getZ();
                                        if (!pPlayer.hasPillar(uuid)) {
                                            final Location locationClone = location.clone();
                                            pPlayer.addPillarLocation(uuid, locationClone);
                                            blocksToChange.add(locationClone);
                                        }
                                    }
                                }
                            }
                            this.addBlocks(blocksToChangeMap, blocksToChange, firstMaterial);
                        }
                    }
                } else {
                    this.addBlocks(blocksToChangeMap, pPlayer.getPillarLocations(), this.secondMaterial);
                    pPlayer.clearPillarLocations();
                }
                this.sendBlockChanges(this.protocolManager, blocksToChangeMap, player);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int generateColorId(final Location position1, final Location position2) {
        return Math.abs((int) Math.floor((position1.getX() + position1.getZ() + position1.getY() + position2.getX()
                + position2.getZ() + position2.getY()) % 16.0));
    }

    private void addBlocks(final Map<Chunk, Collection<MultiBlockChangeInfo>> blocksToChangeMap,
            final Collection<Location> blocksToAdd, final WrappedBlockData blockData) {
        if (blocksToAdd == null || blocksToAdd.isEmpty()) {
            return;
        }
        for (final Location blockLocation : blocksToAdd) {
            final Block block = blockLocation.getBlock();
            if (block == null || block.getType() == Material.AIR) {
                final Chunk chunk = blockLocation.getChunk();
                final Collection<MultiBlockChangeInfo> blocksToChange = blocksToChangeMap.getOrDefault(chunk,
                        new HashSet<MultiBlockChangeInfo>());
                blocksToChange.add(new MultiBlockChangeInfo(blockLocation, blockData));
                blocksToChangeMap.put(chunk, blocksToChange);
            }
        }
    }

    private void sendBlockChanges(final ProtocolManager protocolManager,
            final Map<Chunk, Collection<MultiBlockChangeInfo>> blocksToChangeMap, final Player player)
            throws InvocationTargetException {
        if (blocksToChangeMap == null || blocksToChangeMap.isEmpty()) {
            return;
        }
        for (final Map.Entry<Chunk, Collection<MultiBlockChangeInfo>> entry : blocksToChangeMap.entrySet()) {
            final Chunk chunk = entry.getKey();
            final Collection<MultiBlockChangeInfo> blocksToChange = entry.getValue();
            final PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.MULTI_BLOCK_CHANGE);
            packet.getChunkCoordIntPairs().write(0, new ChunkCoordIntPair(chunk.getX(), chunk.getZ()));
            packet.getMultiBlockChangeInfoArrays().write(0, blocksToChange.toArray(new MultiBlockChangeInfo[0]));
            protocolManager.sendServerPacket(player, packet);
        }
    }
}
