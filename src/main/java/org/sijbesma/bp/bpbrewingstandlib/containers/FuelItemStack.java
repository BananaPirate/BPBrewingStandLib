package org.sijbesma.bp.bpbrewingstandlib.containers;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class FuelItemStack extends ItemStack{
	
	private int power;
	
	public FuelItemStack() {
		super();
		this.power = 1;
	}
	
	public FuelItemStack(ItemStack stack) {
		super(stack);
		this.power = 1;
	}
	
	public FuelItemStack(ItemStack stack, int power) {
		super(stack);
		this.power = power;
	}
	
	public FuelItemStack(Material type) {
		super(type);
		this.power = 1;
	}
	
	public FuelItemStack(Material type, int amount) {
		super(type,amount);
		this.power = 1;
	}
	
	public FuelItemStack(Material type, int amount, int power) {
		super(type,amount);
		this.power = power;
	}

	public boolean isSimilar(FuelItemStack stack) {
		boolean isSimilar = super.isSimilar(stack);
		if(!isSimilar) {
			return false;
		}
		if(stack.getPower() == this.power) {
			return true;
		}
		return false;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}
	
	public FuelItemStack clone() {
		ItemStack clone = super.clone();
		FuelItemStack fuelItemStack = new FuelItemStack(clone,this.getPower());
		return fuelItemStack;
	}
	
	
	
}
