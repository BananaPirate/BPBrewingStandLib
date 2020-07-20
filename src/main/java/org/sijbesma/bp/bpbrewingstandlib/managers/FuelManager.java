package org.sijbesma.bp.bpbrewingstandlib.managers;


import java.util.HashMap;
import java.util.HashSet;

import java.util.LinkedList;
import java.util.ListIterator;

import org.bukkit.inventory.ItemStack;
import org.sijbesma.bp.bpbrewingstandlib.datatypes.FuelItemStack;

import static org.sijbesma.bp.utils.DebugLogger.debug;



/**
 * This class contains the registry for all added fuel items.
 * methods are provided to interact with these stored fuels but are mostly used internally
 * @author JJS
 *
 */
public class FuelManager {

	private static HashSet<FuelItemStack> fuelItemStackSet = new HashSet<FuelItemStack>();
	private static HashSet<ItemStack> validInFuelSlotSet = new HashSet<ItemStack>();
	private static HashMap<ItemStack, LinkedList<FuelItemStack>> itemFuelMap= new HashMap<ItemStack, LinkedList<FuelItemStack>>();

	/**
	 * test whether the ItemStack is valid item inside a brewing stand's fuel slot
	 * it does not consider vanilla Minecraft items in this test, only those registered through the library.
	 * @param item The item to test
	 * @return true if it's a valid item in a brewing stand's fuel slot, false if invalid.
	 */
	public static boolean isValidFuelSlotItem(ItemStack item) {
		item = item.clone();
		item.setAmount(1);
		if (validInFuelSlotSet.contains(item)) {
			return true;
		}
		return false;
	}

	/**
	 * This function returns the fuel power of the ItemStack provided, it prefers the result with the largest amount that fits. 
	 * @param item the ItemStack for which to get the fuel power
	 * @return
	 */
	public static int getFuelPower(ItemStack item) {
		int amount = item.getAmount();
		ItemStack itemSingle = item.clone();
		itemSingle.setAmount(1);
		if(itemFuelMap.containsKey(itemSingle)) {
			LinkedList<FuelItemStack>fuelItemStacks = itemFuelMap.get(itemSingle);
			for(FuelItemStack fuelItemStack : fuelItemStacks) {
				int amountContainer = fuelItemStack.getAmount();
				if(amountContainer > 1 && amount >= amountContainer) {
					return fuelItemStack.getPower();
				}
				if(amountContainer == 1) {
					return fuelItemStack.getPower();
				}
			}	
		}
		return 0;
	}

	/**
	 * registers a FuelContainer as a possible brewing stand fuel source
	 * @param fuelContainer the FuelContainer to register as a fuel source
	 */
	public static void addFuelItemStack(FuelItemStack fuelItemStack) {
		fuelItemStack = fuelItemStack.clone();
		fuelItemStackSet.add(fuelItemStack);
		debug("Registering Item: "+fuelItemStack);
		//
		ItemStack itemStack = new ItemStack(fuelItemStack);
		itemStack.setAmount(1);
		debug("Registering Item as Valid in Fuel Slot as: "+itemStack);
		validInFuelSlotSet.add(itemStack);
		
		if(itemFuelMap.containsKey(itemStack)) {
			debug("Collision Detected");
			LinkedList<FuelItemStack> fuelItemStackList = itemFuelMap.get(itemStack);
			boolean insertedBefore = false;
			ListIterator<FuelItemStack> it = fuelItemStackList.listIterator();
			while(it.hasNext()) {
				FuelItemStack currentIndex = it.next();
				if(currentIndex.getAmount() < fuelItemStack.getAmount()) {
					debug("inserted before: "+currentIndex);
					it.previous();
					it.add(fuelItemStack);
					insertedBefore = true;
					break;
				}
			}
			if(!insertedBefore) {
				fuelItemStackList.addLast(fuelItemStack);
			}
			debug("Current Collision List: "+fuelItemStackList);
		}else {
			itemFuelMap.put(itemStack, new LinkedList<FuelItemStack>());
			itemFuelMap.get(itemStack).add(fuelItemStack);
		}
		
	}

	/**
	 * removes a FuelContainer from the registry of possible brewing stand fuel sources
	 * @param fuelContainer the FuelContainer to remove
	 */
	public static void removeFuelItemStack(FuelItemStack fuelItemStack) {
		debug("removeFuel",true);
		fuelItemStackSet.remove(fuelItemStack);
		ItemStack singleItem = (ItemStack) fuelItemStack.clone();
		singleItem.setAmount(1);
		LinkedList<FuelItemStack> fuelItemStackList = itemFuelMap.get(singleItem);
		if(fuelItemStackList.size() > 0) {
			fuelItemStackList.remove(fuelItemStack);
		}else {
			itemFuelMap.remove(singleItem);
		}
		repopulateValidInFuelSlotSet();
	}

	private static void repopulateValidInFuelSlotSet() {
		debug("repopulateValidInFuelSlotSet",true);
		validInFuelSlotSet.clear();
		for (FuelItemStack fuelItemStack : fuelItemStackSet) {
			ItemStack singleItem = fuelItemStack.clone();
			singleItem.setAmount(1);
			validInFuelSlotSet.add(singleItem);
		}
	}

}
