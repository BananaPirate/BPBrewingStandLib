/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sijbesma.bp.bpbrewingstandlib.managers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import org.bukkit.Location;
import org.sijbesma.bp.bpbrewingstandlib.BPBrewingStandLib;
import org.sijbesma.bp.bpbrewingstandlib.tasks.BrewingTask;
import static org.sijbesma.bp.utils.DebugLogger.debug;

/**
 *
 * @author JJS
 */
public class BrewingManager {

    private static final HashSet<BrewingTask> runningTasks = new HashSet<BrewingTask>();
    private static final HashSet<BrewingTask> pausedTasks = new HashSet<BrewingTask>();

    private static final HashMap<Location, BrewingTask> locationTaskMap = new HashMap<Location, BrewingTask>();
    private static final HashMap<Location, LinkedList<BrewingTask>> chunkLocationTaskMap = new HashMap<Location, LinkedList<BrewingTask>>();

    public void pauseTask(BrewingTask task) {
        debug("pausing task");
        if (runningTasks.contains(task)) {
            task.pause();
            runningTasks.remove(task);
            pausedTasks.add(task);
            debug("task ID: " + task.getTaskId());
        }
    }

    public void resumeTask(BrewingTask task) {
        debug("resuming task");
        if (pausedTasks.contains(task)) {
            task.resume();
            runningTasks.add(task);
            pausedTasks.remove(task);
            debug("task ID: " + task.getTaskId());
        }
    }

    public void startTask(BrewingTask task) {
        debug("starting\\scheduling task");
        task.runTaskTimer(BPBrewingStandLib.INSTANCE, 1, 1);
        resumeTask(task);
    }

    public void removeTask(BrewingTask task) {
        debug("removeTask", true);
        try {
            task.cancel();
        } catch (IllegalStateException e) {
            //task is already cancelled.
        }
        if (pausedTasks.contains(task)) {
            pausedTasks.remove(task);
        }
        if (runningTasks.contains(task)) {
            runningTasks.remove(task);
        }
        locationTaskMap.remove(task.getLocation());
        chunkLocationTaskMap.get(task.getChunkLocation()).remove(task);
        if (chunkLocationTaskMap.get(task.getChunkLocation()).isEmpty()) {
            chunkLocationTaskMap.remove(task.getChunkLocation());
        }
    }

    public void registerTask(BrewingTask task) {
        debug("registerTask", true);
        if (task.isPaused()) {
            pausedTasks.add(task);
        } else {
            runningTasks.add(task);
        }
        locationTaskMap.put(task.getLocation(), task);
        if (chunkLocationTaskMap.containsKey(task.getChunkLocation())) {
            chunkLocationTaskMap.get(task.getChunkLocation()).add(task);
        } else {
            chunkLocationTaskMap.put(task.getChunkLocation(), new LinkedList<BrewingTask>());
            chunkLocationTaskMap.get(task.getChunkLocation()).add(task);
        }
    }

    public BrewingTask getTaskByLocation(Location location) {
        return locationTaskMap.get(location);
    }

    public LinkedList<BrewingTask> getTasksByChunkLocation(Location location) {
        return chunkLocationTaskMap.get(location);
    }

    public void pauseTasksInChunk(Location location) {
        if (chunkLocationTaskMap.containsKey(location)) {
            debug("Pausing tasks in chunk: " + location);
            LinkedList<BrewingTask> tasks = chunkLocationTaskMap.get(location);
            for (BrewingTask task : tasks) {
                pauseTask(task);
            }
        }
    }

    public void resumeTasksInChunk(Location location) {
        if (chunkLocationTaskMap.containsKey(location)) {
            debug("resuming tasks in chunk: " + location);
            LinkedList<BrewingTask> tasks = chunkLocationTaskMap.get(location);
            for (BrewingTask task : tasks) {
                resumeTask(task);
            }
        }
    }
    
    public boolean chunkHasTasks(Location location){
        return chunkLocationTaskMap.containsKey(location);
    }

}
