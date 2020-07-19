package org.sijbesma.bp.bpbrewingstandlib.containers;

import org.bukkit.inventory.ItemStack;

public class RecipeContainer {
	
	private ItemStack ingredient;
	private ItemStack bottle;
	private ItemStack result;
	private int fuelUsage;
	private int maxBottleStackSize;
	private int maxResultStackSize;
	
	public RecipeContainer(ItemStack ingredient, ItemStack bottle, ItemStack result, int fuelUsage, int maxBottleStackSize, int maxResultStackSize) {
		this.ingredient = ingredient;
		this.bottle = bottle;
		this.result = result;
		this.fuelUsage = fuelUsage;
		this.maxBottleStackSize = maxBottleStackSize;
		this.maxResultStackSize = maxResultStackSize;
	}
	
	public RecipeContainer(ItemStack ingredient, ItemStack bottle, ItemStack result, int fuelUsage, int maxBottleStackSize) {
		this(ingredient,bottle,result,fuelUsage,maxBottleStackSize,-1);
	}
	
	public RecipeContainer(ItemStack ingredient, ItemStack bottle, ItemStack result, int fuelUsage) {
		this(ingredient,bottle,result,fuelUsage,-1,-1);
	}
	
	public RecipeContainer(ItemStack ingredient, ItemStack bottle, ItemStack result) {
		this(ingredient,bottle,result,1,-1,-1);
	}
	
	public RecipeContainer() {
		//empty for construction via setters.
	}

	public  RecipeContainer clone() {
		ItemStack ingredient = this.ingredient.clone();
		ItemStack bottle = this.bottle.clone();
		ItemStack result = this.result.clone();
		int fuelUsage = this.fuelUsage;
		int maxBottleStackSize = this.maxBottleStackSize;
		int maxResultStackSize = this.maxResultStackSize;
		return new RecipeContainer(ingredient,bottle,result,fuelUsage,maxBottleStackSize,maxResultStackSize);
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

	public int getFuelUsage() {
		return fuelUsage;
	}

	public void setFuelUsage(int fuelUsage) {
		this.fuelUsage = fuelUsage;
	}

	public int getMaxBottleStackSize() {
		return maxBottleStackSize;
	}

	public void setMaxBottleStackSize(int maxBottleStackSize) {
		this.maxBottleStackSize = maxBottleStackSize;
	}

	public int getMaxResultStackSize() {
		return maxResultStackSize;
	}

	public void setMaxResultStackSize(int maxResultStackSize) {
		this.maxResultStackSize = maxResultStackSize;
	}


	
	
}
