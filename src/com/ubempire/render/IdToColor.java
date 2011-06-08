/*
 * IdToColor.java
 * 
 * Version 3.2
 *
 * Last Edited
 * 07/06/2011
 * 
 * written by codename_B
 * 
 */

package com.ubempire.render;

import java.awt.Color;

import org.bukkit.ChunkSnapshot;
//import org.bukkit.block.Biome;
import org.bukkit.util.Vector;

public class IdToColor {
	/*
	 * Provides a color value for every single ID that is likely to be encountered and renders a greyscale for any that haven't been placed
	 * just incase notch decides to try to break my code by adding new blocks.
	 * 
	 * Damage is looked at on leaves and wool.
	 * 
	 * Depth is looked at on water.
	 * 
	 * Height relative to the sky is looked at on grass and sand.
	 */
	private static boolean showdepth = true;
	public static Color getColor(int id, int damage, Vector vec, ChunkSnapshot chunk) {
		int cv = id*6;
		if(cv > 255) cv = 255;
		Color color = new Color(cv, cv, cv);
		if(id == 0) color = new Color(255,255,255);
		if(id == 1) color = new Color(139,137,137);
		if(id == 2 || id == 31 || id == 37 || id == 38 || id == 39 || id == 40 || id == 59) {
			int[] bs= new int[]{15, 0, 0}; //biomeShader(location.getBlock().getBiome());
			int mxp; mxp = 255;
			if(showdepth){
			int multiplier = vec.getBlockY();
			mxp = 255-((128-multiplier)*2-1)+bs[2];
			if(mxp>255)mxp=255;
			}
			color = new Color(bs[0], mxp, bs[1]);
		}
		if(id == 3 || id == 60 || id == 88) color = new Color(139,69,19);
		if(id == 4) color = new Color(205,197,191);
		if(id == 5 || id == 53 || id == 54 || id == 58 || id == 85 || id == 86 || id>=90) color = new Color(148,124,80);
		if(id == 6) color = new Color(139,69,19);
		if(id == 7) color = new Color(52,52,52);
		if(id == 8 || id == 9) {
            int newR = 20; int newG = 20; int newB = 200;
            if(showdepth) {
    		    color = new Color(44,70,163);
        		Vector proc = vec;
        		int Y = vec.getBlockY();
        		int procID = id;
        		while(proc.getY()>1 && (procID == 8 || procID == 9)) {
        			proc.setY(proc.getY()-1);
        			procID = chunk.getBlockTypeId(proc.getBlockX(), proc.getBlockY(), proc.getBlockZ());
        			newR = (18-(Y-proc.getBlockY()))*4;
        			newG = (18-(Y-proc.getBlockY()))*7;
        			newB = (18-(Y-proc.getBlockY()))*16;
        			if(newR<0)newR=0;
        			if(newG<0)newG=0;
        			if(newB<0)newB=0;
        			if(newR>255)newR=255;
        			if(newG>255)newG=255;
        			if(newB>255)newB=255;
        		}
            }
            color = new Color(newR,newG,newB);
		}
		if( id == 55)color = new Color(252,87,0);
		if(id == 10 || id==11) {
			int newR = 252; int newG = 87; int newB = 0;
			if(showdepth) {
        		Vector proc = vec;
        		int Y = vec.getBlockY();
        		int procID = id;
        		while(proc.getY()>3 && (procID == 10 || procID == 11)) {
        			proc.setY(proc.getY()-3);
        			procID = chunk.getBlockTypeId(proc.getBlockX(), proc.getBlockY(), proc.getBlockZ());
        			newR = (54-(Y-proc.getBlockY()))*4;
        			newG = (54-(Y-proc.getBlockY()))*1;
        			newB = (54-(Y-proc.getBlockY()))*0;
        			if(newR<0)newR=0;
        			if(newG<0)newG=0;
        			if(newB<0)newB=0;
        			if(newR>252)newR=252;
        			if(newG>87)newG=87;
        			if(newB>0)newB=05;
        		}
			}
			color = new Color(newR,newG,newB);
		}		
		if(id == 12) {   
			int xiG=230;
			if(showdepth) {
    			int multiplier = vec.getBlockY();
    			int mxp = 255-((128-multiplier)*2-1)+70;
    			int mxR = mxp;
    			if(mxR>255)mxR=255;
    			xiG = mxR-70;
			}
			color = new Color(xiG+70, xiG+50, xiG+30);
		}
		if(id == 13 || id == 14 || id == 15 || id == 16 || id == 21 || id == 56 || id == 61 || id == 62 || id == 67 || id == 73 || id == 74) color = new Color(144, 144, 144);
		if(id == 17 || id == 81 || id == 83) color = new Color(160,82,45);

		if(id == 18) {
			if(damage == 0) color = new Color(34,100,34);
			if(damage == 1) color = new Color(40,72,0);
			if(damage == 2) color = new Color(20,105,36);
		}

		if(id == 19 || id == 20) color = new Color(255,255,255);
		if(id == 22) color = new Color(26,70,161);
		if(id == 24) color = new Color(214,207,154);

		if(id == 35) {
			if(damage == 0) color = new Color(241,241,241);
			if(damage == 1) color = new Color(235,129,56);
			if(damage == 2) color = new Color(185,57,197);
			if(damage == 3) color = new Color(126,156,219);
			if(damage == 4) color = new Color(212,187,32);
			if(damage == 5) color = new Color(62,198,49);
			if(damage == 6) color = new Color(221,141,163);
			if(damage == 7) color = new Color(63,63,63);
			if(damage == 8) color = new Color(173,180,180);
			if(damage == 9) color = new Color(31,96,123);
			if(damage == 10) color = new Color(135,56,205);
			if(damage == 11) color = new Color(35,46,141);
			if(damage == 12) color = new Color(82,49,27);
			if(damage == 13) color = new Color(54,74,24);
			if(damage == 14) color = new Color(167,45,41);
			if(damage == 15) color = new Color(0,0,0);
		}

		if(id == 41) color = new Color(255,251,86);
		if(id == 42) color = new Color(240,240,240);
		if(id == 43 || id == 44) color = new Color(164,164,164);
		if(id == 45 || id == 46 || id == 47) color = new Color(157,77,55);
		if(id == 48) color = new Color(33,76,33);
		if(id == 49) color = new Color(15,15,24);
		if(id == 50 || id == 51 || id == 52) color = new Color(255,255,255);
		if(id == 57) color = new Color(156,234,231);
		if(id == 79)	 {
			int variator = 0;
    		if(Math.random()>0.5){variator = (int) ((Math.random()*15));
    	}
    			
		color = new Color(90+variator,134+variator,191+variator);}
		
		if(id == 87) {
			int mxp = 128;
			if(showdepth) {
    			int multiplier = vec.getBlockY();
    			mxp = (255-((128-multiplier)*2))/2;
			}
			color = new Color(mxp,8,8);
		}
		if(id == 89) color = new Color(37,64,48);

		return color;
	}
	/*
	private static int[] biomeShader(Biome biome) {
		int[] bs=new int[3];
		bs[2]=0;
		if(biome==Biome.DESERT){bs[0]=25;bs[1]=22;}
		
		if(biome==Biome.FOREST){bs[0]=15;bs[1]=0;}
		
		if(biome==Biome.ICE_DESERT){bs[0]=0;bs[1]=36;}
		
		if(biome==Biome.PLAINS){bs[0]=55;bs[1]=33;}
		
		if(biome==Biome.RAINFOREST){bs[0]=9;bs[1]=17;}
		
		if(biome==Biome.SAVANNA){bs[0]=40;bs[1]=12;}
		
		if(biome==Biome.SEASONAL_FOREST){bs[0]=50;bs[1]=0;}
		
		if(biome==Biome.SHRUBLAND){bs[0]=20;bs[1]=30;}
		
		if(biome==Biome.SWAMPLAND){bs[0]=3;bs[1]=9;}
		
		if(biome==Biome.TAIGA){bs[0]=22;bs[1]=5;}
		
		if(biome==Biome.TUNDRA){
			int random = (int) (Math.random()*20);
			bs[0]=218;bs[1]=225+random;bs[2]=80+random;
			
		}
		return bs;
	}
	*/
}
