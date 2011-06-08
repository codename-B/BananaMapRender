/*
 * RegionReader.java
 * 
 * Version 0.3
 * 
 * Last Edited
 * 06/06/2011
 * 
 * written by codename_B
 * 
 */
package com.ubempire.render;

import java.io.File;

import org.bukkit.World;
import org.bukkit.util.config.Configuration;


public class RegionReader {
	
    public static Configuration getRegions(World world) {
		File filepath = new File("plugins/WorldGuard/worlds/" + world.getName() + "/regions.yml");
		Configuration c = null;
		if (filepath.exists())
		    (c = new Configuration(filepath)).load();
        return c;
	}
    
}
