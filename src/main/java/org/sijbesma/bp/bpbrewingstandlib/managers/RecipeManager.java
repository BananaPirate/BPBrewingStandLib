package org.sijbesma.bp.bpbrewingstandlib.managers;

import java.util.HashSet;
import java.util.HashMap;
import java.util.LinkedList;

import org.bukkit.inventory.ItemStack;
import org.sijbesma.bp.bpbrewingstandlib.containers.RecipeContainer;

public class RecipeManager {
	
	private static HashSet<ItemStack> validInIngredientSlotSet = new HashSet<ItemStack>();
	private static HashSet<ItemStack> validInBottleSlotSet = new HashSet<ItemStack>();
	
	private static HashSet<RecipeContainer> recipeSet = new HashSet<RecipeContainer>();
	private static HashMap<ItemStack,LinkedList<RecipeContainer>> ingredientRecipeMap = new HashMap<ItemStack,LinkedList<RecipeContainer>>();
	private static HashMap<ItemStack,LinkedList<RecipeContainer>> bottleRecipeMap = new HashMap<ItemStack,LinkedList<RecipeContainer>>();

	
	public static boolean isValidInIngredientSlot(ItemStack item) {
		item = item.clone();
		item.setAmount(1);
		return validInIngredientSlotSet.contains(item);
	}
	
	
	public static boolean isValidInBottleSlot(ItemStack item) {
		item = item.clone();
		item.setAmount(1);
		return validInBottleSlotSet.contains(item);
	}
	
	public static void addRecipeContainer(RecipeContainer recipe) {
		//add to the recipeSet
		recipeSet.add(recipe);
		//
		ItemStack ingredient = recipe.getIngredient();
		ItemStack singleIngredient = ingredient.clone();
		singleIngredient.setAmount(1);
		validInIngredientSlotSet.add(singleIngredient);
		if(ingredientRecipeMap.containsKey(singleIngredient)) {
			ingredientRecipeMap.get(singleIngredient).add(recipe);
		} else {
			ingredientRecipeMap.put(singleIngredient,new LinkedList<RecipeContainer>());
			ingredientRecipeMap.get(singleIngredient).add(recipe);
		}
		//
		ItemStack bottle = recipe.getBottle();
		ItemStack singleBottle = bottle.clone();
		bottle.setAmount(1);
		validInBottleSlotSet.add(singleBottle);
		if(bottleRecipeMap.containsKey(singleBottle)) {
			bottleRecipeMap.get(singleBottle).add(recipe);
		}else {
			bottleRecipeMap.put(singleBottle, new LinkedList<RecipeContainer>());
			bottleRecipeMap.get(singleBottle).add(recipe);
		}
	}
	
	public static void removeRecipeContainer(RecipeContainer recipe) {
		recipeSet.remove(recipe);
		//
		ItemStack ingredient = recipe.getIngredient();
		ItemStack singleIngredient = ingredient.clone();
		singleIngredient.setAmount(1);
		LinkedList<RecipeContainer>ingredientRecipeList = ingredientRecipeMap.get(singleIngredient);
		ingredientRecipeList.remove(recipe);
		if(ingredientRecipeList.isEmpty()) {
			ingredientRecipeMap.remove(singleIngredient);
		}
		//
		ItemStack bottle = recipe.getBottle();
		ItemStack singleBottle = bottle.clone();
		singleBottle.setAmount(1);
		LinkedList<RecipeContainer>bottleRecipeList = bottleRecipeMap.get(singleBottle);
		bottleRecipeList.remove(recipe);
		if(bottleRecipeList.isEmpty()) {
			bottleRecipeMap.remove(singleBottle);
		}
		//
		repopulateValidInSlotSets();
	}
	
	private static void repopulateValidInSlotSets() {
		validInIngredientSlotSet.clear();
		validInBottleSlotSet.clear();
		for(RecipeContainer recipe: recipeSet) {
			ItemStack ingredient = recipe.getIngredient();
			ItemStack singleIngredient = ingredient.clone();
			singleIngredient.setAmount(1);
			validInIngredientSlotSet.add(singleIngredient);
			ItemStack bottle = recipe.getBottle();
			ItemStack singleBottle = bottle.clone();
			bottle.setAmount(1);
			validInBottleSlotSet.add(singleBottle);
		}
	}
	

	
}
