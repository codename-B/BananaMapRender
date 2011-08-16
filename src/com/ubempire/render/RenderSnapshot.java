package com.ubempire.render;

import java.awt.Color;

import org.bukkit.ChunkSnapshot;
import org.bukkit.World;
import org.bukkit.World.Environment;

public class RenderSnapshot {
	public Color[][] chunkColors;
	public RenderSnapshot(World world, ChunkSnapshot[] snapss){
		this.chunkColors = new Color[16][16];
		ChunkSnapshot snaps = snapss[0];
		ChunkSnapshot xupBeyond = snapss[1];
		ChunkSnapshot zupBeyond = snapss[3];
		ChunkSnapshot xdownBeyond = snapss[2];
		ChunkSnapshot zdownBeyond = snapss[4];
		boolean isNether = false;
		if(world.getEnvironment()==Environment.NETHER)
			isNether = true;
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                Color current = ChunkToPng.getHighestBlockColor(x, z, snaps, isNether);
        			int y = snaps.getHighestBlockYAt(x, z)-1;
        			/*
        			 * x==0
        			 */
        			if(x==0)
        			{
        				int yX = xdownBeyond.getHighestBlockYAt(15, z)-1;
            			int diff = (y-yX)*3;
            			current = new Color(normalise(current.getRed()+diff), normalise(current.getGreen()+diff), normalise(current.getBlue()+diff));
        			}
        			else
        			{
        				int yX = snaps.getHighestBlockYAt(x-1, z)-1;
            			int diff = (y-yX)*3;
            			current = new Color(normalise(current.getRed()+diff), normalise(current.getGreen()+diff), normalise(current.getBlue()+diff));	
        			}
        			/*
        			 * z==0
        			 */
        			if(z==0)
        			{
        				int yZ = zdownBeyond.getHighestBlockYAt(x, 15)-1;
            			int diff = (y-yZ)*3;
            			current = new Color(normalise(current.getRed()+diff), normalise(current.getGreen()+diff), normalise(current.getBlue()+diff));
        			}
        			else
        			{
        				int yZ = snaps.getHighestBlockYAt(x, z-1)-1;
            			int diff = (y-yZ)*3;
            			current = new Color(normalise(current.getRed()+diff), normalise(current.getGreen()+diff), normalise(current.getBlue()+diff));	
        			}
        			/*
        			 * x==15
        			 */
        			if(x==15)
        			{
        				int yX = xupBeyond.getHighestBlockYAt(0, z)-1;
            			int diff = (y-yX)*3;
            			current = new Color(normalise(current.getRed()+diff), normalise(current.getGreen()+diff), normalise(current.getBlue()+diff));
        			}
        			else
        			{
        				int yX = snaps.getHighestBlockYAt(x+1, z)-1;
            			int diff = (y-yX)*3;
            			current = new Color(normalise(current.getRed()+diff), normalise(current.getGreen()+diff), normalise(current.getBlue()+diff));	
        			}
        			/*
        			 * z==15
        			 */
        			if(z==15)
        			{
        				int yZ = zupBeyond.getHighestBlockYAt(x, 0)-1;
            			int diff = (y-yZ)*3;
            			current = new Color(normalise(current.getRed()+diff), normalise(current.getGreen()+diff), normalise(current.getBlue()+diff));
        			}
        			else
        			{
        				int yZ = snaps.getHighestBlockYAt(x, z+1)-1;
            			int diff = (y-yZ)*3;
            			current = new Color(normalise(current.getRed()+diff), normalise(current.getGreen()+diff), normalise(current.getBlue()+diff));	
        			}
        			chunkColors[x][z] = current;
            }
        }
        /*
         * Don't know if I need to do this - can't hurt to try.
         */
        world=null;
        snaps=null;
        zupBeyond=null;
        zdownBeyond=null;
        xupBeyond=null;
        xdownBeyond=null;
        snapss=null;
	}
	private int normalise(int rDiff) {
		if(rDiff>255)
		return 255;
		else if(rDiff<0)
		return 0;
		return  rDiff;
	}
	public Color getColor(int x, int z)
	{
		return chunkColors[x][z];
	}

}
