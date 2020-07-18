package org.sijbesma.bp.bpbrewingstandlib.event.listeners;

import org.bukkit.event.Listener;

import static org.sijbesma.bp.utils.DebugLogger.debug;

import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.BrewerInventory;
import org.sijbesma.bp.bpbrewingstandlib.event.handlers.BrewerInventoryEventHandler;


public class InventoryEventListener implements Listener{

	private BrewerInventoryEventHandler brewerInventoryEventHandler;

	public InventoryEventListener(BrewerInventoryEventHandler brewerInventoryEventHandler) {
		this.brewerInventoryEventHandler = brewerInventoryEventHandler;
	}

	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent event) {
		// InventoryType is null if the location clicked is outside the inventory screen.
		InventoryType inventoryType = event.getInventory().getType();
		InventoryType inventoryPartType = event.getClickedInventory() != null ? event.getClickedInventory().getType(): null;
		ClickType clickType = event.getClick();
		//debug info
		debug("onInventoryClickEvent",true);
		debug("Inventory Type     : " + inventoryType);
		debug("Inventory part Type: " + (inventoryPartType != null ? inventoryPartType : "OUTSIDE"));
		debug("Click Type         : " + clickType);
		//
		//if the open inventory is a brewing stand
		if(inventoryType.equals(InventoryType.BREWING)) {
			//determine click type
			debug("");
			debug("Switch Case (clickType):");
			switch(clickType) {
			case SHIFT_LEFT:
				debug("Case SHIFT_LEFT");
				debug("Falling through to case is SHIFT_RIGHT");
			case SHIFT_RIGHT:
				debug("Case SHIFT_RIGHT");
				//player is shift clicking and the inventory is the player inventory
				if(inventoryPartType != null && inventoryPartType.equals(InventoryType.PLAYER)) {
					debug("Inventory Part Type is PLAYER");
					brewerInventoryEventHandler.onPlayerShiftClick(event);
				}
				break;
			case LEFT:
				debug("Case LEFT");
				//player is left clicking and the inventory is the brewing stand inventory
				if(inventoryPartType != null && inventoryPartType.equals(InventoryType.BREWING)) {
					debug("Inventory Part Type is BREWING");
					brewerInventoryEventHandler.onPlayerLeftClick(event);
				}
				break;
			case RIGHT:
				debug("Case RIGHT");
				//player is right clicking and the inventory is the brewing stand inventory
				if(inventoryPartType != null && inventoryPartType.equals(InventoryType.BREWING)) {
					debug("Inventory Part Type is BREWING");
					brewerInventoryEventHandler.onPlayerRightClick(event);
				}
				break;
			default:
				debug("Default");
				break;
			}
		}
		debug("End of Event",true);
		debug("");
	}
	
	
	@EventHandler
	public void onInventoryMoveItemEvent(InventoryMoveItemEvent event) {
		debug("onInventoryMoveItemEvent",true);
		if (!(event.getDestination() instanceof BrewerInventory)) {
			debug("Destination: Not Brewing Stand");
			return;
		}
		if (!event.getSource().getType().equals(InventoryType.HOPPER)) {
			debug("Source: Not Hopper");
			return;
		}
		debug("Destination    : Brewing Stand");
		debug("Source         : Hopper");
		int brewingStandY = event.getDestination().getLocation().getBlockY();
		debug("Brewing Stand Y: "+brewingStandY);
		int hopperY = event.getSource().getLocation().getBlockY();
		debug("Hopper Y       : "+hopperY);
		if(hopperY > brewingStandY) {
			debug("Hopper above Brewing Stand");
			brewerInventoryEventHandler.onTopHopperMoveItemEvent(event);
		}else {
			debug("Hopper same level as Brewing Stand");
			brewerInventoryEventHandler.onSideHopperMoveItemEvent(event);
		}
		debug("End of Event",true);
		debug("");
	}
	
	
	
	
}
