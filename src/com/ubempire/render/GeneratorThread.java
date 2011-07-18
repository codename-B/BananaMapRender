package com.ubempire.render;
/*
 * GeneratorThread.java
 *
 * Version 0.1
 *
 * Last Edited
 * 18/07/2011
 *
 * written by codename_B
 * forked by K900
 * forked by Nightgunner5
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

    GeneratorThread(BananaMapRender plugin, int tileX, int tileZ, World world) {
        super();
        this.plugin = plugin;
        this.tileX = tileX;
        this.tileZ = tileZ;
        this.world = world;
        this.nether = (world.getEnvironment() == World.Environment.NETHER);
    }

    @Override
	public void run() {
    	region = BananaMapRender.prepareRegion(world, tileX, tileZ);
        (new ChunkToPng(plugin)).makeTile(tileX, tileZ, world, region, nether);
        done = true;
    }
}
