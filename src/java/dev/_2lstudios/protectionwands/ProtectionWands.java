package dev._2lstudios.protectionwands;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.inventory.meta.ItemMeta;
import dev._2lstudios.worldsentinel.region.RegionManager;
import org.bukkit.plugin.PluginManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Server;
import dev._2lstudios.protectionwands.tasks.ProtectionWandsRunnable;
import org.bukkit.command.CommandExecutor;
import dev._2lstudios.protectionwands.commands.ProtectionWandsCommand;
import dev._2lstudios.protectionwands.listeners.PlayerQuitListener;
import dev._2lstudios.protectionwands.listeners.RegionEnterListener;
import org.bukkit.event.Listener;
import dev._2lstudios.protectionwands.listeners.PlayerInteractListener;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import java.util.ArrayList;
import org.bukkit.plugin.Plugin;
import dev._2lstudios.protectionwands.pplayer.PPlayerManager;
import dev._2lstudios.worldsentinel.WorldSentinel;
import dev._2lstudios.protectionwands.utils.JSONUtil;
import org.bukkit.plugin.java.JavaPlugin;

public class ProtectionWands extends JavaPlugin {
    public void onEnable() {
        JSONUtil.initialize(this.getDataFolder().toString());
        final Server server = this.getServer();
        final Economy economy = this.registerEconomy();
        final PluginManager pluginManager = server.getPluginManager();
        final WorldSentinel worldSentinel = WorldSentinel.getInstance();
        final RegionManager regionManager = worldSentinel.getRegionManager();
        final PPlayerManager pPlayerManager = new PPlayerManager((Plugin) this);
        final List<String> itemLore = new ArrayList<String>();

        // Añadido compatibilidad con el nuevo rename de los materiales - by Sammwy
        final Material legacyMaterial = Material.getMaterial("GOLD_AXE");
        final Material newMaterial = Material.getMaterial("GOLDEN_AXE");
        final ItemStack wandItemStack = new ItemStack(legacyMaterial == null ? newMaterial : legacyMaterial);

        final ItemMeta itemMeta = wandItemStack.getItemMeta();
        itemLore.add(ChatColor.translateAlternateColorCodes('&', "&aClick izq.&7 - Seleccionar posicion 1"));
        itemLore.add(ChatColor.translateAlternateColorCodes('&', "&aClick der.&7 - Seleccionar posicion 2"));
        itemLore.add(ChatColor.translateAlternateColorCodes('&', "&aShift + Click izq.&7 - Claimear seleccion"));
        itemLore.add(ChatColor.translateAlternateColorCodes('&', "&aShift + Click der.&7 - Limpiar seleccion"));
        itemLore.add(ChatColor.translateAlternateColorCodes('&', "&9Usa esta herramienta para claimear regiones!"));
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eHacha de Claim"));
        itemMeta.setLore(itemLore);
        wandItemStack.setItemMeta(itemMeta);
        pluginManager.registerEvents(
                (Listener) new PlayerInteractListener(economy, regionManager, pPlayerManager, wandItemStack),
                (Plugin) this);
        pluginManager.registerEvents((Listener) new RegionEnterListener(), (Plugin) this);
        pluginManager.registerEvents((Listener) new PlayerQuitListener(pPlayerManager), (Plugin) this);
        this.getCommand("ProtectionWands").setExecutor((CommandExecutor) new ProtectionWandsCommand((Plugin) this,
                pPlayerManager, wandItemStack, regionManager));
        server.getScheduler().runTaskTimer((Plugin) this,
                (Runnable) new ProtectionWandsRunnable(server, pPlayerManager, regionManager), 40L, 40L);
    }

    public void onDisable() {
        this.getServer().getScheduler().cancelTasks((Plugin) this);
    }

    public Economy registerEconomy() {
        final RegisteredServiceProvider<Economy> economyProvider = (RegisteredServiceProvider<Economy>) this.getServer()
                .getServicesManager().getRegistration((Class) Economy.class);
        if (economyProvider != null) {
            return (Economy) economyProvider.getProvider();
        }
        return null;
    }
}
