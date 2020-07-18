package org.sijbesma.bp.bpbrewingstandlib;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.sijbesma.bp.bpbrewingstandlib.containers.FuelContainer;
import org.sijbesma.bp.bpbrewingstandlib.containers.FuelItemStack;
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
}
