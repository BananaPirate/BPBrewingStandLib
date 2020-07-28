/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sijbesma.bp.bpbrewingstandlib.managers;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.sijbesma.bp.bpbrewingstandlib.BPBrewingStandLib;
import static org.sijbesma.bp.utils.DebugLogger.debug;

/**
 *
 * @author JJS
 */
public class ItemManager {

    public boolean cursorToSlotRight(Player player, InventoryView inventoryView, int slot) {
        debug("cursorToSlotRight",true);
        ItemStack itemOnCursor = player.getItemOnCursor();
        if (itemOnCursor == null || itemOnCursor.getType().isAir()) {
            return false;
        }
        int itemOnCursorAmount = itemOnCursor.getAmount();

        ItemStack itemInSlot = inventoryView.getItem(slot);
        if (itemInSlot == null || itemInSlot.getType().isAir()) {
            itemInSlot = new ItemStack(itemOnCursor);
            itemInSlot.setAmount(0);
        }
        int maxItemInSlot = BPBrewingStandLib.getRecipeManager().getMaxStackSizeAllowed(itemInSlot);
        if (maxItemInSlot == -1) {
            maxItemInSlot = itemInSlot.getMaxStackSize();
        }
        int itemInSlotAmount = itemInSlot.getAmount();
        if (itemOnCursor.isSimilar(itemInSlot)) {
            if (itemInSlotAmount < maxItemInSlot) {
                if (itemOnCursorAmount > 1) {
                    itemOnCursor.setAmount(itemOnCursorAmount - 1);
                    itemInSlot.setAmount(itemInSlotAmount + 1);
                    inventoryView.setItem(slot, itemInSlot);
                    player.setItemOnCursor(itemOnCursor);
                    return true;
                } else {
                    itemOnCursor = new ItemStack(Material.AIR, 0);
                    itemInSlot.setAmount(itemInSlotAmount + 1);
                    inventoryView.setItem(slot, itemInSlot);
                    player.setItemOnCursor(itemOnCursor);
                    return true;
                }
            }
        } else {
            return swapItemFromCursor(player, inventoryView, slot);
        }
        return false;
    }

    public boolean cursorToSlotLeft(Player player, InventoryView inventoryView, int slot) {
        debug("cursorToSlotLeft",true);
        ItemStack itemOnCursor = player.getItemOnCursor();
        if (itemOnCursor == null) {
            return false;
        }

        ItemStack itemInSlot = inventoryView.getItem(slot);
        if (itemInSlot == null || itemInSlot.getType().isAir()) {
            return emptyItemFromCursor(player, inventoryView, slot);
        }

        if (itemOnCursor.isSimilar(itemInSlot)) {
            // add items from cursor to stack
            return addItemFromCursor(player, inventoryView, slot);
        } else {
            // swap item and cursor
            return swapItemFromCursor(player, inventoryView, slot);
        }
    }

    public void itemStacktoSlot(ItemStack item, Inventory inventory, int slot) {
        inventory.setItem(slot, item.clone());
    }

    public boolean slotToSlot(int sourceSlot, int targetSlot, InventoryView inventoryView) {
        debug("slotToSlot", true);
        ItemStack sourceStack = inventoryView.getItem(sourceSlot);
        ItemStack targetStack = inventoryView.getItem(targetSlot);
        if (sourceStack == null || sourceStack.getType().isAir()) {
            //cannot move nothing
            return false;
        }
        int sourceAmount = sourceStack.getAmount();
        int targetAmount;
        int targetMax;
        int targetSpace;

        if (targetStack == null || targetStack.getType().isAir()) {
            // target stack is empty
            debug("empty slot routine");
            targetAmount = 0;
            targetMax = BPBrewingStandLib.getRecipeManager().getMaxStackSizeAllowed(sourceStack);
            if (targetMax == -1) {
                targetMax = sourceStack.getMaxStackSize();
            }
            targetSpace = targetMax - targetAmount;
            if (targetSpace >= sourceAmount) {
                targetStack = new ItemStack(sourceStack);
                sourceStack.setAmount(0);
                targetStack.setAmount(targetAmount + sourceAmount);
                inventoryView.setItem(targetSlot, targetStack);
                return true;
            } else if (targetSpace > 0) {
                targetStack = new ItemStack(sourceStack);
                sourceStack.setAmount(sourceAmount - targetSpace);
                targetStack.setAmount(targetMax);
                inventoryView.setItem(targetSlot, targetStack);
                return true;
            }
            return false;
        }

        targetAmount = targetStack.getAmount();
        targetMax = BPBrewingStandLib.getRecipeManager().getMaxStackSizeAllowed(targetStack);
        debug(targetMax);
        if (targetMax == -1) {
            targetMax = targetStack.getMaxStackSize();
        }
        debug(targetMax);

        targetSpace = targetMax - targetAmount;

        if (sourceStack.isSimilar(targetStack)) {
            if (targetSpace >= sourceAmount) {
                sourceStack.setAmount(0);
                targetStack.setAmount(targetAmount + sourceAmount);
                return true;
            } else if (targetSpace > 0) {
                sourceStack.setAmount(sourceAmount - targetSpace);
                targetStack.setAmount(targetMax);
                return true;
            }

        }
        //items do not match so do nothing
        return false;
    }

