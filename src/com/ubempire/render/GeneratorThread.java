package com.ubempire.render;

import org.bukkit.ChunkSnapshot;
import org.bukkit.World;

public class GeneratorThread extends Thread {

    BananaMapRender plugin;
    int tileX;
    int tileZ;
    World world;
    boolean nether = false;
    boolean done = false;
    ChunkSnapshot[][] region;
    
    GeneratorThread(BananaMapRender plugin, int tileX, int tileZ, World world, ChunkSnapshot[][] region, boolean nether) {
        super();
        this.plugin = plugin;
        this.tileX = tileX;
        this.tileZ = tileZ;
        this.world = world;
        this.region = region;
        this.nether = nether;
    }
    
    public void run() {
        (new ChunkToPng(plugin)).makeTile(tileX, tileZ, world, region, nether);
        done = true;
    }
    
}
