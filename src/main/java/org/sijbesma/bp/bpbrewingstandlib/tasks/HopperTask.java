package org.sijbesma.bp.bpbrewingstandlib.tasks;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.sijbesma.bp.bpbrewingstandlib.BPBrewingStandLib;
import static org.sijbesma.bp.utils.DebugLogger.debug;

public class HopperTask extends BukkitRunnable {

    private InventoryMoveItemEvent event;
    private LinkedList<Integer> destinationSlots;

    public HopperTask(InventoryMoveItemEvent event, List<Integer> destinationSlots) {
        this.event = event;
        this.destinationSlots = new LinkedList<Integer>(destinationSlots);
    }

    @Override
    public void run() {
        debug("running HopperTask", true);
        debug("id               : " + this.getTaskId());
        debug("destination slots: " + destinationSlots);
        LinkedList<Integer> slotList = slotsToMove();
        debug("source slots     : " + slotList);
        boolean success = transferItems(slotList);
        debug("moved Items      : " + success);
        if (success) {
            debug("Updating inventory display for players viewing");
            List<HumanEntity> viewers = event.getDestination().getViewers();
            viewers.addAll(event.getSource().getViewers());
            for (HumanEntity viewer : viewers) {
                if (viewer instanceof Player) {
                    ((Player) viewer).updateInventory();
                }
            }
            debug("creating new RecipeValidationTask");
            BrewerInventory brewerInventory = (BrewerInventory) event.getDestination();
            new RecipeValidationTask(brewerInventory).runTask(BPBrewingStandLib.INSTANCE);
        }
        debug("End of Task", true);
    }

    private LinkedList<Integer> slotsToMove() {
        int transferRate = BPBrewingStandLib.getHopperTransferRate();
        LinkedList<Integer> slotList = new LinkedList<Integer>();
        ItemStack itemToMove = event.getItem();
        int maxAllowedDestination = BPBrewingStandLib.getRecipeManager().getMaxStackSizeAllowed(itemToMove);
        ItemStack[] sourceItems = event.getSource().getContents();
        int availableToMove = 0;
        int stacksToMove = 0;
        for (int i = 0; i < sourceItems.length; i++) {
            ItemStack item = sourceItems[i];
            if (item == null) {
                continue;
            }
            if (itemToMove.isSimilar(item)) {
                availableToMove += item.getAmount();
                slotList.add(i);
                if (availableToMove >= transferRate) {
                    break;
                }
            }
        }
        return slotList;
    }

    private boolean transferItems(List<Integer> sourceSlots) {
        int transferRate = BPBrewingStandLib.getHopperTransferRate();
        int movedItems = 0;
        Inventory sourceInv = event.getSource();
        Inventory destiInv = event.getDestination();
        ItemStack itemToMove = event.getItem();

        for (int sourceSlot : sourceSlots) {
            if (transferRate - movedItems <= 0) {
                break;
            }
            ItemStack sourceSlotStack = sourceInv.getItem(sourceSlot);
            int sourceSlotAmount = sourceSlotStack.getAmount();
            int amountToGrab = transferRate - movedItems;
            if (amountToGrab > sourceSlotAmount) {
                amountToGrab = sourceSlotAmount;
            }
            ListIterator<Integer> it = destinationSlots.listIterator();
            while (it.hasNext()) {
                if (transferRate - movedItems <= 0) {
                    break;
                }
                int destinationSlot = it.next();
                ItemStack destinationStack = destiInv.getItem(destinationSlot);
                // if null set to air to prevent nullpointer

                // if not null and not air and not similar then slot is invalid so remove it from future checks.
                if (!(destinationStack == null) && !destinationStack.getType().isAir() && !destinationStack.isSimilar(itemToMove)) {
                    it.remove();
                    continue;
                }
                //destinationStack is going to be altered from here on, so might as well set it to the same item
                if (destinationStack == null || destinationStack.getType().isAir()) {
                    destinationStack = new ItemStack(itemToMove);
                    destinationStack.setAmount(0);
                } // the only other option left is that destinationStack is similar

                int destiAmount = destinationStack.getAmount();
                int destiMax = BPBrewingStandLib.getRecipeManager().getMaxStackSizeAllowed(itemToMove);
                if (destiMax == -1) {
                    destiMax = itemToMove.getMaxStackSize();
                }
                int destiSpace = destiMax - destiAmount;

                //more space than stuff to grab
                if (destiSpace >= amountToGrab) {
                    destinationStack.setAmount(destiAmount + amountToGrab);
                    if (sourceSlotAmount - amountToGrab > 0) { // stuff left
                        sourceSlotStack.setAmount(sourceSlotAmount - amountToGrab);
                    } else { // no stuff left
                        sourceSlotStack = new ItemStack(Material.AIR, 0);
                    }
                    destiInv.setItem(destinationSlot, destinationStack);
                    sourceInv.setItem(sourceSlot, sourceSlotStack);
                    movedItems = movedItems + amountToGrab;
                    break; // source slot has no items left to move, so continue with the next source slot.
                } else {
                    //less space than stuff to grab
                    destinationStack.setAmount(destiAmount + destiSpace);
                    sourceSlotStack.setAmount(sourceSlotAmount - destiSpace);
                    amountToGrab = amountToGrab - destiSpace;
                    destiInv.setItem(destinationSlot, destinationStack);
                    sourceInv.setItem(sourceSlot, sourceSlotStack);
                    movedItems = movedItems + destiSpace;
                    //no more space, so remove this from the valid slots
                    it.remove();
                }
            }
        }

        if (movedItems == 0) {
            return false;
        } else {
            return true;
        }
    }

}
