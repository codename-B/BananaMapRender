package com.ubempire.render;
/*
 * GeneratorThread.java
 *
 * Version 0.1
 *
 * Last Edited
 * 12/06/2011
 *
 * written by codename_B
 * forked by K900
 *
 */

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

    GeneratorThread(BananaMapRender plugin, int tileX, int tileZ, World world, ChunkSnapshot[][] region) {
        super();
        this.plugin = plugin;
        this.tileX = tileX;
        this.tileZ = tileZ;
        this.world = world;
        this.region = region;
        this.nether = (world.getEnvironment() == World.Environment.NETHER);
    }

    public void run() {
        (new ChunkToPng(plugin)).makeTile(tileX, tileZ, world, region, nether);
        done = true;
    }

}
