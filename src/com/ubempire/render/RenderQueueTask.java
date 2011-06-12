package com.ubempire.render;

import org.bukkit.World;
import org.bukkit.World.Environment;

public class RenderQueueTask implements Runnable {

    BananaMapRender plugin;
    int tileX;
    int tileZ;
    World world;
    
    RenderQueueTask(BananaMapRender plugin, int tileX, int tileZ, World world) {
        this.plugin = plugin;
        this.tileX = tileX;
        this.tileZ = tileZ;
        this.world = world;
    }
    
    public void run() {
        plugin.threadQueue.add(new GeneratorThread(plugin, tileX, tileZ, world, plugin.prepareRegion(world, tileX, tileZ)));
    }
    
}