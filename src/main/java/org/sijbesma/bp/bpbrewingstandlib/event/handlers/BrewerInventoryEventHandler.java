package org.sijbesma.bp.bpbrewingstandlib.event.handlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.sijbesma.bp.bpbrewingstandlib.BPBrewingStandLib;
import org.sijbesma.bp.bpbrewingstandlib.managers.FuelManager;
import org.sijbesma.bp.bpbrewingstandlib.managers.RecipeManager;
import org.sijbesma.bp.bpbrewingstandlib.datatypes.SlotType;
import org.sijbesma.bp.bpbrewingstandlib.managers.ItemManager;
import org.sijbesma.bp.bpbrewingstandlib.tasks.HopperTask;
import org.sijbesma.bp.bpbrewingstandlib.tasks.RecipeValidationTask;

import static org.sijbesma.bp.utils.DebugLogger.*;

public class BrewerInventoryEventHandler {

    private final int[] slotInsertOrder = new int[]{4, 3, 0, 1, 2};
    private ItemManager itemManager;
    private FuelManager fuelManager;
    private RecipeManager recipeManager;

    public BrewerInventoryEventHandler() {
        this.itemManager = BPBrewingStandLib.INSTANCE.getItemManager();
        this.fuelManager = BPBrewingStandLib.INSTANCE.getFuelManager();
        this.recipeManager = BPBrewingStandLib.INSTANCE.getRecipeManager();
    }

    public void onPlayerLeftClick(InventoryClickEvent event) {
        debug("onPlayerLeftClick", true);
        ItemStack cursorItem = event.getCursor();
        debug("Cursor Item: " + cursorItem);
        boolean changedInventory = false;
        boolean isAllowedInSlot = false;
        SlotType slotType = SlotType.valueOf(event.getSlot());
        debug("Slot Type  : " + slotType);
        switch (slotType) {
            case FUEL:
                isAllowedInSlot = fuelManager.isValidFuelSlotItem(cursorItem);
                break;
            case INGREDIENT:
                isAllowedInSlot = recipeManager.isValidIngredientSlot(cursorItem);
                break;
            case BOTTLE:
                isAllowedInSlot = recipeManager.isValidBottleSlot(cursorItem);
                break;
        }
        debug("Allowed in Slot: " + isAllowedInSlot);
        if (isAllowedInSlot == false) {
            return;
        }
        HumanEntity humanEntity = event.getWhoClicked();
        if (humanEntity instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            changedInventory = itemManager.cursorToSlotLeft(player, event.getView(), event.getSlot());
        }
        if (changedInventory) {
            debug("Updating inventory display for players viewing");
            for (HumanEntity viewer : event.getViewers()) {
                if (viewer instanceof Player) {
                    ((Player) viewer).updateInventory();
                }
            }
            debug("creating new RecipeValidationTask");
            BrewerInventory brewerInventory = (BrewerInventory) event.getView().getTopInventory();
            new RecipeValidationTask(brewerInventory).runTask(BPBrewingStandLib.INSTANCE);
            event.setCancelled(true);
        }
    }

    public void onPlayerRightClick(InventoryClickEvent event) {
        debug("onPlayerRightClick", true);
        int slot = event.getSlot();
        InventoryView inventoryView = event.getView();
        HumanEntity whoClicked = event.getWhoClicked();
        boolean changedInventory = false;
        if (whoClicked instanceof Player) {
            Player player = (Player) whoClicked;
            changedInventory = itemManager.cursorToSlotRight(player, inventoryView, slot);
        }
        if (changedInventory) {
            debug("Updating inventory display for players viewing");
            for (HumanEntity viewer : event.getViewers()) {
                if (viewer instanceof Player) {
                    ((Player) viewer).updateInventory();
                }
            }
            debug("creating new RecipeValidationTask");
            BrewerInventory brewerInventory = (BrewerInventory) event.getView().getTopInventory();
            new RecipeValidationTask(brewerInventory).runTask(BPBrewingStandLib.INSTANCE);
            event.setCancelled(true);
        }
    }

    public void onPlayerShiftClick(InventoryClickEvent event) {
        debug("onPlayerShiftClick", true);
        boolean changedInventory = false;
        InventoryView inventoryView = event.getView();

        ItemStack currentItem = event.getCurrentItem();
        int currentSlot = event.getRawSlot();

        debug("Current Item              : " + currentItem);
        for (int slot : slotInsertOrder) {
            SlotType slotType = SlotType.valueOf(slot);
            debug("Slot    : " + slot);
            debug("SlotType: " + slotType);
            boolean attemptMove = false;
            switch (slotType) {
                case BOTTLE:
                    if (recipeManager.isValidBottleSlot(currentItem)) {
                        attemptMove = true;
                    }
                    break;
                case INGREDIENT:
                    if (recipeManager.isValidIngredientSlot(currentItem)) {
                        attemptMove = true;
                    }
                    break;
                case FUEL:
                    if (fuelManager.isValidFuelSlotItem(currentItem)) {
                        attemptMove = true;
                    }
                    break;
                default:
                    debug("slottype switch case defaulted, this really shouldn't be possible");
                    break;
            }
            debug("Attempt Move: " + attemptMove);
            if (attemptMove) {
                if (itemManager.slotToSlot(currentSlot, slot, inventoryView)) {
                    changedInventory = true;
                    break;
                }
                debug("Move Failed");
            }
        }

        if (changedInventory) {
            debug("Updating inventory display for players viewing");
            for (HumanEntity viewer : event.getViewers()) {
                if (viewer instanceof Player) {
                    ((Player) viewer).updateInventory();
                }
            }
            debug("creating new RecipeValidationTask");
            BrewerInventory brewerInventory = (BrewerInventory) event.getView().getTopInventory();
            new RecipeValidationTask(brewerInventory).runTask(BPBrewingStandLib.INSTANCE);
            event.setCancelled(true);
        }

    }

    public void onTopHopperMoveItemEvent(InventoryMoveItemEvent event) {
        debug("onTopHopperMoveItemEvent", true);
        ItemStack itemToMove = event.getItem();

        if (itemToMove == null) {
            return;
        }
        boolean validIngredientSlot = recipeManager.isValidIngredientSlot(itemToMove);
        if (validIngredientSlot) {
            debug("creating new HopperTask");
            new HopperTask(event, Arrays.asList(3)).runTask(BPBrewingStandLib.INSTANCE);
            event.setCancelled(true);
        } else {
            return;
        }

    }

    public void onSideHopperMoveItemEvent(InventoryMoveItemEvent event) {
        debug("onSideHopperMoveItemEvent", true);
        ItemStack itemToMove = event.getItem();
        if (itemToMove == null) {
            return;
        }
        boolean validFuelSlot = fuelManager.isValidFuelSlotItem(itemToMove);
        boolean validBottleSlot = recipeManager.isValidBottleSlot(itemToMove);
        List<Integer> allowedSlots = new LinkedList<Integer>();
        if (validFuelSlot) {
            allowedSlots.add(4);
        }
        if (validBottleSlot) {
            allowedSlots.add(0);
            allowedSlots.add(1);
            allowedSlots.add(2);
        }
        if (validFuelSlot || validBottleSlot) {
            debug("creating new HopperTask");
            new HopperTask(event, allowedSlots).runTask(BPBrewingStandLib.INSTANCE);
            event.setCancelled(true);
        }
    }

}
