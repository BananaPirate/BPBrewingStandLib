package org.sijbesma.bp.bpbrewingstandlib.event.handlers;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.ItemStack;
import org.sijbesma.bp.bpbrewingstandlib.managers.FuelManager;
import org.sijbesma.bp.bpbrewingstandlib.managers.RecipeManager;

import static org.sijbesma.bp.utils.DebugLogger.*;


public class BrewerInventoryEventHandler {

	
	public void onPlayerLeftClick(InventoryClickEvent event) {
		debug("onPlayerLeftClick",true);
	}
	
	public void onPlayerRightClick(InventoryClickEvent event) {
		debug("onPlayerRightClick",true);
	}
	
	public void onPlayerShiftClick(InventoryClickEvent event) {
		//this is test code
		debug("onPlayerShiftClick",true);
		ItemStack currentItem = event.getCurrentItem();
		debug("Current Item              : "+currentItem);
		boolean isValidFuelSlotItem = FuelManager.isValidFuelSlotItem(currentItem);
		boolean isValidIngredientSlotItem = RecipeManager.isValidIngredientSlot(currentItem);
		boolean isValidBottleSlotItem = RecipeManager.isValidBottleSlot(currentItem);
		debug("Valid Ingredient Slot Item: "+isValidIngredientSlotItem);
		debug("Valid Bottle Slot Item    : "+isValidBottleSlotItem);
		debug("Max Bottle Slot Stack Size: "+RecipeManager.getMaxStackSizeAllowed(currentItem));
		debug("Valid Fuel Slot Item      : "+isValidFuelSlotItem);		
		if(isValidFuelSlotItem) {
			debug("Fuel Power         : "+FuelManager.getFuelPower(currentItem));
		}
	}
	
	public void onTopHopperMoveItemEvent(InventoryMoveItemEvent event) {
		debug("onTopHopperMoveItemEvent", true);
	}
	
	public void onSideHopperMoveItemEvent(InventoryMoveItemEvent event) {
		debug("onSideHopperMoveItemEvent", true);
	}
}
