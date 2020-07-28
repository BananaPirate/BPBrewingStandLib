package org.sijbesma.bp.bpbrewingstandlib.tasks;

import java.util.Arrays;
import java.util.UUID;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.BrewingStand;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.sijbesma.bp.bpbrewingstandlib.BPBrewingStandLib;
import org.sijbesma.bp.bpbrewingstandlib.datatypes.RecipeContainer;
import static org.sijbesma.bp.utils.DebugLogger.debug;

public class BrewingTask extends BukkitRunnable {

    private BrewerInventory brewerInventory;
    private BrewingStand brewingStand;
    private Location brewingStandLocation;
    private boolean paused;
    private RecipeContainer recipe;

    public BrewingTask(BrewingStand brewingStand, RecipeContainer recipe) {
        this.paused = true;
        //
        this.brewingStand = brewingStand;
        this.recipe = recipe;
        //
        this.brewingStandLocation = brewingStand.getLocation();
        this.brewerInventory = brewingStand.getInventory();

    }

    @Override
    public void run() {
        if (!paused) {
            debug("BrewingTask", true);
            debug("ID: " + this.getTaskId());
            debug("Paused: "+this.paused);
            boolean validTask = checkRecipe();
            debug("Valid: " + validTask);
            if (validTask == false) {
                debug("task is no longer valid");
                clean();
            }
            debug("End of Task", true);
        }
    }

    public void pause() {
        debug("paused task: "+this.getTaskId());
        this.paused = true;
    }

    public void resume() {
        debug("resuming task: "+this.getTaskId());
        this.paused = false;
    }

    public boolean isPaused() {
        return this.paused;
    }

    public Location getLocation() {
        return this.brewingStandLocation;
    }

    public Location getChunkLocation() {
        return new Location(this.brewingStand.getWorld(), this.brewingStand.getChunk().getX(), 0, this.brewingStand.getChunk().getZ());
    }

    private boolean checkRecipe() {
        BrewerInventory inventory = brewingStand.getInventory();
        ItemStack recIngredient = recipe.getIngredient();
        ItemStack invIngredient = inventory.getIngredient();
        ItemStack recBottle = recipe.getBottle();
        ItemStack[] invBottles = Arrays.copyOfRange(inventory.getContents(), 0, 2);

        //is the ingredient still the same?
        if (recIngredient.isSimilar(invIngredient)) {
            //enough of the ingredient?
            if (invIngredient.getAmount() >= recIngredient.getAmount()) {
                //time to check the bottle
                boolean match = false;
                for (ItemStack invBottle : invBottles) {
                    if (invBottle == null) {
                        continue;
                    }
                    if (recBottle.isSimilar(invBottle)) {
                        if (recBottle.getAmount() >= invBottle.getAmount()) {
                            match = true;
                            break;
                        }
                    }
                }
                if (match) {
                    return true;
                }
            }
        }
        return false;
    }

    private void clean() {
        debug("cleaning task");
        BPBrewingStandLib.getBrewingManager().removeTask(this);
    }

}
