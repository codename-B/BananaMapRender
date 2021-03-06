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
    RenderSnapshot[][] region = new RenderSnapshot[32][32];

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
        ChunkToPng chunkToPng = new ChunkToPng(plugin);
        if (!chunkToPng.shouldMakeTile(tileX, tileZ, world)) {
    	    done = true;
    	    return;
        }
    	int taskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new RegionGatherer(), 1, 10);
    	try {
    		synchronized (region) {
    			region.wait();
			}
		} catch (InterruptedException ex) {
		}
    	plugin.getServer().getScheduler().cancelTask(taskId);
    	
        chunkToPng.makeTile(tileX, tileZ, world, region, nether);
        done = true;
    }

    private class RegionGatherer implements Runnable {
    	int row = 0;

		public RegionGatherer() {
		}

		@Override
		public void run() {
			if (row < 32) {
				try {
					sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				region[row] = BananaMapRender.prepareRegionRow(world, tileX, tileZ, row++);
			}
    		if (row == 32) {
        		synchronized (region) {
        			region.notifyAll();
    			}
    		}
    	}
    }
}
