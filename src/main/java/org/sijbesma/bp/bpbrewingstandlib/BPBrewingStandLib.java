package org.sijbesma.bp.bpbrewingstandlib;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.sijbesma.bp.bpbrewingstandlib.datatypes.FuelItemStack;
import org.sijbesma.bp.bpbrewingstandlib.datatypes.RecipeContainer;
import org.sijbesma.bp.bpbrewingstandlib.event.handlers.BrewerInventoryEventHandler;
import org.sijbesma.bp.bpbrewingstandlib.event.listeners.InventoryEventListener;
import org.sijbesma.bp.bpbrewingstandlib.event.listeners.WorldEventListener;
import org.sijbesma.bp.bpbrewingstandlib.managers.BrewingManager;
import org.sijbesma.bp.bpbrewingstandlib.managers.FuelManager;
import org.sijbesma.bp.bpbrewingstandlib.managers.ItemManager;
import org.sijbesma.bp.bpbrewingstandlib.managers.RecipeManager;
import org.sijbesma.bp.utils.DebugLogger;
import static org.sijbesma.bp.utils.DebugLogger.debug;

public class BPBrewingStandLib extends JavaPlugin {

    private static final boolean DEBUG_FLAG = true;
    private static DebugLogger debugLogger;

    public static BPBrewingStandLib INSTANCE;
    private static InventoryEventListener inventoryEventListener;
    private static WorldEventListener worldEventListener;
    private static BrewerInventoryEventHandler brewerInventoryEventHandler;

    private static final ItemManager itemManager = new ItemManager();
    private static final RecipeManager recipeManager = new RecipeManager();
    private static final FuelManager fuelManager = new FuelManager();
    private static final BrewingManager brewingManager = new BrewingManager();
    
    private static int hopperTransferRate = 1;

    public BPBrewingStandLib() {
        BPBrewingStandLib.INSTANCE = this;
        debugLogger = new DebugLogger(this.getLogger(), DEBUG_FLAG);
        brewerInventoryEventHandler = new BrewerInventoryEventHandler();
        inventoryEventListener = new InventoryEventListener(brewerInventoryEventHandler);
        worldEventListener = new WorldEventListener(brewingManager);
    }

    @Override
    public void onEnable() {
        debug("onEnable",true);
        BPBrewingStandLib.INSTANCE.getServer().getPluginManager().registerEvents(inventoryEventListener, BPBrewingStandLib.INSTANCE);
        BPBrewingStandLib.INSTANCE.getServer().getPluginManager().registerEvents(worldEventListener, BPBrewingStandLib.INSTANCE);
        registerTestFuels();
        registerTestRecipes();
        debug("End of Enable",true);
    }

    @Override
    public void onDisable() {
        debug("onDisable",true);
        
        
        
        debug("End of Disable", true);
    }

    public static ItemManager getItemManager() {
        return itemManager;
    }

    public static RecipeManager getRecipeManager() {
        return recipeManager;
    }

    public static FuelManager getFuelManager() {
        return fuelManager;
    }
    
    public static BrewingManager getBrewingManager(){
        return brewingManager;
    }

    public static void setHopperTransferRate(int hopperTransferRate) {
        BPBrewingStandLib.hopperTransferRate = hopperTransferRate;
    }

    public static int getHopperTransferRate() {
        return hopperTransferRate;
    }
    
    

    private void registerTestFuels() {
        FuelItemStack singleEnderPearl = new FuelItemStack(new ItemStack(Material.ENDER_PEARL, 1), 4);
        INSTANCE.addFuel(singleEnderPearl);
        FuelItemStack fourEnderPearls = new FuelItemStack(new ItemStack(Material.ENDER_PEARL, 4), 24);
        INSTANCE.addFuel(fourEnderPearls);
    }

    private void registerTestRecipes() {
        ItemStack ingredient = new ItemStack(Material.SHROOMLIGHT, 1);
        ItemStack bottle = new ItemStack(Material.GOLD_INGOT, 1);
        ItemStack result = new ItemStack(Material.NETHERITE_INGOT, 1);
        RecipeContainer recipe = new RecipeContainer(ingredient, bottle, result);
        recipe.setMaxBottleStackSize(1);
        recipe.setMaxResultStackSize(1);
        INSTANCE.addRecipe(recipe);
        ingredient = new ItemStack(Material.NAUTILUS_SHELL, 1);
        bottle = new ItemStack(Material.PACKED_ICE, 5);
        result = new ItemStack(Material.HEART_OF_THE_SEA, 1);
        recipe = new RecipeContainer(ingredient, bottle, result);
        recipe.setMaxBottleStackSize(5);
        recipe.setMaxResultStackSize(1);
        INSTANCE.addRecipe(recipe);
    }
    public static void addRecipe(RecipeContainer recipe) {
        recipeManager.addRecipe(recipe);
    }
    
    public static void removeRecipe(RecipeContainer recipe) {
        recipeManager.removeRecipeContainer(recipe);
    }
    
    public static void addFuel(FuelItemStack fuelItemStack) {
        fuelManager.addFuelItemStack(fuelItemStack);
    }
    
    public static void removeFuel(FuelItemStack fuelItemStack) {
        fuelManager.removeFuelItemStack(fuelItemStack);
    }
}
