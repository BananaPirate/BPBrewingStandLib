package org.sijbesma.bp.bpbrewingstandlib.managers;

import java.util.HashSet;
import java.util.HashMap;
import java.util.LinkedList;

import org.bukkit.inventory.ItemStack;
import org.sijbesma.bp.bpbrewingstandlib.datatypes.RecipeContainer;
import static org.sijbesma.bp.utils.DebugLogger.debug;

public class RecipeManager {
	//slot validation data structures
	private static HashSet<ItemStack> validIngredientSlotSet = new HashSet<ItemStack>();
	private static HashSet<ItemStack> validBottleSlotSet = new HashSet<ItemStack>();
	//stack maximums for the bottle\result slot per item
	private static HashMap<ItemStack,Integer> bottleSlotStackMaxMap= new HashMap<ItemStack,Integer>();
	//recipe registry data structures.
	private static HashSet<RecipeContainer> recipeSet = new HashSet<RecipeContainer>();
	private static HashMap<ItemStack,LinkedList<RecipeContainer>> ingredientRecipeMap = new HashMap<ItemStack,LinkedList<RecipeContainer>>();
	private static HashMap<ItemStack,LinkedList<RecipeContainer>> bottleRecipeMap = new HashMap<ItemStack,LinkedList<RecipeContainer>>();
	//
	
	public boolean isValidIngredientSlot(ItemStack item) {
		item = item.clone();
		item.setAmount(1);
		return validIngredientSlotSet.contains(item);
	}
	
	public boolean isValidBottleSlot(ItemStack item) {
		item = item.clone();
		item.setAmount(1);
		return validBottleSlotSet.contains(item);
	}
	
	public int getMaxStackSizeAllowed(ItemStack item) {
		item = item.clone();
		item.setAmount(1);
		//return value if exists else return -1
		return bottleSlotStackMaxMap.containsKey(item) ? bottleSlotStackMaxMap.get(item) : -1 ;
	}
        
        public LinkedList<RecipeContainer> getRecipesByIngredient(ItemStack ingredient){
            ingredient = ingredient.clone();
            ingredient.setAmount(1);
            return ingredientRecipeMap.get(ingredient);
        }
        
         public LinkedList<RecipeContainer> getRecipesByBottle(ItemStack bottle){
             bottle = bottle.clone();
             bottle.setAmount(1);
             return bottleRecipeMap.get(bottle);
        }

	public void addRecipe(RecipeContainer recipe) {
		recipe = recipe.clone();
		//add to the recipeSet
		recipeSet.add(recipe);
		//
		//ingredient
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
		//bottle
		ItemStack singleBottle = recipe.getBottle();
		singleBottle.setAmount(1);
		validBottleSlotSet.add(singleBottle);
		//stores whichever value for that item in bottleSlot is largest.
		int bottleMax = recipe.getMaxBottleStackSize();
                //debug("bottleMax: "+bottleMax);
		if (bottleSlotStackMaxMap.containsKey(singleBottle)) {
			int storedMax = bottleSlotStackMaxMap.get(singleBottle);
			if(storedMax >= 0 && storedMax < bottleMax) {
				bottleSlotStackMaxMap.replace(singleBottle, bottleMax);
			}
		} else {
			bottleSlotStackMaxMap.put(singleBottle,bottleMax);
		}
		//to find recipe by bottle
		if(bottleRecipeMap.containsKey(singleBottle)) {
			bottleRecipeMap.get(singleBottle).add(recipe);
		}else {
			bottleRecipeMap.put(singleBottle, new LinkedList<RecipeContainer>());
			bottleRecipeMap.get(singleBottle).add(recipe);
		}
		//
		//result
		//result uses the bottle slot as well.
		ItemStack singleResult = recipe.getResult();
		singleResult.setAmount(1);
		validBottleSlotSet.add(singleResult);
		//stores whichever value for that item in bottleSlot is largest.
		int resultMax = recipe.getMaxResultStackSize();
                //debug("resultMax: "+ resultMax);
		if (bottleSlotStackMaxMap.containsKey(singleResult)) {
			int storedMax = bottleSlotStackMaxMap.get(singleResult);
			if(storedMax >= 0 && storedMax < resultMax) {
				bottleSlotStackMaxMap.replace(singleResult, resultMax);
			}
		} else {
			bottleSlotStackMaxMap.put(singleResult,resultMax);
		}
                //debug(bottleSlotStackMaxMap);
	}
	
	public void removeRecipeContainer(RecipeContainer recipe) {
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
	
	private void repopulateValidSlotSets() {
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
