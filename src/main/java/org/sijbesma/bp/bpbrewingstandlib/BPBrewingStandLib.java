package org.sijbesma.bp.bpbrewingstandlib;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.sijbesma.bp.bpbrewingstandlib.datatypes.FuelItemStack;
import org.sijbesma.bp.bpbrewingstandlib.datatypes.RecipeContainer;
import org.sijbesma.bp.bpbrewingstandlib.event.handlers.BrewerInventoryEventHandler;
import org.sijbesma.bp.bpbrewingstandlib.event.listeners.InventoryEventListener;
import org.sijbesma.bp.bpbrewingstandlib.managers.FuelManager;
import org.sijbesma.bp.bpbrewingstandlib.managers.RecipeManager;
import org.sijbesma.bp.utils.DebugLogger;



public class BPBrewingStandLib extends JavaPlugin{

	
	private static boolean debugFlag = true;
	private static DebugLogger debugLogger;
	
	private static JavaPlugin INSTANCE;
	private static InventoryEventListener inventoryEventListener;
	private static BrewerInventoryEventHandler brewerInventoryEventHandler;

	
	public BPBrewingStandLib() {
		BPBrewingStandLib.INSTANCE = this;
		debugLogger = new DebugLogger(this.getLogger(),debugFlag);
		brewerInventoryEventHandler = new BrewerInventoryEventHandler();
		inventoryEventListener = new InventoryEventListener(brewerInventoryEventHandler);
	}
	
	@Override
	public void onEnable() {
		BPBrewingStandLib.INSTANCE.getServer().getPluginManager().registerEvents(inventoryEventListener, BPBrewingStandLib.INSTANCE);
		registerTestFuels();
		registerTestRecipes();
	}
	
	@Override
	public void onDisable() {
		
	}



	private void registerTestFuels() {
		FuelItemStack singleEnderPearl = new FuelItemStack(new ItemStack(Material.ENDER_PEARL,1),4);
		FuelManager.addFuelItemStack(singleEnderPearl);
		FuelItemStack fourEnderPearls = new FuelItemStack(new ItemStack(Material.ENDER_PEARL,4),24);
		FuelManager.addFuelItemStack(fourEnderPearls);
	}
	
	private void registerTestRecipes() {
		ItemStack ingredient = new ItemStack(Material.SHROOMLIGHT,1);
		ItemStack bottle = new ItemStack(Material.GOLD_INGOT,1);
		ItemStack result = new ItemStack(Material.NETHERITE_INGOT,1);
		RecipeContainer recipe = new RecipeContainer(ingredient, bottle, result);
		recipe.setMaxBottleStackSize(1);
		recipe.setMaxResultStackSize(1);
		RecipeManager.addRecipe(recipe);
		ingredient = new ItemStack(Material.NAUTILUS_SHELL,1);
		bottle = new ItemStack(Material.PACKED_ICE,5);
		result = new ItemStack(Material.HEART_OF_THE_SEA,1);
		recipe.setMaxBottleStackSize(5);
		recipe.setMaxResultStackSize(1);
		recipe = new RecipeContainer(ingredient, bottle, result);
		RecipeManager.addRecipe(recipe);
	}
}
