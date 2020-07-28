package org.sijbesma.bp.bpbrewingstandlib.tasks;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.ListIterator;
import org.bukkit.block.BrewingStand;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.sijbesma.bp.bpbrewingstandlib.BPBrewingStandLib;
import org.sijbesma.bp.bpbrewingstandlib.datatypes.RecipeContainer;
import org.sijbesma.bp.bpbrewingstandlib.managers.BrewingManager;
import org.sijbesma.bp.bpbrewingstandlib.managers.RecipeManager;
import static org.sijbesma.bp.utils.DebugLogger.debug;

public class RecipeValidationTask extends BukkitRunnable {

    private final BrewerInventory inventory;
    private static final RecipeManager recipeManager = BPBrewingStandLib.getRecipeManager();
    private static final BrewingManager brewingManager = BPBrewingStandLib.getBrewingManager();

    public RecipeValidationTask(BrewerInventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public void run() {
        debug("running RecipeValidationTask", true);
        debug("id: " + this.getTaskId());
        LinkedList<RecipeContainer> validRecipes = validateRecipe();
        if (validRecipes == null) {
            debug("no valid recipes found");
        } else {
            debug("valid recipes found");
            debug("starting brewing task for first recipe found");
            BrewingStand brewingStand = inventory.getHolder();
            RecipeContainer firstRecipe = validRecipes.getFirst();
            BrewingTask brewingTask = new BrewingTask(brewingStand,firstRecipe);
            brewingManager.registerTask(brewingTask);
            brewingManager.startTask(brewingTask);
        }
        debug("End of Task", true);
    }

    private LinkedList<RecipeContainer> validateRecipe() {
        ItemStack ingredient = inventory.getIngredient();
        ItemStack[] bottles = Arrays.copyOfRange(inventory.getContents(), 0, 2);
        if (ingredient == null) {
            return null;
        }
        int ingredientAmount = ingredient.getAmount();
        LinkedList<RecipeContainer> recipesByIngredient = recipeManager.getRecipesByIngredient(ingredient);
        if (recipesByIngredient == null) {
            return null;
        }
        recipesByIngredient = new LinkedList<RecipeContainer>(recipesByIngredient);
        //check if there's a recipe that uses less or equal to the amount of ingredient present
        ListIterator<RecipeContainer> it = recipesByIngredient.listIterator();
        while (it.hasNext()) {
            RecipeContainer recipe = it.next();
            int recipeAmount = recipe.getIngredient().getAmount();
            if (ingredientAmount < recipeAmount) {
                //not enough ingredient
                it.remove();
            } else {
                //ingredient amount is valid so lets check the bottles
                boolean foundBottle = false;
                for (ItemStack bottle : bottles) {
                    if (bottle == null) {
                        continue;
                    }
                    ItemStack recipeBottle = recipe.getBottle();
                    if (recipeBottle.isSimilar(bottle)) {
                        int bottleAmount = bottle.getAmount();
                        int recipeBottleAmount = recipeBottle.getAmount();
                        if (bottleAmount >= recipeBottleAmount) {
                            foundBottle = true;
                            break;
                        }
                    }
                }
                if (foundBottle == false) {
                    //no recipe matching the bottle
                    it.remove();
                }
            }
        }
        // no recipes left.
        if (recipesByIngredient.isEmpty()) {
            return null;
        }
        return recipesByIngredient;
    }

}
