/*
 * prepThread.java
 * 
 * Version 0.1
 *
 * Last Edited
 * 17/07/2011
 * 
 * written by codename_B
 * 
 */
package com.ubempire.render;

import org.bukkit.World;

public class prepThread extends Thread {
	private int range;
	private int playerX;
	private int playerZ;
	private BananaMapRender plugin;
	private World world;
	prepThread(int range, int playerX, int playerZ, BananaMapRender plugin, World world)
	{
	this.range=range;
	this.playerX=playerX;
	this.playerZ=playerZ;
	this.plugin=plugin;
	this.world=world;
	}
	public void run()
	{
        for (int i = 0; i <= range; i++) {
            for (int x = -i + playerX; x <= i + playerX; x++) {
                for (int z = -i + playerZ; z <= i + playerZ; z++){
                    plugin.threadQueue.add(new GeneratorThread(plugin, x, z, world, plugin.prepareRegion(world, x, z)));
            }
          }
        }
        plugin.chunkToRender();
        interrupt();
	}
}
