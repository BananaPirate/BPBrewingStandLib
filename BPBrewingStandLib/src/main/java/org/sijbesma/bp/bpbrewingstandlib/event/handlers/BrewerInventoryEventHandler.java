package org.sijbesma.bp.bpbrewingstandlib.event.handlers;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.ItemStack;
import org.sijbesma.bp.bpbrewingstandlib.managers.FuelManager;

import static org.sijbesma.bp.utils.DebugLogger.*;


public class BrewerInventoryEventHandler {

	
	public void onPlayerLeftClick(InventoryClickEvent event) {
		debug("onPlayerLeftClick",true);
	}
	
	public void onPlayerRightClick(InventoryClickEvent event) {
		debug("onPlayerRightClick",true);
	}
	
	public void onPlayerShiftClick(InventoryClickEvent event) {
		debug("onPlayerShiftClick",true);
		ItemStack currentItem = event.getCurrentItem();
		boolean isValidFuelSlotItem = FuelManager.isValidFuelSlotItem(currentItem);
		debug("Valid Fuelslot Item: "+isValidFuelSlotItem);		
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
