package de.doppelkool.knockit.service;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Class Description
 *
 * @author doppelkool | github.com/doppelkool
 */
public class InventoryService {

	private static InventoryService instance = null;

	private static final HashMap<ItemStack, Integer> defaultInv = new HashMap<>(Map.of(
			ItemStacks.knockStick, 0
	));

	public void loadDefaultInventory(Player pl) {
		pl.getInventory().clear();

		defaultInv.forEach((stack, slot) -> {
			pl.getInventory().setItem(slot, stack);
		});
	}

	public static InventoryService getInstance() {
		if(instance == null) {
			instance = new InventoryService();
		}
		return instance;
	}

	private class ItemStacks {
		public static ItemStack knockStick = new ItemStack(Material.STICK);

		static {
			knockStick.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
		}

	}

}
