package org.sijbesma.bp.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MoveItem {
	
	public static void cursorToSlot(Player player, Inventory inventory, int slot) {
		ItemStack itemOnCursor = player.getItemOnCursor().clone();
		ItemStack itemInSlot = inventory.getItem(slot).clone();
		
		if(itemOnCursor.isSimilar(itemInSlot)) {
			//add items from cursor to stack
			addItemFromCursor(player,inventory,slot);
		} else {
			//swap item and cursor
			swapItemFromCursor(player, inventory, slot);
		}
	}
	
	public static void ItemStacktoSlot(ItemStack item, Inventory inventory, int slot) {
		inventory.setItem(slot, item.clone());
	}
	

	private static void addItemFromCursor(Player player, Inventory inventory, int slot) {
		ItemStack itemOnCursor = player.getItemOnCursor().clone();
		ItemStack itemInSlot = inventory.getItem(slot).clone();
		int cursorAmount = itemOnCursor.getAmount();
		int slotAmount = itemInSlot.getAmount();
		int spaceInSlot = itemInSlot.getMaxStackSize() - itemInSlot.getAmount();
		if(spaceInSlot > 0) {
			if(spaceInSlot >= cursorAmount) {
				itemInSlot.setAmount(slotAmount + cursorAmount);
				itemOnCursor.setType(Material.AIR);
				itemOnCursor.setAmount(0);
			} else {
				itemInSlot.setAmount(slotAmount + spaceInSlot);
				itemOnCursor.setAmount(cursorAmount - spaceInSlot);
			}
		}
		player.setItemOnCursor(itemOnCursor);
		inventory.setItem(slot, itemInSlot);	
	}
	 
	private static void swapItemFromCursor(Player player, Inventory inventory, int slot) {
		ItemStack itemOnCursor = player.getItemOnCursor().clone();
		ItemStack itemInSlot = inventory.getItem(slot).clone();
		player.setItemOnCursor(itemInSlot);
		inventory.setItem(slot, itemOnCursor);
	}
}
