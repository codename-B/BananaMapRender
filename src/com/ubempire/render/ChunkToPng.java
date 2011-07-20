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

import org.bukkit.ChunkSnapshot;
import org.bukkit.World;
import org.bukkit.util.Vector;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.WritableRaster;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class ChunkToPng {
    final int X = 512, Z = 512;

    BananaMapRender plugin;
    BufferedImage img = null;
    File file;
    String directory;

    ChunkToPng(BananaMapRender plugin) {
        this.plugin = plugin;
        directory = plugin.getDir();
    }


    private static AffineTransform findTranslation(AffineTransform at, BufferedImage bi) {
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

    public boolean shouldMakeTile(int tileX, int tileZ, World world) {
        String folder = plugin.getDir(world.getName());

        return ageInHours(new File(folder + ((tileX)) + "," + ((tileZ)) + ".png"))
        		> plugin.varExpirationHours();
    }

    public boolean makeTile(int tileX, int tileZ, World world, ChunkSnapshot[][] region, boolean isNether) {
        String folder = plugin.getDir(world.getName());

        file = new File(folder + ((tileX)) + "," + ((tileZ)) + ".png");


        if (ageInHours(file) > plugin.varExpirationHours()) {
            img = new BufferedImage(X, Z, BufferedImage.TYPE_INT_RGB);
            WritableRaster output = img.getRaster();

            BananaMapRender.logger.info("Map rendering: " + tileX + " " + tileZ + " START");

            for (int cx = 0; cx < 32; cx++)
                for (int cz = 0; cz < 32; cz++)
                    for (int x = 0; x < 16; x++)
                        for (int z = 0; z < 16; z++) {
                            Color current = getHighestBlockColor(x, z, region[cx][cz], isNether);
                    		if (plugin.varDepthNormal()) {
                    			int y = region[cx][cz].getHighestBlockYAt(x, z);
                    			float normalX = 0;
                    			float normalZ = 0;
                    			final int NORMAL_RANGE = 5;

                    			for (int _x = -NORMAL_RANGE; _x <= NORMAL_RANGE; _x++) {
                    				int __x = cx * 16 + x + _x;
                    				if (__x < 0 || __x >= X)
                    					continue;
                    				for (int _y = -NORMAL_RANGE; _y <= NORMAL_RANGE; _y++) {
                    					if (_y + y < 0 || _y + y >= 128)
                    						continue;
                    					for (int _z = -NORMAL_RANGE; _z <= NORMAL_RANGE; _z++) {
                            				int __z = cz * 16 + z + _z;
                            				if (__z < 0 || __z >= Z)
                            					continue;
                            				int id = region[__x / 16][__z / 16].getBlockTypeId(__x % 16, y + _y, __z % 16);
                    						if (id == 0 // Air
                    								|| id == 17 || id == 18 // Trees
                    								|| id == 6 || id == 31 || id == 32 || (id >= 37 && id <= 40)
                    								|| id == 59 || id == 81 || id == 83 // Small plants
                    								|| id == 50 || id == 63 || id == 68 || id == 69 || id == 75
                    								|| id == 76 || id == 77 || id == 93 || id == 94) // Other small objects
                    							continue;
                    						if (_x != 0)
                    							normalX -= (float) _y / (float) _x;
                    						if (_z != 0)
                    							normalZ -= (float) _y / (float) _z;
                    					}
                    				}
                    			}

                    			// Light comes from the north-northeast in this code
                    			int variator = Math.round((2 * normalX - normalZ) / NORMAL_RANGE);
                    			if (cx == 0)
                    				variator /= 16 - x;
                    			if (cx == 31)
                    				variator /= x + 1;
                    			if (cz == 0)
                    				variator /= 16 - z;
                    			if (cz == 31)
                    				variator /= z + 1;
                    			current = new Color(
                    					Math.min(Math.max(current.getRed() + variator, 0), 255),
                    					Math.min(Math.max(current.getGreen() + variator, 0), 255),
                    					Math.min(Math.max(current.getBlue() + variator, 0), 255)
                    					);
                    		}
                            output.setPixel(cx * 16 + x, cz * 16 + z, new int[]{
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

            } catch (Exception e) {
                System.err.println("Did you even create config.yml?");
                e.printStackTrace();
            }

            BananaMapRender.logger.info("Map rendering: " + tileX + " " + tileZ + " DONE");
            
            return true;
        }

        return false;
    }

    public static Color getHighestBlockColor(int x, int z, ChunkSnapshot chunk, boolean isNether) {
        Color color = new Color(200, 80, 5);
        int highest;
        highest = chunk.getHighestBlockYAt(x, z);
        if (chunk.getBlockTypeId(x, highest, z) == 0) {
            highest = chunk.getHighestBlockYAt(x, z) - 1;
        }
        if (highest < 0) highest = 0;
        if (isNether) {
            highest = 90;
            int typeId = chunk.getBlockTypeId(x, highest, z);
            if (typeId == 0) color = new Color(120, 0, 0);
        }
        if (!isNether) {
            int ID = chunk.getBlockTypeId(x, highest, z);
            int ID2 = chunk.getBlockData(x, highest, z);
            color = IdToColor.getColor(ID, ID2, new Vector(x, highest, z), chunk);
        }
        return color;
    }

    public static int ageInHours(File file) {
        if (file.exists()) return (int) ((System.currentTimeMillis() - file.lastModified()) / 3600000L);
		return 65535;
    }
}