    private boolean addItemFromCursor(Player player, InventoryView inventoryView, int slot) {
        debug("addItemFromCursor", true);
        ItemStack itemOnCursor = player.getItemOnCursor();
        if (itemOnCursor == null) {
            return false;
        } else {
            itemOnCursor = itemOnCursor.clone();
        }
        ItemStack itemInSlot = inventoryView.getItem(slot);
        if (itemInSlot == null) {
            itemInSlot = new ItemStack(Material.AIR, 0);
        } else {
            itemInSlot = itemInSlot.clone();
        }
        int cursorAmount = itemOnCursor.getAmount();
        int slotAmount = itemInSlot.getAmount();
        int maxAmount = BPBrewingStandLib.getRecipeManager().getMaxStackSizeAllowed(itemInSlot);
        if (maxAmount == -1) {
            maxAmount = itemInSlot.getMaxStackSize();
        }
        int spaceInSlot = maxAmount - itemInSlot.getAmount();
        if (spaceInSlot > 0) {
            if (spaceInSlot >= cursorAmount) {
                itemInSlot.setAmount(slotAmount + cursorAmount);
                itemOnCursor.setAmount(0);
            } else {
                itemInSlot.setAmount(slotAmount + spaceInSlot);
                itemOnCursor.setAmount(cursorAmount - spaceInSlot);
            }
        } else {
            return false;
        }
        player.setItemOnCursor(itemOnCursor);
        inventoryView.setItem(slot, itemInSlot);
        return true;
    }

    private boolean swapItemFromCursor(Player player, InventoryView inventoryView, int slot) {
        debug("swapItemFromCursor", true);
        ItemStack itemOnCursor = player.getItemOnCursor();
        if (itemOnCursor == null || itemOnCursor.getType().isAir()) {
            return false;
        } else {
            itemOnCursor = itemOnCursor.clone();
        }
        ItemStack itemInSlot = inventoryView.getItem(slot);
        if (itemInSlot == null) {
            itemInSlot = new ItemStack(Material.AIR, 0);
        } else {
            itemInSlot = itemInSlot.clone();
        }
        player.setItemOnCursor(itemInSlot);
        inventoryView.setItem(slot, itemOnCursor);
        return true;
    }

    private boolean emptyItemFromCursor(Player player, InventoryView inventoryView, int slot) {
        debug("emptyItemFromCursor");
        ItemStack itemOnCursor = player.getItemOnCursor();
        if (itemOnCursor == null || itemOnCursor.getType().isAir()) {
            return false;
        } else {
            itemOnCursor = itemOnCursor.clone();
        }
        int destinationMax = BPBrewingStandLib.getRecipeManager().getMaxStackSizeAllowed(itemOnCursor);
        if (destinationMax == -1) {
            destinationMax = itemOnCursor.getMaxStackSize();
        }
        int cursorAmount = itemOnCursor.getAmount();
        ItemStack destinationStack = new ItemStack(itemOnCursor);
        if (cursorAmount > destinationMax) {
            itemOnCursor.setAmount(cursorAmount - destinationMax);
            destinationStack.setAmount(destinationMax);
            player.setItemOnCursor(itemOnCursor);
            inventoryView.setItem(slot, destinationStack);
            return true;
        } else {
            itemOnCursor.setAmount(0);
            itemOnCursor.setType(Material.AIR);
            destinationStack.setAmount(cursorAmount);
            player.setItemOnCursor(itemOnCursor);
            inventoryView.setItem(slot, destinationStack);
            return true;
        }
    }

}
