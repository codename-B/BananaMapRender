/*
 * BananaMapRender.java
 * 
 * Version 0.0.4
 *
 * Last Edited
 * 04/06/2011
 * 
 * written by codename_B
 * 
 */

package com.ubempire.render;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.logging.Logger;

import net.minecraft.server.ChunkCoordIntPair;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.*;
import org.bukkit.World.Environment;

public class BananaMapRender extends JavaPlugin {
	
    protected final static Logger logger = Logger.getLogger("Minecraft");
    
    Timer renderStarter;
    PlayerScript markups;
    
    int renderThreads;
    List<GeneratorThread> threadQueue;

	public void onDisable() {
		final PluginDescriptionFile pdfFile = getDescription();
		System.out.println("[" + (pdfFile.getName()) + "]" + " version " + pdfFile.getVersion() + " is disabled!");
		getServer().getScheduler().cancelTasks(this);
		renderStarter.cancel();
	}

	public void onEnable() {
	    renderThreads = 0;
	    threadQueue = new LinkedList<GeneratorThread>();
	    
	    markups = new PlayerScript(this);
	    renderStarter = new Timer();
	    renderStarter.schedule(new RenderStarterTask(this), 1000, 1000);
	    
	    for (Plugin p : getServer().getPluginManager().getPlugins())
	        System.out.println(p.getDescription().getName());
	    
	    displayWorldName();

	    getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				for(World world : getServer().getWorlds()){markups.updateMapMarkers(world);}
			}
		}, 0, varMarkerUpdatesFrequency() * 60 * 20);
	    
	    
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				chunkToRender();
			}
		}, 0, varTileCheckerFrequency() * 60 * 20);
		
		final PluginDescriptionFile pdfFile = getDescription();
		System.out.println("[" + (pdfFile.getName()) + "]" + " version " + pdfFile.getVersion() + " is enabled!");
	}
	
	public void displayWorldName() {
		for (World world : getServer().getWorlds()) {
		    String directory = getDir(world.getName());
    		String wfile = directory + "/world.js"; 
    		try {
    		    new File(wfile).createNewFile();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    		PrintWriter out = null;
    		try {
    			out = new PrintWriter(new FileWriter(wfile));
    			out.print("document.getElementById('worldname').innerHTML = '" + world.getName() + "';");
    		} catch (IOException e) {
				e.printStackTrace();
			} finally {
			    if (out != null) out.close();
			}
    	}
	}
	
	public void chunkToRender() {
		for (World world : getServer().getWorlds()) {markups.updateMapMarkers(world);
    		List<Player> players = world.getPlayers();
    		for (Player player : players) {
    			double playerX = player.getLocation().getX();
    			double playerZ = player.getLocation().getZ();
    			int chunkx = (int) (Math.round(playerX / 512));
    			int chunkz = (int) (Math.round(playerZ / 512)); 
    			threadQueue.add(new GeneratorThread(this, chunkx, chunkz, world, prepareRegion(world, chunkx, chunkz),
    			        world.getEnvironment() == Environment.NETHER));
    		}
    		
		}
	}
	
	@SuppressWarnings("unchecked")
	Set<ChunkCoordIntPair> getChunksInUse(World world) {
	    Set<ChunkCoordIntPair> chunksInUse = new HashSet<ChunkCoordIntPair>();
	    List<Player> players = world.getPlayers();
	    for (Player player : players) {
            CraftPlayer p = (CraftPlayer)player;
            chunksInUse.addAll( p.getHandle().g );
	    }
	    return chunksInUse;
	}
	
	ChunkSnapshot[][] prepareRegion(World world, int x, int z) {
	    Set<ChunkCoordIntPair> chunksInUse = getChunksInUse(world);
	    
	    ChunkSnapshot[][] region = new ChunkSnapshot[32][32];
	    for (int i = 0; i < 32; i++)
	        for (int j = 0; j < 32; j++) {
	            int cx = x*32+i, cz = z*32+j;
	            Chunk chunk = world.getChunkAt(cx, cz);
	            region[i][j] = chunk.getChunkSnapshot();
	            if (!chunksInUse.contains(new ChunkCoordIntPair(cx, cz))) {
	                for (Entity e : chunk.getEntities())
	                    if (e instanceof LivingEntity) e.remove();
	                world.unloadChunk(cx, cz, false);
	            }
	        }
	    return region;
	}

	public boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
	    if (!sender.isOp()) return false;
	    try{
    		if (cmd.getName().equalsIgnoreCase("bmr")) {
    			Location loc = null;
    			World world = null;
    			int worldNum = 0;
    			int range = 0;
    			
    			if (args.length > 0) range = Integer.parseInt(args[0]);
    			if (args.length > 1) worldNum = Integer.parseInt(args[1]);
    			
    			//Prevent user from shooting himself in the foot
    			if (range > 4) {
    			    sender.sendMessage(ChatColor.RED + "I'm sorry, Dave. I'm afraid I can't do that.");
    			    return true;
    			}
    
    			//Location to render
    			if (sender instanceof Player) {
    			    final Player player = (Player) sender;
    			    world = player.getWorld();
                    loc = player.getLocation();
    			} else {
                    sender.sendMessage(ChatColor.RED + "You aren't a player");
                    world = getServer().getWorlds().get(worldNum);
                    loc = world.getSpawnLocation();
    			}			    
    
    			//Create directory
    			getDir(world.getName());
    			markups.updateMapMarkers(world);
    			sender.sendMessage(ChatColor.GREEN + "Starting map render");
    			
    			final int playerX = (int)(loc.getX() / 512);
    			final int playerZ = (int)(loc.getZ() / 512);
    			
    			for (int i = 0; i <= range; i++) {
    				for (int x = -i + playerX; x <= i + playerX; x++) {
    					for (int z = -i + playerZ; z <= i + playerZ; z++)
    					    threadQueue.add(new GeneratorThread(this, x, z, world, prepareRegion(world, x, z),
    		                        world.getEnvironment() == Environment.NETHER));
    				}
    			}
    			
    			return true;

    		}
    	} catch (Exception e) {System.out.println("You're doing it wrong");}
		return false;	
	}
	
	// Config variables are fetched here (option names and default values)
	
    public String getDir() {
        return getConfiguration().getString("directory");
    }
    public String getDir(String worldName) {
        String directory = getDir() + worldName + "/";
        File dir = new File((directory.substring(0, directory.length() - (directory.endsWith("/") ? 1 : 0)))); 
        if (!dir.exists()) dir.mkdir();     
        return directory;
    }
	protected int varMaxThreads() {
	    return getConfiguration().getInt("max-threads", 2);
	}
	protected int varExpirationHours() {
	    return getConfiguration().getInt("expiration-hours", 1);
	}
	protected int varTileCheckerFrequency() {
	    return getConfiguration().getInt("tile-checker-frequency", 5);
	}
    protected int varMarkerUpdatesFrequency() {
        return getConfiguration().getInt("marker-updates-frequency", 1);
    }	
	protected boolean varWorldborderEnable() {
	    return getConfiguration().getBoolean("worldborder.enable", false);
	}
	protected String varWorldborderColor() {
	    return getConfiguration().getString("worldborder.color", "#ff0000");
	}
    protected double varWorldborderOpacity() {
        return getConfiguration().getDouble("worldborder.opacity", 0.5);
    }
    protected double varWorldborderFillOpacity() {
        return getConfiguration().getDouble("worldborder.fill-opacity", 0.0);
    }
    protected boolean varWorldguardRegionsEnable() {
        return getConfiguration().getBoolean("worldguard-regions.enable", true);
    }
    protected String varWorldguardRegionsColor() {
        return getConfiguration().getString("worldguard-regions.color", "#0033ff");
    }
    protected double varWorldguardRegionsOpacity() {
        return getConfiguration().getDouble("worldguard-regions.opacity", 0.5);
    }
    protected double varWorldguardRegionsFillOpacity() {
        return getConfiguration().getDouble("worldguard-regions.fill-opacity", 0.1);
    }
    protected boolean varEntitiesEnable() {
        return getConfiguration().getBoolean("entities.enable", true);
    }
    protected int varEntitiesMaxPerChunk() {
        return getConfiguration().getInt("entities.max-per-chunk", 3);
    }
    protected boolean varEntitiesPlayers() {
        return getConfiguration().getBoolean("entities.players", true);
    }
    protected boolean varEntitiesPlayerPopups() {
        return getConfiguration().getBoolean("entities.player-popups", true);
    }
    protected boolean varEntitiesMob(String name) {
        return getConfiguration().getBoolean("entities.mobs." + name, true);
    }
    protected boolean varEntitiesTamedWolves() {
        return getConfiguration().getBoolean("entities.tamed-wolves", true);
    }
    protected boolean varTileEntitiesEnable() {
        return getConfiguration().getBoolean("tile-entities.enable", true);
    }
    protected int varTileEntitiesMaxPerChunk() {
        return getConfiguration().getInt("tile-entities.max-per-chunk", 3);
    }
    protected boolean varTileEntitiesSpawners() {
        return getConfiguration().getBoolean("tile-entities.spawners", false);
    }
    protected double varTileEntitiesSpawnerChance() {
        return getConfiguration().getDouble("tile-entities.spawner-chance", 0.2);
    }
    protected boolean varTileEntitiesSigns() {
        return getConfiguration().getBoolean("tile-entities.signs", true);
    }
		
}

