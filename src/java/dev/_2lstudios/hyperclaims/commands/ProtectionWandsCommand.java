package dev._2lstudios.hyperclaims.commands;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;

import dev._2lstudios.hyperclaims.player.ProtectionPlayerManager;
import dev._2lstudios.worldsentinel.region.RegionManager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.command.CommandExecutor;

public class ProtectionWandsCommand implements CommandExecutor {
    private final Plugin plugin;
    private final ProtectionPlayerManager pPlayerManager;
    private final ItemStack wandItemStack;
    private final RegionManager regionManager;
    private final String helpMessage;

    public ProtectionWandsCommand(final Plugin plugin, final ProtectionPlayerManager pPlayerManager,
            final ItemStack wandItemStack, final RegionManager regionManager) {
        final String lineSeparator = System.lineSeparator();
        this.plugin = plugin;
        this.pPlayerManager = pPlayerManager;
        this.wandItemStack = wandItemStack;
        this.regionManager = regionManager;
        this.helpMessage = ChatColor.translateAlternateColorCodes('&',
                "&aComandos de ProtectionWands:" + lineSeparator
                        + "&e/%label% info [region] &7- &bMuestra informacion de la region ingresada!" + lineSeparator
                        + "&e/%label% map &7- &bMuestra bordes de las protecciones!" + lineSeparator
                        + "&e/%label% wand &7- &bObten una azada de claim!" + lineSeparator
                        + "&e/%label% unclaim &7- &bRemueve el claim donde estas parado!" + lineSeparator
                        + "&e/%label% transfer <jugador> &7- &bTransfiere el claim a un jugador!" + lineSeparator
                        + "&e/%label% add <jugador> &7- &bAgrega un jugador a tu claim!" + lineSeparator
                        + "&e/%label% remove <jugador> &7- &bRemueve un jugador de tu claim!" + lineSeparator
                        + "&e/%label% regions [jugador] &7- &bObserva tu lista de claims o de otros!");
    }

    public boolean onCommand(final CommandSender commandSender, final Command command, final String label,
            final String[] args) {
        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, () -> {
            if (commandSender instanceof Player) {
                final Player player = (Player) commandSender;
                if (args.length > 0) {
                    final Server server = this.plugin.getServer();
                    if (args[0].equals("map")) {
                        new ProtectionWandsMapCommand(this.pPlayerManager, player);
                    } else if (args[0].equals("wand")) {
                        new ProtectionWandsWandCommand(this.wandItemStack, player);
                    } else if (args[0].equals("transfer")) {
                        new ProtectionWandsTransferCommand(server, this.regionManager, args, label, player);
                    } else if (args[0].equals("add")) {
                        new ProtectionWandsAddCommand(server, this.regionManager, args, label, player);
                    } else if (args[0].equals("remove")) {
                        new ProtectionWandsRemoveCommand(this.regionManager, args, label, player);
                    } else if (args[0].equals("unclaim")) {
                        new ProtectionWandsUnclaimCommand(this.regionManager, player);
                    } else if (args[0].equals("info")) {
                        new ProtectionWandsInfoCommand(args, this.regionManager, player);
                    } else if (args[0].equals("regions")) {
                        new ProtectionWandsRegionsCommand(server, this.regionManager, this.pPlayerManager, args,
                                player);
                    } else {
                        commandSender.sendMessage(
                                ChatColor.RED + "Argumento invalido! Usa /" + label + " para ver comandos!");
                    }
                } else {
                    player.sendMessage(this.helpMessage.replace("%label%", label));
                }
            } else {
                commandSender.sendMessage(ChatColor.RED + "Este comando no puede ser usado desde la consola!");
            }
            return;
        });
        return true;
    }
}
