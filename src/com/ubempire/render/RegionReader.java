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
import org.bukkit.plugin.Plugin;
import org.bukkit.util.config.Configuration;


public class RegionReader {
	
    public static Configuration getRegions(Plugin p, World world) {
        Plugin wg = p.getServer().getPluginManager().getPlugin("WorldGuard");
        if (wg == null) return null;
		File filepath = new File(wg.getDataFolder() + "/worlds/" + world.getName() + "/regions.yml");
		if (!filepath.exists()) return null;
		Configuration c = new Configuration(filepath);
		c.load();
        return c;
	}
    
    public static Configuration getBorders(Plugin p) {
        Plugin wb = p.getServer().getPluginManager().getPlugin("WorldBorder");
        if (wb == null) return null;
        return wb.getConfiguration();
    }
    
}
