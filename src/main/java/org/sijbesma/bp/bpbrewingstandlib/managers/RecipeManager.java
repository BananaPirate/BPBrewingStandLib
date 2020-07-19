package org.sijbesma.bp.bpbrewingstandlib.managers;

import java.util.HashSet;
import java.util.HashMap;
import java.util.LinkedList;

import org.bukkit.inventory.ItemStack;
import org.sijbesma.bp.bpbrewingstandlib.containers.RecipeContainer;

public class RecipeManager {
	//slot validation data structures
	private static HashSet<ItemStack> validIngredientSlotSet = new HashSet<ItemStack>();
	private static HashSet<ItemStack> validBottleSlotSet = new HashSet<ItemStack>();
	//recipe registry data structures.
	private static HashSet<RecipeContainer> recipeSet = new HashSet<RecipeContainer>();
	private static HashMap<ItemStack,LinkedList<RecipeContainer>> ingredientRecipeMap = new HashMap<ItemStack,LinkedList<RecipeContainer>>();
	private static HashMap<ItemStack,LinkedList<RecipeContainer>> bottleRecipeMap = new HashMap<ItemStack,LinkedList<RecipeContainer>>();
	//
	
	public static boolean isValidIngredientSlot(ItemStack item) {
		item = item.clone();
		item.setAmount(1);
		return validIngredientSlotSet.contains(item);
	}
	
	public static boolean isValidBottleSlot(ItemStack item) {
		item = item.clone();
		item.setAmount(1);
		return validBottleSlotSet.contains(item);
	}
	
	public static void addRecipe(RecipeContainer recipe) {
		recipe = recipe.clone();
		//add to the recipeSet
		recipeSet.add(recipe);
		//
		ItemStack singleIngredient = recipe.getIngredient();
		singleIngredient.setAmount(1);
		validIngredientSlotSet.add(singleIngredient);
		if(ingredientRecipeMap.containsKey(singleIngredient)) {
			ingredientRecipeMap.get(singleIngredient).add(recipe);
		} else {
			ingredientRecipeMap.put(singleIngredient,new LinkedList<RecipeContainer>());
			ingredientRecipeMap.get(singleIngredient).add(recipe);
		}
		//
		ItemStack singleBottle = recipe.getBottle();
		singleBottle.setAmount(1);
		validBottleSlotSet.add(singleBottle);
		if(bottleRecipeMap.containsKey(singleBottle)) {
			bottleRecipeMap.get(singleBottle).add(recipe);
		}else {
			bottleRecipeMap.put(singleBottle, new LinkedList<RecipeContainer>());
			bottleRecipeMap.get(singleBottle).add(recipe);
		}
		//
		ItemStack singleResult = recipe.getResult();
		singleResult.setAmount(1);
		validBottleSlotSet.add(singleResult);
	}
	
	public static void removeRecipeContainer(RecipeContainer recipe) {
		recipeSet.remove(recipe);
		//
		ItemStack singleIngredient = recipe.getIngredient();
		singleIngredient.setAmount(1);
		LinkedList<RecipeContainer>ingredientRecipeList = ingredientRecipeMap.get(singleIngredient);
		ingredientRecipeList.remove(recipe);
		if(ingredientRecipeList.isEmpty()) {
			ingredientRecipeMap.remove(singleIngredient);
		}
		//
		ItemStack singleBottle = recipe.getBottle();
		singleBottle.setAmount(1);
		LinkedList<RecipeContainer>bottleRecipeList = bottleRecipeMap.get(singleBottle);
		bottleRecipeList.remove(recipe);
		if(bottleRecipeList.isEmpty()) {
			bottleRecipeMap.remove(singleBottle);
		}
		//
		repopulateValidSlotSets();
	}
	
	private static void repopulateValidSlotSets() {
		validIngredientSlotSet.clear();
		validBottleSlotSet.clear();
		for(RecipeContainer recipe: recipeSet) {
			ItemStack singleIngredient = recipe.getIngredient();
			singleIngredient.setAmount(1);
			validIngredientSlotSet.add(singleIngredient);
			//
			ItemStack singleBottle = recipe.getBottle();
			singleBottle.setAmount(1);
			validBottleSlotSet.add(singleBottle);
			//
			ItemStack singleResult = recipe.getResult();
			singleResult.setAmount(1);
			validBottleSlotSet.add(singleResult);
		}
	}
	

	
}
