package org.sijbesma.bp.bpbrewingstandlib.containers;

import org.bukkit.inventory.ItemStack;

public class FuelContainer {

	private ItemStack fuelItemStack;
	private int fuelPower;
	
	public FuelContainer(ItemStack fuelItemStack, int fuelPower) {
		this.fuelItemStack = fuelItemStack.clone();
		this.fuelPower = fuelPower;
	}
	
	public FuelContainer(ItemStack fuelItemStack) {
		this(fuelItemStack,1);
	}
	
	public FuelContainer() {
		//for construction via setters
	}

	public ItemStack getFuelItemStack() {
		return fuelItemStack.clone();
	}

	public void setFuelItemStack(ItemStack fuelItemStack) {
		this.fuelItemStack = fuelItemStack;
	}

	public int getFuelPower() {
		return fuelPower;
	}

	public void setFuelPower(int fuelPower) {
		this.fuelPower = fuelPower;
	}
	
	public boolean isSimilar(ItemStack item) {
		return fuelItemStack.isSimilar(item);
	}
	
}
