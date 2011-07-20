package com.ubempire.render;

import java.io.*;

/*
 * CopierThread.java
 *
 * Version 1.0
 *
 * Last Edited
 * 12/06/2011
 *
 * written by K900
 *
 */
public class CopierThread extends Thread {
    private File copyTo;
    private File copyFrom;

    // A snippet I Googled. Laaa-zyy... :P
    public void copy(File sourceLocation, File targetLocation) throws IOException {
        if (!sourceLocation.exists()) {
            System.out.println("Source files not found! Please check if you have everything set up correctly.");
            return;
        }
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }

            String[] children = sourceLocation.list();
            for (String aChildren : children) {
                copy(new File(sourceLocation, aChildren),
                        new File(targetLocation, aChildren));
            }
        } else {

            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
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
