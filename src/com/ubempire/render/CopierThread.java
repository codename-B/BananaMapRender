package com.ubempire.render;

import java.io.*;

import org.bukkit.util.FileUtil;

/*
 * CopierThread.java
 *
 * Version 1.0
 *
 * Last Edited
 * 18/07/2011
 *
 * written by K900
 * forked by Nightgunner5
 */
public class CopierThread extends Thread {
    private File copyTo;
    private File copyFrom;

    // A snippet I Googled. Laaa-zyy... :P
    public void copy(File sourceLocation, File targetLocation) throws IOException {
        if (!sourceLocation.exists()) {
            System.out.println("Source files not found! Please check if you have everything set up correctly.");
        }
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdirs();
            }

            File[] children = sourceLocation.listFiles();
            for (File child : children) {
                copy(child, child);
            }
        } else {
        	FileUtil.copy(sourceLocation, targetLocation);
        }
    }

    CopierThread(File copyFrom, File copyTo) {
        super();
        this.copyFrom = copyFrom;
        this.copyTo = copyTo;
    }

    @Override
	public void run() {
        try {
            copy(copyFrom, copyTo);
            System.out.println("Done copying files.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
