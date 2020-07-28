/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sijbesma.bp.bpbrewingstandlib.event.listeners;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.sijbesma.bp.bpbrewingstandlib.managers.BrewingManager;
import static org.sijbesma.bp.utils.DebugLogger.debug;

/**
 *
 * @author JJS
 */
public class WorldEventListener implements Listener {

    private static BrewingManager brewingManager;

    public WorldEventListener(BrewingManager brewingManager) {
        this.brewingManager = brewingManager;
    }

    @EventHandler
    public void onChunkLoadEvent(ChunkLoadEvent event) {

        Chunk chunk = event.getChunk();
        World world = chunk.getWorld();
        int X = chunk.getX();
        int Z = chunk.getZ();
        Location location = new Location(world, X, 0, Z);
        if (brewingManager.chunkHasTasks(location)) {
            debug("onChunkLoadEvent", true);
            brewingManager.resumeTasksInChunk(location);
            debug("End of Event", true);
        }
    }

    @EventHandler
    public void onChunkUnloadEvent(ChunkUnloadEvent event) {

        Chunk chunk = event.getChunk();
        World world = chunk.getWorld();
        int X = chunk.getX();
        int Z = chunk.getZ();
        Location location = new Location(world, X, 0, Z);
        if (brewingManager.chunkHasTasks(location)) {
            debug("onChunkUnloadEvent", true);
            brewingManager.pauseTasksInChunk(location);
            debug("End of Event", true);
        }
    }

}
