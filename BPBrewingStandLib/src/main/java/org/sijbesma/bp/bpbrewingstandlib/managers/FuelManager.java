package org.sijbesma.bp.bpbrewingstandlib.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import org.bukkit.inventory.ItemStack;
import org.sijbesma.bp.bpbrewingstandlib.containers.FuelContainer;
import org.sijbesma.bp.bpbrewingstandlib.containers.FuelItemStack;

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
			ArrayList<FuelItemStack>fuelItemStacks = itemFuelMap.get(itemSingle);
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
		ItemStack itemStack = (ItemStack) fuelItemStack;
		fuelItemStackSet.add(fuelItemStack);
		itemStack.setAmount(1);
		validInFuelSlotSet.add(itemStack);
		if(itemFuelMap.containsKey(itemStack)) {
			LinkedList<FuelItemStack> fuelItemStackList = itemFuelMap.get(itemStack);
			boolean insertedBefore = false;
			ListIterator<FuelItemStack> it = fuelItemStackList.listIterator();
			while(it.hasNext()) {
				FuelItemStack currentIndex = it.next();
				if(currentIndex.getAmount() < fuelItemStack.getAmount()) {
					it.add(fuelItemStack);
					insertedBefore = true;
					break;
				}
			}
			if(!insertedBefore) {
				fuelItemStackList.addLast(fuelItemStack);
			}
		}
		
	}
		
		
		
//		
//		if(itemStackContainerMap.containsKey(fuelItem)) {
//			ArrayList<FuelContainer>fuelContainerList = itemFuelContainerMap.get(fuelItem);
//			boolean insertedBefore = false;
//			for(int i = 0; i < fuelContainerList.size();i++) {
//				if(fuelContainerList.get(i).getFuelItemStack().getAmount() < fuelContainer.getFuelItemStack().getAmount()) {
//					fuelContainerList.add(i, fuelContainer);
//					insertedBefore = true;
//					break;
//				}
//			}
//			if(!insertedBefore) {
//				fuelContainerList.add(fuelContainer);
//			}
//		}else {
//			itemFuelContainerMap.put(fuelItem, new ArrayList<FuelContainer>());
//			itemFuelContainerMap.get(fuelItem).add(fuelContainer);
//		}
//	}

	/**
	 * removes a FuelContainer from the registry of possible brewing stand fuel sources
	 * @param fuelContainer the FuelContainer to remove
	 */
	public static void removeFuelItemStack(FuelContainer fuelContainer) {
		debug("removeFuel",true);
		fuelContainerSet.remove(fuelContainer);
		ItemStack singleItem = fuelContainer.getFuelItemStack();
		singleItem.setAmount(1);
		ArrayList<FuelContainer> fuelContainerList = itemFuelContainerMap.get(singleItem);
		if(fuelContainerList.size() > 0) {
			fuelContainerList.remove(fuelContainer);
		}else {
			itemFuelContainerMap.remove(singleItem);
		}
		repopulateValidInFuelSlotSet();
	}

	private static void repopulateValidInFuelSlotSet() {
		debug("repopulateValidInFuelSlotSet",true);
		validInFuelSlotSet.clear();
		for (FuelContainer fuelContainer : fuelContainerSet) {
			ItemStack fuelItem = fuelContainer.getFuelItemStack();
			fuelItem.setAmount(1);
			validInFuelSlotSet.add(fuelItem);
		}
	}

}
