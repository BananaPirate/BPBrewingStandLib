package org.sijbesma.bp.bpbrewingstandlib.containers;

import org.bukkit.inventory.ItemStack;

public class RecipeContainer {
	
	private ItemStack ingredient;
	private ItemStack bottle;
	private ItemStack result;
	private int fuelPower;
	private boolean strictBottleQuantities;
	
	public RecipeContainer(ItemStack ingredient, ItemStack bottle, ItemStack result, int fuelPower, boolean strictBottleQuantities) {
		this.ingredient = ingredient;
		this.bottle = bottle;
		this.result = result;
		this.fuelPower = fuelPower;
		this.strictBottleQuantities = strictBottleQuantities;
	}
	
	public RecipeContainer(ItemStack ingredient, ItemStack bottle, ItemStack result, int fuelPower) {
		this(ingredient,bottle,result,fuelPower,false);
	}
	
	public RecipeContainer(ItemStack ingredient, ItemStack bottle, ItemStack result) {
		this(ingredient,bottle,result,1,false);
	}
	
	public RecipeContainer() {
		//empty for construction via setters.
	}

	public ItemStack getIngredient() {
		return ingredient;
	}

	public void setIngredient(ItemStack ingredient) {
		this.ingredient = ingredient;
	}

	public ItemStack getBottle() {
		return bottle;
	}

	public void setBottle(ItemStack bottle) {
		this.bottle = bottle;
	}

	public ItemStack getResult() {
		return result;
	}

	public void setResult(ItemStack result) {
		this.result = result;
	}

	public int getFuelPower() {
		return fuelPower;
	}

	public void setFuelPower(int fuelPower) {
		this.fuelPower = fuelPower;
	}

	public boolean isStrictBottleQuantities() {
		return strictBottleQuantities;
	}

	public void setStrictBottleQuantities(boolean strictBottleQuantities) {
		this.strictBottleQuantities = strictBottleQuantities;
	}
	
	
	
}
