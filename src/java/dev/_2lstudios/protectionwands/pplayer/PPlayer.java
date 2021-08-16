package dev._2lstudios.protectionwands.pplayer;

import java.util.Iterator;
import org.json.simple.JSONObject;
import dev._2lstudios.protectionwands.utils.JSONUtil;
import java.util.HashSet;
import java.util.HashMap;
import org.bukkit.entity.Player;
import java.util.UUID;
import java.util.Collection;
import org.bukkit.Location;
import java.util.Map;

public class PPlayer {
    private final Map<String, Location> pillarMap;
    private final Collection<String> regions;
    private final UUID uuid;
    private final String name;
    private Location position1;
    private Location position2;
    private boolean map;

    public PPlayer(final Player player) {
        this.pillarMap = new HashMap<String, Location>();
        this.regions = new HashSet<String>();
        this.uuid = player.getUniqueId();
        this.name = player.getName();
        this.position1 = null;
        this.position2 = null;
        this.map = false;
    }

    public void loadData() {
        final String path = "%datafolder%/" + this.uuid.toString() + ".json";
        final JSONObject playerData = JSONUtil.getJSON(path);
        if (playerData.containsKey((Object) "regions")) {
            final Object regionsSection = playerData.get((Object) "regions");
            if (regionsSection instanceof Iterable) {
                for (final Object regionsObject : (Iterable) regionsSection) {
                    if (regionsObject instanceof String) {
                        final String regionName = (String) regionsObject;
                        this.regions.add(regionName);
                    }
                }
            }
        }
    }

    public void saveData() {
        final String path = "%datafolder%/" + this.uuid.toString() + ".json";
        if (this.regions.isEmpty()) {
            JSONUtil.deleteJSON(path);
        } else {
            final JSONObject playerData = new JSONObject();
            playerData.put((Object) "regions", (Object) this.regions);
            JSONUtil.saveJSON(path, playerData);
        }
    }

    public boolean isMapEnabled() {
        return this.map;
    }

    public void setMapEnabled(final boolean map) {
        this.map = map;
    }

    public Location getPosition1() {
        return this.position1;
    }

    public void setPosition1(final Location location) {
        if (location == null) {
            this.position1 = null;
        } else {
            (this.position1 = location.clone()).setY((double) location.getWorld().getMaxHeight());
        }
    }

    public Location getPosition2() {
        return this.position2;
    }

    public void setPosition2(final Location location) {
        if (location == null) {
            this.position1 = null;
        } else {
            (this.position2 = location.clone()).setY(0.0);
        }
    }

    public Collection<String> getRegions() {
        return this.regions;
    }

    public void addRegion(final String region) {
        this.regions.add(region);
    }

    public void removeRegion(final String region) {
        this.regions.remove(region);
    }

    public Collection<Location> getPillarLocations() {
        return this.pillarMap.values();
    }

    public void addPillarLocation(final String uuid, final Location location) {
        this.pillarMap.put(uuid, location);
    }

    public boolean hasPillar(final String uuid) {
        return this.pillarMap.containsKey(uuid);
    }

    public void clearPillarLocations() {
        this.pillarMap.clear();
    }

    public void fixPositions() {
        if (this.position1 != null && this.position1.getX() > this.position2.getX()) {
            this.position1.add(0.99, 0.0, 0.0);
        } else {
            this.position2.add(0.99, 0.0, 0.0);
        }
        if (this.position1 != null && this.position1.getZ() > this.position2.getZ()) {
            this.position1.add(0.0, 0.0, 0.99);
        } else {
            this.position2.add(0.0, 0.0, 0.99);
        }
    }

    public String getName() {
        return this.name;
    }
}
