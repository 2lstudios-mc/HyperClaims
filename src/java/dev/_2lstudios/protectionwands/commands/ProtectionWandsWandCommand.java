package dev._2lstudios.protectionwands.commands;

import org.bukkit.inventory.PlayerInventory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ProtectionWandsWandCommand {
    public ProtectionWandsWandCommand(final ItemStack wandItemStack, final Player player) {
        final PlayerInventory inventory = player.getInventory();
        final ItemStack itemStack = player.getItemInHand();
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            if (!inventory.contains(wandItemStack)) {
                player.setItemInHand(wandItemStack.clone());
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&aLas instrucciones para claimear se encuentran en la descripcion del item!\n&7(Abre el inventario y pasa el mouse por encima del item)"));
            } else {
                player.sendMessage(ChatColor.RED + "Ya tienes una claiming wand en tu inventario!");
            }
        } else {
            player.sendMessage(ChatColor.RED + "Necesitas la mano vacia para recibir la claiming wand!");
        }
    }
}
