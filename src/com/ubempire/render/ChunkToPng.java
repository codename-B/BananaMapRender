/*
 * ChunkToPng.java
 * 
 * Version 0.6
 *
 * Last Edited
 * 05/06/2011
 * 
 * written by codename_B
 * 
 */

package com.ubempire.render;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.WritableRaster;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

import org.bukkit.ChunkSnapshot;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class ChunkToPng {
	final int X = 512, Z = 512;
	
	BananaMapRender plugin;
	BufferedImage img = null;
	File file;
	String directory;
	int zoom = 9-2;
	public int leftshift = 1 << zoom;
	ChunkToPng(BananaMapRender plugin) {
	    this.plugin = plugin;
	    directory = plugin.getDir();
	}

	
	private AffineTransform findTranslation(AffineTransform at, BufferedImage bi) {
	    Point2D p2din, p2dout;

	    p2din = new Point2D.Double(0.0, 0.0);
	    p2dout = at.transform(p2din, null);
	    double ytrans = p2dout.getY();

	    p2din = new Point2D.Double(0, bi.getHeight());
	    p2dout = at.transform(p2din, null);
	    double xtrans = p2dout.getX();

	    AffineTransform tat = new AffineTransform();
	    tat.translate(-xtrans, -ytrans);
	    return tat;
	  }
	
	public boolean makeTile(int tileX, int tileZ, World world, ChunkSnapshot[][] region, boolean isNether) {
		String folder = plugin.getDir(world.getName());
		
		file = new File(folder + ((tileX)) + ","+ ((tileZ)) + ".png");
		


		if (ageInHours(file) > plugin.varExpirationHours()) {
	        img = new BufferedImage(X, Z, BufferedImage.TYPE_INT_RGB);
	        WritableRaster output = img.getRaster();
			
			BananaMapRender.logger.info("Map rendering: " + tileX + " " + tileZ + " START");
			
			for (int cx = 0; cx < 32; cx++)
			    for (int cz = 0; cz < 32; cz++)
			        for (int x = 0; x < 16; x++)
			            for (int z = 0; z < 16; z++) {
			                Color current = getHighestBlockColor(x, z, region[cx][cz], isNether);
			                output.setPixel(cx*16+x, cz*16+z, new int[] {
                                    current.getRed(),
                                    current.getGreen(),
                                    current.getBlue()
                            });
			            }
			

			
			
	        try {
				 AffineTransform at = new AffineTransform();
				 
				 at.rotate(90.0 * Math.PI / 180.0, img.getWidth() / 2.0, img
					        .getHeight() / 2.0);
				 AffineTransform translationTransform;
				    translationTransform = findTranslation(at, img);
				    at.preConcatenate(translationTransform);
				    BufferedImageOp bio;
				    bio = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);

				    img = bio.filter(img, null);
				    
	            BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(file));
	            ImageIO.setCacheDirectory(new File(directory));
	            ImageIO.setUseCache(true);
	            ImageIO.write(img, "png", os);
	            file = null;
	            img.flush();
	            os.flush();
	            
	        } catch (IOException e) {
	            System.err.println("Image not saved: " + e);
	        }
	        
	        BananaMapRender.logger.info("Map rendering: " + tileX + " " + tileZ + " DONE");

			return true;
		}
		
		return false;
	}

	public Color getHighestBlockColor(int x, int z, ChunkSnapshot chunk, boolean isNether) {
		Color color = new Color(200, 80, 5);
		int highest;
		highest = chunk.getHighestBlockYAt(x, z);
		if(chunk.getBlockTypeId(x, highest, z)==0) {highest = chunk.getHighestBlockYAt(x, z) - 1;}
		if (highest < 0) highest = 0;
		if(isNether) {
		    highest=90;
		    int typeId = chunk.getBlockTypeId(x,highest,z);
		    if (typeId == 0) color = new Color(120, 0, 0);
		}
		if(!isNether) {
    		int ID = chunk.getBlockTypeId(x, highest, z);
    		int ID2 = chunk.getBlockData(x, highest, z);
    		color = IdToColor.getColor(ID, ID2, new Vector(x, highest, z), chunk);
		}  
		return color;
	}

	public int ageInHours (File file){
		if (file.exists()) return (int)((System.currentTimeMillis() - file.lastModified())/3600000.0);
		else return 65535;
	}
}
