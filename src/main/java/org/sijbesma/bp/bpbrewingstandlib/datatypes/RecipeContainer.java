package org.sijbesma.bp.bpbrewingstandlib.datatypes;

import org.bukkit.inventory.ItemStack;

public class RecipeContainer {
	
	private ItemStack ingredient;
	private ItemStack bottle;
	private ItemStack result;
	private int brewingTime;
	private int fuelUsage;
	private int maxBottleStackSize;
	private int maxResultStackSize;
	
	public RecipeContainer(ItemStack ingredient, ItemStack bottle, ItemStack result,int brewingTime, int fuelUsage, int maxBottleStackSize, int maxResultStackSize) {
		this.ingredient = ingredient;
		this.bottle = bottle;
		this.result = result;
		this.brewingTime = brewingTime;
		this.fuelUsage = fuelUsage;
		this.maxBottleStackSize = maxBottleStackSize;
		this.maxResultStackSize = maxResultStackSize;
	}
	
	public RecipeContainer(ItemStack ingredient, ItemStack bottle, ItemStack result, int brewingTime, int fuelUsage, int maxBottleStackSize) {
		this(ingredient,bottle,result,brewingTime,fuelUsage,maxBottleStackSize,-1);
	}
	
	public RecipeContainer(ItemStack ingredient, ItemStack bottle, ItemStack result, int brewingTime, int fuelUsage) {
		this(ingredient,bottle,result,brewingTime,fuelUsage,-1,-1);
	}
	
	public RecipeContainer(ItemStack ingredient, ItemStack bottle, ItemStack result, int brewingTime) {
		this(ingredient,bottle,result,brewingTime,1,-1,-1);
	}
	
	public RecipeContainer(ItemStack ingredient, ItemStack bottle, ItemStack result) {
		this(ingredient,bottle,result,400,1,-1,-1);
	}
	
	public RecipeContainer() {
		//empty for construction via setters.
	}

	/**
	 * This is the clone constructor for RecipeContainer, it clones both the RecipeContainer itself as well as the ItemStacks and values it contains.
	 * As such this is a deep copy.
	 */
	public RecipeContainer clone() {
		ItemStack ingredient = this.ingredient.clone();
		ItemStack bottle = this.bottle.clone();
		ItemStack result = this.result.clone();
		int fuelUsage = this.fuelUsage;
		int maxBottleStackSize = this.maxBottleStackSize;
		int maxResultStackSize = this.maxResultStackSize;
		return new RecipeContainer(ingredient,bottle,result,fuelUsage,maxBottleStackSize,maxResultStackSize);
	}
	
	/**
	 * This returns a clone of the ingredient ItemStack, if you wish to alter the ingredient use the setter.
	 * @return clone of the ingredient ItemStack
	 */
	public ItemStack getIngredient() {
		return ingredient.clone();
	}

	public void setIngredient(ItemStack ingredient) {
		this.ingredient = ingredient;
	}
	
	/**
	 * This returns a clone of the bottle ItemStack, if you wish to alter the bottle use the setter.
	 * @return clone of the bottle ItemStack
	 */
	public ItemStack getBottle() {
		return bottle.clone();
	}

	public void setBottle(ItemStack bottle) {
		this.bottle = bottle;
	}

	/**
	 * This returns a clone of the result ItemStack, if you wish to alter the result use the setter.
	 * @return clone of the result ItemStack
	 */
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

	public int getBrewingTime() {
		return brewingTime;
	}

	public void setBrewingTime(int brewingTime) {
		this.brewingTime = brewingTime;
	}


	
	
}
