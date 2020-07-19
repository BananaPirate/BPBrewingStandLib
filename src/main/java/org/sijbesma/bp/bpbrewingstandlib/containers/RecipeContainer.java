package org.sijbesma.bp.bpbrewingstandlib.containers;

import org.bukkit.inventory.ItemStack;

public class RecipeContainer {
	
	private ItemStack ingredient;
	private ItemStack bottle;
	private ItemStack result;
	private int fuelPower;
	private int maxBottleStackSize;
	
	public RecipeContainer(ItemStack ingredient, ItemStack bottle, ItemStack result, int fuelUsage, int maxBottleStackSize) {
		this.ingredient = ingredient;
		this.bottle = bottle;
		this.result = result;
		this.fuelPower = fuelUsage;
		this.maxBottleStackSize = maxBottleStackSize;
	}
	
	public RecipeContainer(ItemStack ingredient, ItemStack bottle, ItemStack result, int fuelPower) {
		this(ingredient,bottle,result,fuelPower,-1);
	}
	
	public RecipeContainer(ItemStack ingredient, ItemStack bottle, ItemStack result) {
		this(ingredient,bottle,result,1,-1);
	}
	
	public RecipeContainer() {
		//empty for construction via setters.
	}

	public ItemStack getIngredient() {
		return ingredient.clone();
	}

	public void setIngredient(ItemStack ingredient) {
		this.ingredient = ingredient;
	}

	public ItemStack getBottle() {
		return bottle.clone();
	}

	public void setBottle(ItemStack bottle) {
		this.bottle = bottle;
	}

	public ItemStack getResult() {
		return result.clone();
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

	public int getMaxBottleStackSize() {
		return maxBottleStackSize;
	}

	public void setMaxBottleStackSize(int maxBottleStackSize) {
		this.maxBottleStackSize = maxBottleStackSize;
	}


	
	
}
