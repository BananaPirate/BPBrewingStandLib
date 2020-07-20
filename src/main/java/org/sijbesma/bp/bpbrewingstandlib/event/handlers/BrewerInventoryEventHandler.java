package org.sijbesma.bp.bpbrewingstandlib.event.handlers;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.sijbesma.bp.bpbrewingstandlib.managers.FuelManager;
import org.sijbesma.bp.bpbrewingstandlib.managers.RecipeManager;
import org.sijbesma.bp.bpbrewingstandlib.datatypes.SlotType;

import static org.sijbesma.bp.utils.DebugLogger.*;
import org.sijbesma.bp.utils.MoveItem;


public class BrewerInventoryEventHandler {
	private final int[] slotInsertOrder = new int[] {4,3,0,1,2};

	
	
	public void onPlayerLeftClick(InventoryClickEvent event) {
		debug("onPlayerLeftClick", true);
		Inventory inventory = event.getInventory();
		int slot = event.getSlot();
		debug("Clicked Slot: " + slot);
	}

	public void onPlayerRightClick(InventoryClickEvent event) {
		debug("onPlayerRightClick", true);
	}

	public void onPlayerShiftClick(InventoryClickEvent event) {
		debug("onPlayerShiftClick", true);
		boolean changedInventory = false;
		Inventory inventory = event.getInventory();
		ItemStack currentItem = event.getCurrentItem();
		int currentSlot = event.getRawSlot();

		debug("Current Item              : " + currentItem);
		for(int slot : slotInsertOrder) {
			SlotType slotType = SlotType.valueOf(slot);
			debug("Slot    : "+slot);
			debug("SlotType: "+slotType);
			boolean attemptMove = false;
			switch(slotType){
			case BOTTLE:
				if(RecipeManager.isValidBottleSlot(currentItem)) {
					attemptMove = true;
				}
				break;
			case INGREDIENT:
				if(RecipeManager.isValidIngredientSlot(currentItem)) {
					attemptMove = true;
				}
				break;
			case FUEL:
				if(FuelManager.isValidFuelSlotItem(currentItem)) {
					attemptMove = true;
				}
				break;
			default:
				debug("slottype switch case defaulted, this really shouldn't be possible");
				break;
			}
			debug("Attempt Move: "+attemptMove);
			if(attemptMove) {
				if(MoveItem.slotToSlot(currentSlot, slot, inventory)) {
					changedInventory = true;
					break;
				}
				debug("Move Failed");
			}
		}
		
		if (changedInventory) {
			debug("Updating inventory display for player viewing");
			for (HumanEntity viewer : event.getViewers()) {
				if (viewer instanceof Player) {
					((Player) viewer).updateInventory();
				}
			}
			event.setCancelled(true);
		}

	}

	public void onTopHopperMoveItemEvent(InventoryMoveItemEvent event) {
		debug("onTopHopperMoveItemEvent", true);
	}

	public void onSideHopperMoveItemEvent(InventoryMoveItemEvent event) {
		debug("onSideHopperMoveItemEvent", true);
	}
}
